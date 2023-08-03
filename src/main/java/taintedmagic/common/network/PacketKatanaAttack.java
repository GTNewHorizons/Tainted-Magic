package taintedmagic.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.items.tools.ItemKatana;

public class PacketKatanaAttack implements IMessage, IMessageHandler<PacketKatanaAttack, IMessage> {

    private int entityID;
    private int playerID;
    private int dimensionID;
    private float dmg;
    private boolean leech;
    private boolean sus;

    public PacketKatanaAttack() {}

    public PacketKatanaAttack(final Entity entity) {
        entityID = entity.getEntityId();
    }

    private static void log(EntityPlayerMP origin) {
        TaintedMagic.log.warn(
                PacketHandler.SECURITY_MARKER,
                "Player {} tried to kill stuff around it",
                origin.getGameProfile());
    }

    @Override
    public IMessage onMessage(final PacketKatanaAttack message, final MessageContext ctx) {
        EntityPlayerMP realPlayer = ctx.getServerHandler().playerEntity;
        ItemStack item = realPlayer.getCurrentEquippedItem();
        if (item == null || !(item.getItem() instanceof ItemKatana)) {
            log(realPlayer);
            return null;
        }
        MutablePair<Integer, Integer> ticksInUseHolder = ItemKatana.EventHandler.ticksInUse.remove(realPlayer);
        if (ticksInUseHolder == null) {
            log(realPlayer);
            return null;
        }
        int ticksInUse = ticksInUseHolder.right;
        boolean fullyCharged = ticksInUse >= 30;
        float damage = ItemKatana.getAttackDamage(item) * (1 + Math.min(ticksInUse / 40f, 1));
        boolean leech = ItemKatana.getInscription(item) == 2 && fullyCharged;
        if (sus && (realPlayer.getEntityId() != playerID || realPlayer.dimension != dimensionID
                || Math.abs(damage - message.dmg) > 1e-4
                || leech != message.leech)) {
            log(realPlayer);
            return null;
        }
        final World world = realPlayer.getEntityWorld();

        final Entity entity = world.getEntityByID(message.entityID);

        if (entity instanceof EntityLivingBase) {
            double reach = realPlayer.theItemInWorldManager.getBlockReachDistance();
            if (!(realPlayer.getDistanceSqToEntity(entity) < reach * reach)) {
                log(realPlayer);
                return null;
            }

            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(realPlayer, entity), damage);

            if (leech) {
                realPlayer.heal(damage * 0.25F);
                world.playSoundAtEntity(realPlayer, "thaumcraft:wand", 0.5F, 0.5F + ((float) Math.random() * 0.5F));
            }
        }

        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        if (buf.readableBytes() == 0) return;
        // forged packets, but could also be an outdated client
        this.sus = true;
        this.playerID = buf.readInt();
        this.dimensionID = buf.readInt();
        this.dmg = buf.readFloat();
        this.leech = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
    }
}

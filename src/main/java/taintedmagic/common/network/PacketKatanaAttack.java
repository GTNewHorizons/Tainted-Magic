package taintedmagic.common.network;

import java.util.stream.DoubleStream;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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
    private boolean sus;

    public PacketKatanaAttack() {}

    public PacketKatanaAttack(final Entity entity) {
        entityID = entity.getEntityId();
    }

    private static void log(EntityPlayerMP origin) {
        TaintedMagic.logger.warn(
                PacketHandler.SECURITY_MARKER,
                "Player {} tried to kill stuff around it",
                origin.getGameProfile());
    }

    @Override
    public IMessage onMessage(final PacketKatanaAttack message, final MessageContext ctx) {
        EntityPlayerMP realPlayer = ctx.getServerHandler().playerEntity;
        if (realPlayer.getCurrentEquippedItem() == null
                || !(realPlayer.getCurrentEquippedItem().getItem() instanceof ItemKatana)) {
            log(realPlayer);
            return null;
        }
        float damage = ItemKatana.getAttackDamage(realPlayer.getCurrentEquippedItem()) * 1.5f;
        if (sus) {
            if (realPlayer.getEntityId() != playerID || realPlayer.dimension != dimensionID) {
                log(realPlayer);
                return null;
            }
            boolean dmgOk = DoubleStream.of(damage, damage + 1.0).anyMatch(d -> Math.abs(d - dmg) < 1e-4);
            if (!dmgOk) {
                log(realPlayer);
                return null;
            }
        }
        final World world = realPlayer.getEntityWorld();

        if (world.rand.nextInt(10) == 0) damage += 1f;

        final Entity entity = world.getEntityByID(message.entityID);

        if (entity instanceof EntityLivingBase) {
            double reach = realPlayer.theItemInWorldManager.getBlockReachDistance();
            if (realPlayer.getDistanceSqToEntity(entity) < reach * reach) {
                entity.attackEntityFrom(DamageSource.causePlayerDamage(realPlayer).setDamageBypassesArmor(), damage);
            } else {
                log(realPlayer);
            }
        }

        return null;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        entityID = buf.readInt();
        if (buf.readableBytes() == 0) return;
        // forged packets, but could also be an outdated client
        sus = true;
        playerID = buf.readInt();
        dimensionID = buf.readInt();
        dmg = buf.readFloat();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(entityID);
    }
}

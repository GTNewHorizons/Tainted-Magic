package taintedmagic.common.network;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import taintedmagic.client.handler.SashServerHandler;
import taintedmagic.common.items.equipment.ItemVoidwalkerSash;

public class PacketSashToggle implements IMessage, IMessageHandler<PacketSashToggle, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {
        // do nothing
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // do nothing
    }

    @Override
    public IMessage onMessage(PacketSashToggle message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;

        final ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
        if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash) {
            SashServerHandler.toggleSashStatus(player);
        }

        return null;
    }
}

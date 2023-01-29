package taintedmagic.client.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderPlayerEvent;

import taintedmagic.api.IRenderInventoryItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public void tickEnd(final TickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            HUDHandler.updateTicks();
        }
    }

    /*
     * Render items implementing IRenderInventoryItem
     */
    @SubscribeEvent
    public void onPlayerRender(final RenderPlayerEvent.Specials.Post event) {
        final EntityPlayer player = event.entityPlayer;
        if (player.getActivePotionEffect(Potion.invisibility) != null) return;

        final ItemStack[] inv = player.inventory.mainInventory;
        final List<Item> rendering = new ArrayList<>();
        for (final ItemStack stack : inv) {
            if (stack != null && stack.getItem() instanceof IRenderInventoryItem
                    && !rendering.contains(stack.getItem())) {
                ((IRenderInventoryItem) stack.getItem()).render(player, stack, event.partialRenderTick);
                rendering.add(stack.getItem());
            }
        }
        rendering.clear();
    }
}

package taintedmagic.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandUpdateHandler implements IWandRodOnUpdate {

    Aspect aspect;
    Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);

    public void onUpdate(ItemStack itemstack, EntityPlayer player) {
        if (!player.isPotionActive(Config.potionWarpWardID)) {
            int permwarp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());

            int x = permwarp;
            if (x == 0) x++;

            int y = Math.max((200 - x), 50);

            if (player.ticksExisted % y == 0) {
                for (int i = 0; i < primals.length; i++) {
                    ((ItemWandCasting) itemstack.getItem()).addVis(itemstack, this.primals[i], 1, true);
                }
            }
        }
    }
}

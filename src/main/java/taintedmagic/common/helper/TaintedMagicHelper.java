package taintedmagic.common.helper;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TaintedMagicHelper {

    public static AspectList getPrimals(int amount) {
        return new AspectList().add(Aspect.FIRE, amount).add(Aspect.WATER, amount).add(Aspect.EARTH, amount)
                .add(Aspect.AIR, amount).add(Aspect.ORDER, amount).add(Aspect.ENTROPY, amount);
    }

    public static Vector3 getDistanceBetween(Entity e, Entity target) {
        Vector3 fromPosition = new Vector3(e.posX, e.posY, e.posZ);
        Vector3 toPosition = new Vector3(target.posX, target.posY, target.posZ);
        Vector3 dist = fromPosition.sub(toPosition);
        dist.normalize();
        return dist;
    }

    public static double getDistanceTo(double x, double y, double z, EntityPlayer p) {
        double var7 = p.posX + 0.5D - x;
        double var9 = p.posY + 0.5D - y;
        double var11 = p.posZ + 0.5D - z;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    /** get damage with 20% * potency level **/
    public static int getFocusDamageWithPotency(ItemStack stack, int damage) {
        if (stack != null && stack.getItem() instanceof ItemWandCasting) {
            final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
            return (int) ((damage + damage * wand.getFocusPotency(stack) * 0.2) + 0.5);
        } else if (stack != null && stack.getItem() instanceof ItemFocusBasic) {
            final ItemFocusBasic focus = (ItemFocusBasic) stack.getItem();
            return (int) ((damage + damage * focus.getUpgradeLevel(stack, FocusUpgradeType.potency) * 0.2) + 0.5);
        } else return damage;
    }

    public static float getFocusDamageWithPotency(ItemStack stack, int damage, float staffBuff) {
        if (stack != null && stack.getItem() instanceof ItemWandCasting) {
            final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

            if ((wand.getRod(stack) instanceof StaffRod)) return getFocusDamageWithPotency(stack, damage) * staffBuff;
        }

        return getFocusDamageWithPotency(stack, damage);

    }

    /** Get Focus Upgrade from wand or focus */
    public static boolean hasFocusUpgrade(ItemStack stack, FocusUpgradeType upgrade) {

        if (stack != null) {
            Item item = stack.getItem();
            ItemFocusBasic foci;
            ItemStack fociStack;

            if (item instanceof ItemWandCasting) {
                ItemWandCasting wand = (ItemWandCasting) item;
                foci = wand.getFocus(stack);
                fociStack = wand.getFocusItem(stack);
            } else {
                foci = (ItemFocusBasic) item;
                fociStack = stack;
            }
            return foci.isUpgradedWith(fociStack, upgrade);
        }
        return false;
    }

    public static int getFocusLevelUpgrade(ItemStack stack, FocusUpgradeType upgrade) {
        if (stack != null) {
            Item item = stack.getItem();
            ItemFocusBasic foci;
            ItemStack fociStack;

            if (item instanceof ItemWandCasting) {
                ItemWandCasting wand = (ItemWandCasting) item;
                foci = wand.getFocus(stack);
                fociStack = wand.getFocusItem(stack);
            } else {
                foci = (ItemFocusBasic) item;
                fociStack = stack;
            }
            return foci.getUpgradeLevel(fociStack, upgrade);
        }
        return 0;
    }

    public static List addTooltipBaseDamage(List list, int damage, ItemStack stack) {
        list.add(
                EnumChatFormatting.BLUE + ""
                        + TaintedMagicHelper.getFocusDamageWithPotency(stack, damage)
                        + " "
                        + StatCollector.translateToLocal("text.attackdamageequipped"));
        return list;
    }

    public static List addTooltipStaffMultiplier(List list, float multiplier) {
        list.add(
                EnumChatFormatting.BLUE
                        + String.format(StatCollector.translateToLocal("focus.upgrade.staff_tooltip.tip"), multiplier));
        return list;
    }

    public static List addTooltipDamageAndStaffMultiplier(List list, int damage, ItemStack stack, float multiplier) {
        list.add("");
        addTooltipBaseDamage(list, damage, stack);
        addTooltipStaffMultiplier(list, multiplier);
        list.add("");
        return list;
    }

}

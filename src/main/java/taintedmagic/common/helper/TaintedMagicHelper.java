package taintedmagic.common.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
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
        if (stack != null && stack.getItem() instanceof ItemFocusMageMace) {
            final ItemFocusMageMace cap = (ItemFocusMageMace) stack.getItem();
            return (int) ((damage + damage * cap.getUpgradeLevel(stack, FocusUpgradeType.potency) * 0.2) + 0.5);
        } else return damage;
    }

    public static int getWandDamageWithPotency(ItemStack stack, int damage) {
        if (stack != null && stack.getItem() instanceof ItemWandCasting) {
            final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
            return (int) ((damage + damage * wand.getFocusPotency(stack) * 0.2) + 0.5);
        } else return damage;
    }
}

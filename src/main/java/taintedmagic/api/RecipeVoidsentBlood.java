package taintedmagic.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import taintedmagic.common.items.ItemVoidsentBlood;
import thaumcraft.common.items.armor.ItemCultistBoots;
import thaumcraft.common.items.armor.ItemCultistLeaderArmor;
import thaumcraft.common.items.armor.ItemCultistPlateArmor;
import thaumcraft.common.items.armor.ItemCultistRobeArmor;

/**
 * Custom recipe for applying Voidsent Blood to Cult Attire
 */
public class RecipeVoidsentBlood implements IRecipe {

    private boolean isValidArmor(Item item) {
        return item instanceof ItemCultistRobeArmor || item instanceof ItemCultistPlateArmor
                || item instanceof ItemCultistLeaderArmor
                || item instanceof ItemCultistBoots;
    }

    private boolean isValidBlood(Item item) {
        return item instanceof ItemVoidsentBlood;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World w) {
        boolean foundBlood = false;
        boolean foundArmor = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);

            if (itemStack == null) continue;

            if (isValidBlood(itemStack.getItem()) && !foundBlood) {
                foundBlood = true;
            } else if (isValidArmor(itemStack.getItem()) && !foundArmor) {
                foundArmor = true;
            } else {
                return false;
            }
        }

        return foundBlood && foundArmor;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack copy = null;
        ItemStack armor = null;
        ItemStack blood = null;

        // First, find the required ingredients in the crafting grid.
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);

            if (itemStack == null) continue;

            if (isValidArmor(itemStack.getItem())) {
                armor = itemStack;
            } else if (isValidBlood(itemStack.getItem())) {
                blood = itemStack;
            }
        }

        // Check that both items exist and that the armor is not already voidtouched.
        if (armor != null && blood != null
                && !(armor.hasTagCompound() && armor.getTagCompound().getBoolean("voidtouched"))) {
            copy = armor.copy();

            NBTTagCompound nbt = copy.hasTagCompound() ? copy.getTagCompound() : new NBTTagCompound();

            nbt.setBoolean("voidtouched", true);
            copy.setTagCompound(nbt);
        }

        return copy;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}

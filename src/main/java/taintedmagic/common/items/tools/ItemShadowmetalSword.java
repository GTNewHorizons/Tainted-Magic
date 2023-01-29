package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalSword extends ItemSword implements IRepairable {

    public ItemShadowmetalSword(final ToolMaterial material) {
        super(material);
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemShadowmetalSword");
        setUnlocalizedName("ItemShadowmetalSword");
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0)) ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }
}

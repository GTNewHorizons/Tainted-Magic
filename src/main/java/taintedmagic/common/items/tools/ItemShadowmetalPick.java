package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalPick extends ItemPickaxe implements IRepairable
{
    public ItemShadowmetalPick(ToolMaterial m)
    {
        super(m);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setTextureName("taintedmagic:ItemShadowmetalPick");
        this.setUnlocalizedName("ItemShadowmetalPick");
    }

    public boolean getIsRepairable(ItemStack s, ItemStack s2)
    {
        return (s2.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial)) && s2.getItemDamage() == 0) ? true :
                super.getIsRepairable(s, s2);
    }

    @Override
    public EnumRarity getRarity(ItemStack s)
    {
        return EnumRarity.uncommon;
    }
}

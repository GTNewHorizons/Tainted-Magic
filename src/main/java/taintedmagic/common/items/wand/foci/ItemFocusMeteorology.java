package taintedmagic.common.items.wand.foci;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusMeteorology extends ItemFocusBasic {

    IIcon depthIcon = null;

    private static final AspectList costBase = new AspectList().add(Aspect.AIR, 8000).add(Aspect.WATER, 8000)
            .add(Aspect.FIRE, 8000).add(Aspect.EARTH, 8000).add(Aspect.ORDER, 8000).add(Aspect.ENTROPY, 8000);

    public ItemFocusMeteorology() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusMeteorology");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusMeteorology");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMeteorology_depth");
    }

    @Override
    public IIcon getFocusDepthLayerIcon(ItemStack stack) {
        return this.depthIcon;
    }

    @Override
    public String getSortingHelper(ItemStack stack) {
        return "RAIN" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(ItemStack stack) {
        return 0x23D9EA;
    }

    @Override
    public AspectList getVisCost(ItemStack stack) {
        return costBase;
    }

    @Override
    public int getActivationCooldown(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isVisCostPerTick(ItemStack stack) {
        return false;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop) {
        WandManager.setCooldown(player, 30000);
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        wand.consumeAllVis(stack, player, getVisCost(stack), true, false);

        world.getWorldInfo().setRainTime(world.isRaining() ? 24000 : 0);
        world.getWorldInfo().setRaining(!world.isRaining());

        player.playSound("thaumcraft:wand", 0.5F, 1.0F);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return TaintedMagic.rarityCreation;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
        switch (rank) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal };
            default:
                return null;
        }
    }
}

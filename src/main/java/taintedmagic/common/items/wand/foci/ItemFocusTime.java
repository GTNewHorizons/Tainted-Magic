package taintedmagic.common.items.wand.foci;

import java.awt.Color;

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

public class ItemFocusTime extends ItemFocusBasic {

    IIcon depthIcon = null;

    private static final AspectList costBase = new AspectList().add(Aspect.AIR, 8000).add(Aspect.WATER, 8000)
            .add(Aspect.FIRE, 8000).add(Aspect.EARTH, 8000).add(Aspect.ORDER, 8000).add(Aspect.ENTROPY, 8000);

    public ItemFocusTime() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusTime");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusTime");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMeteorology_depth");
    }

    @Override
    public IIcon getFocusDepthLayerIcon(ItemStack s) {
        return this.depthIcon;
    }

    @Override
    public String getSortingHelper(ItemStack s) {
        return "TIME" + super.getSortingHelper(s);
    }

    @Override
    public int getFocusColor(ItemStack s) {
        EntityPlayer player = TaintedMagic.proxy.getClientPlayer();
        return player == null ? 0xFFFFFF : Color.HSBtoRGB(player.ticksExisted * 16 % 360 / 360F, 1F, 1F);
    }

    @Override
    public AspectList getVisCost(ItemStack s) {
        return costBase;
    }

    @Override
    public int getActivationCooldown(ItemStack s) {
        return 1;
    }

    @Override
    public boolean isVisCostPerTick(ItemStack s) {
        return false;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack s) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop) {
        WandManager.setCooldown(p, 30000);
        ItemWandCasting wand = (ItemWandCasting) s.getItem();

        if (wand.consumeAllVis(s, p, getVisCost(s), true, false)) {
            w.setWorldTime( w.getWorldTime() + 12000);
            p.playSound("thaumcraft:wand", 0.5F, 1.0F);
        }
        return s;
    }

    @Override
    public int getColorFromItemStack(ItemStack s, int i) {
        EntityPlayer p = TaintedMagic.proxy.getClientPlayer();
        return p == null ? 0xFFFFFF : Color.HSBtoRGB(p.ticksExisted * 16 % 360 / 360F, 1F, 1F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumRarity getRarity(ItemStack s) {
        return TaintedMagic.rarityCreation;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack s, int r) {
        switch (r) {
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

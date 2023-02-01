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
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusMeteorology extends ItemFocusBasic {

    IIcon depthIcon = null;

    private static final AspectList costBase = new AspectList().add(Aspect.AIR, 1000).add(Aspect.WATER, 1000)
            .add(Aspect.FIRE, 1000).add(Aspect.EARTH, 1000).add(Aspect.ORDER, 1000).add(Aspect.ENTROPY, 1000);

    public ItemFocusMeteorology() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusMeteorology");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusMeteorology");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMeteorology_depth");
    }

    public IIcon getFocusDepthLayerIcon(ItemStack s) {
        return this.depthIcon;
    }

    public String getSortingHelper(ItemStack s) {
        return "RAIN" + super.getSortingHelper(s);
    }

    public int getFocusColor(ItemStack s) {
        return 0x23D9EA;
    }

    public AspectList getVisCost(ItemStack s) {
        return new AspectList().add(Aspect.AIR, 1000).add(Aspect.WATER, 1000).add(Aspect.FIRE, 1000)
                .add(Aspect.EARTH, 1000).add(Aspect.ORDER, 1000).add(Aspect.ENTROPY, 1000);
    }

    public int getActivationCooldown(ItemStack s) {
        return 1;
    }

    public boolean isVisCostPerTick(ItemStack s) {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack s) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick(ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop) {
        WandManager.setCooldown(p, 30000);
        ItemWandCasting wand = (ItemWandCasting) s.getItem();
        wand.consumeAllVis(s, p, getVisCost(s), true, false);

        w.getWorldInfo().setRainTime(w.isRaining() ? 24000 : 0);
        w.getWorldInfo().setRaining(!w.isRaining());

        p.playSound("thaumcraft:wand", 0.5F, 1.0F);
        return s;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack s) {
        return TaintedMagic.rarityCreation;
    }
}

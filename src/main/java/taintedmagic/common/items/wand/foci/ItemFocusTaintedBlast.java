package taintedmagic.common.items.wand.foci;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusTaintedBlast extends ItemFocusBasic {

    private static final AspectList costBase = new AspectList().add(Aspect.ENTROPY, 1000).add(Aspect.EARTH, 1000)
            .add(Aspect.WATER, 1000);
    private final AspectList costEnlarge = new AspectList().add(Aspect.ENTROPY, 250).add(Aspect.EARTH, 250)
            .add(Aspect.WATER, 250);

    public static IIcon depthIcon;
    public static IIcon ornIcon;

    public ItemFocusTaintedBlast() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusTaintedBlast");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusTaintedBlast");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaint_depth");
        this.ornIcon = ir.registerIcon("taintedmagic:ItemFocusTaintedBlast_orn");
    }

    @Override
    public IIcon getFocusDepthLayerIcon(ItemStack stack) {
        return this.depthIcon;
    }

    @Override
    public IIcon getOrnament(ItemStack stack) {
        return this.ornIcon;
    }

    @Override
    public String getSortingHelper(ItemStack stack) {
        return "SHOCKWAVE" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(ItemStack stack) {
        return 13107455;
    }

    @Override
    public AspectList getVisCost(ItemStack stack) {
        AspectList list = costBase.copy();
        if (TaintedMagicHelper.hasFocusUpgrade(stack, FocusUpgradeType.enlarge)) {
            int focusUpgradeLevel = TaintedMagicHelper.getFocusLevelUpgrade(stack, FocusUpgradeType.enlarge);
            for (int i = 0; i < focusUpgradeLevel; i++) list.add(costEnlarge);
        }

        return list;
    }

    @Override
    public int getActivationCooldown(ItemStack stack) {
        return 6000;
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        super.addInformation(stack, player, list, b);

        list.add(
                EnumChatFormatting.BLUE + "+"
                        + (this.isUpgradedWith(stack, FocusUpgradeType.enlarge)
                                ? Integer.toString(10 + this.getUpgradeLevel(stack, FocusUpgradeType.enlarge) * 2)
                                : "10")
                        + " "
                        + StatCollector.translateToLocal("text.radius"));
        if (ConfigHandler.taintStormStaffMultiple != 1) list = TaintedMagicHelper.addTooltipDamageAndStaffMultiplier(
                list,
                ConfigHandler.taintedBlastBaseDamage,
                stack,
                ConfigHandler.taintedBlastStaffMultiple);
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop) {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false)) {
            final double radius = 10D + 2 * TaintedMagicHelper.getFocusLevelUpgrade(stack, FocusUpgradeType.enlarge);
            final float damage = TaintedMagicHelper.getFocusDamageWithPotency(
                    stack,
                    ConfigHandler.taintedBlastBaseDamage,
                    ConfigHandler.taintedBlastStaffMultiple);
            final float reclining = ConfigHandler.taintedBlastReclining / 5;

            List<EntityLivingBase> ents = world.getEntitiesWithinAABB(
                    EntityLivingBase.class,
                    AxisAlignedBB.getBoundingBox(
                            player.posX,
                            player.posY,
                            player.posZ,
                            player.posX + 1,
                            player.posY + 1,
                            player.posZ + 1).expand(radius, radius, radius));
            if (ents != null && ents.size() > 0) {
                for (EntityLivingBase e : ents) {
                    if (e != player && e.isEntityAlive() && !e.isEntityInvulnerable()) {
                        e.attackEntityFrom(DamageSource.magic, damage);
                        e.addVelocity(reclining, reclining, reclining);
                    }
                }
            }
            world.playSoundAtEntity(player, "taintedmagic:shockwave", 5.0F, (float) Math.random());
            TaintedMagic.proxy.spawnShockwaveParticles(player.worldObj);
            return stack;
        }
        return null;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
        switch (rank) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.enlarge,
                        FocusUpgradeType.potency };
            default:
                return null;
        }
    }
}

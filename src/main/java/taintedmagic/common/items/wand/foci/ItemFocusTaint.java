package taintedmagic.common.items.wand.foci;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityTaintBubble;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusTaint extends ItemFocusBasic {

    public static FocusUpgradeType tainturgy = new FocusUpgradeType(
            55,
            new ResourceLocation("taintedmagic", "textures/foci/IconTainturgy.png"),
            "focus.upgrade.tainturgy.name",
            "focus.upgrade.tainturgy.text",
            new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));
    public static FocusUpgradeType corrosive = new FocusUpgradeType(
            56,
            new ResourceLocation("taintedmagic", "textures/foci/IconCorrosive.png"),
            "focus.upgrade.corrosive.name",
            "focus.upgrade.corrosive.text",
            new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));
    IIcon depthIcon = null;

    private final AspectList costBase = new AspectList().add(Aspect.AIR, 80).add(Aspect.WATER, 80);
    private final AspectList costTainturgy = new AspectList().add(Aspect.ORDER, 60);
    private final AspectList costCorrosive = new AspectList().add(Aspect.ENTROPY, 60).add(Aspect.FIRE, 60);
    private final AspectList costEnlarge = new AspectList().add(Aspect.AIR, 60).add(Aspect.WATER, 60);

    public ItemFocusTaint() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusTaint");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusTaint");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaint_depth");
    }

    @Override
    public IIcon getFocusDepthLayerIcon(ItemStack stack) {
        return this.depthIcon;
    }

    @Override
    public String getSortingHelper(ItemStack stack) {
        return "TAINT" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(ItemStack stack) {
        return 8073200;
    }

    @Override
    public AspectList getVisCost(ItemStack stack) {
        AspectList list = costBase.copy();
        if (TaintedMagicHelper.hasFocusUpgrade(stack, tainturgy)) {
            list.add(costTainturgy);
        }
        if (TaintedMagicHelper.hasFocusUpgrade(stack, corrosive)) {
            list.add(costCorrosive);
        }
        if (TaintedMagicHelper.hasFocusUpgrade(stack, FocusUpgradeType.enlarge)) {
            list.add(costEnlarge);
        }

        return list;
    }

    @Override
    public int getActivationCooldown(ItemStack stack) {
        return -1;
    }

    @Override
    public boolean isVisCostPerTick(ItemStack stack) {
        return true;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.CHARGE;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop) {
        player.setItemInUse(stack, Integer.MAX_VALUE);
        WandManager.setCooldown(player, -1);
        return stack;
    }

    @Override
    public void onUsingFocusTick(ItemStack stack, EntityPlayer player, int count) {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        if (!wand.consumeAllVis(stack, player, getVisCost(stack), false, false)) {
            player.stopUsingItem();
            return;
        }
        if (!player.worldObj.isRemote && (player.ticksExisted + 6) % 5 == 0) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:bubble", 0.33F, 5.0F * (float) Math.random());
            wand.consumeAllVis(stack, player, getVisCost(stack), true, false);
        }

        if (!player.worldObj.isRemote) {
            float scatter = isUpgradedWith(wand.getFocusItem(stack), FocusUpgradeType.enlarge) ? 15.0F : 8.0F;
            for (int a = 0; a < 2 + wand.getFocusPotency(stack); a++) {
                EntityTaintBubble orb = new EntityTaintBubble(
                        player.worldObj,
                        player,
                        scatter,
                        isUpgradedWith(wand.getFocusItem(stack), this.corrosive));
                orb.posX += orb.motionX;
                orb.posY += orb.motionY;
                orb.posZ += orb.motionZ;
                orb.damage = TaintedMagicHelper.getFocusDamageWithPotency(
                        stack,
                        ConfigHandler.taintStormBaseDamage,
                        ConfigHandler.taintStormStaffMultiple);
                player.worldObj.spawnEntityInWorld(orb);
                if (!TaintedMagicHelper.hasFocusUpgrade(stack, tainturgy)) {
                    Random rand = new Random();
                    int randomInt = rand.nextInt(150);
                    if (randomInt == 9) {
                        try {
                            player.addPotionEffect(new PotionEffect(Config.potionVisExhaustID, 1000, 2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (ConfigHandler.taintStormStaffMultiple != 1) {
            list = TaintedMagicHelper.addTooltipDamageAndStaffMultiplier(
                    list,
                    ConfigHandler.taintStormBaseDamage,
                    stack,
                    ConfigHandler.taintStormStaffMultiple);
        }
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
        switch (rank) {
            case 1:
            case 2:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            case 3:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency, tainturgy };
            case 4:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        FocusUpgradeType.enlarge };
            case 5:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency, corrosive };
            default:
                return null;
        }
    }
}

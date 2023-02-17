package taintedmagic.common.items.wand.foci;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityEldritchOrbAttack;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusEldritch extends ItemFocusBasic {

    public static FocusUpgradeType sanity = new FocusUpgradeType(
            57,
            new ResourceLocation("taintedmagic", "textures/foci/IconSanity.png"),
            "focus.upgrade.sanity.name",
            "focus.upgrade.sanity.text",
            new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));
    IIcon depthIcon = null;

    private final AspectList costBase = new AspectList().add(Aspect.ENTROPY, 250).add(Aspect.AIR, 150)
            .add(Aspect.FIRE, 250);
    private final AspectList costSane = new AspectList().add(Aspect.ORDER, 100).add(Aspect.WATER, 80);
    private final AspectList costCorrosive = new AspectList().add(Aspect.ENTROPY, 150).add(Aspect.FIRE, 120);

    public ItemFocusEldritch() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusEldritch");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (ConfigHandler.eldritchStaffMultiple != 1) {
            list = TaintedMagicHelper.addTooltipDamageAndStaffMultiplier(
                    list,
                    ConfigHandler.eldritchBaseDamage,
                    stack,
                    ConfigHandler.eldritchStaffMultiple);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusEldritch");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusEldritch_depth");
    }

    public IIcon getFocusDepthLayerIcon(ItemStack stack) {
        return this.depthIcon;
    }

    public String getSortingHelper(ItemStack stack) {
        return "ELDRITCH" + super.getSortingHelper(stack);
    }

    public int getFocusColor(ItemStack stack) {
        return 0x000018;
    }

    public AspectList getVisCost(ItemStack stack) {
        AspectList list = costBase.copy();
        if (TaintedMagicHelper.hasFocusUpgrade(stack, ItemFocusTaint.corrosive)) {
            list.add(costCorrosive);
        }
        if (TaintedMagicHelper.hasFocusUpgrade(stack, sanity)) {
            list.add(costSane);
        }

        return list;
    }

    public int getActivationCooldown(ItemStack stack) {
        return 1000;
    }

    public boolean isVisCostPerTick(ItemStack stack) {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop) {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (!world.isRemote && wand != null && wand.consumeAllVis(stack, player, getVisCost(stack), true, false)) {
            EntityEldritchOrbAttack orb = new EntityEldritchOrbAttack(
                    world,
                    player,
                    isUpgradedWith(wand.getFocusItem(stack), ItemFocusTaint.corrosive));

            orb.dmg = TaintedMagicHelper.getFocusDamageWithPotency(
                    stack,
                    ConfigHandler.eldritchBaseDamage,
                    ConfigHandler.eldritchStaffMultiple);
            world.spawnEntityInWorld(orb);

            if (!isUpgradedWith(wand.getFocusItem(stack), sanity)) {
                Random rand = new Random();
                int randomInt = rand.nextInt(10);
                if (randomInt == 5) {
                    Thaumcraft.addStickyWarpToPlayer(player, 1);
                }
            }
            world.playSoundAtEntity(player, "thaumcraft:egattack", 0.4F, 1.0F + world.rand.nextFloat() * 0.1F);
        }
        player.swingItem();
        return stack;
    }

    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
        switch (rank) {
            case 1:
            case 2:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            case 3:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency,
                        ItemFocusTaint.corrosive };
            case 4:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            case 5:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency, sanity };
            default:
                return null;
        }
    }
}

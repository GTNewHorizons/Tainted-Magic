package taintedmagic.common.items.wand.foci;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusMageMace extends ItemFocusBasic {

    public IIcon depthIcon = null;
    public IIcon ornIcon = null;

    public static FocusUpgradeType bloodlust = new FocusUpgradeType(
            58,
            new ResourceLocation("taintedmagic", "textures/foci/IconBloodlust.png"),
            "focus.upgrade.bloodlust.name",
            "focus.upgrade.bloodlust.text",
            new AspectList().add(Aspect.WEAPON, 1).add(Aspect.HEAL, 1));
    public final AspectList mageMaceCostBase = new AspectList().add(Aspect.EARTH, 220).add(Aspect.ENTROPY, 220)
            .add(Aspect.ORDER, 180);
    public final AspectList mageMAceCostBloodlust = mageMaceCostBase.copy().add(Aspect.FIRE, 160);

    public ItemFocusMageMace() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusMageMace");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusMageMace");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_depth");
        this.ornIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_orn");
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
        return "MACE" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(ItemStack stack) {
        return 3289650;
    }

    @Override
    public AspectList getVisCost(ItemStack stack) {
        return this.isUpgradedWith(stack, bloodlust) ? mageMAceCostBloodlust : mageMaceCostBase;
    }

    @Override
    public int getActivationCooldown(ItemStack stack) {
        return -1;
    }

    @Override
    public boolean isVisCostPerTick(ItemStack stack) {
        return false;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack stack) {
        return WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop) {
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        super.addInformation(stack, player, list, b);
        list.add("");
        list.add(
                EnumChatFormatting.BLUE + "+"
                        + TaintedMagicHelper.getFocusDamageWithPotency(stack, ConfigHandler.magesMaceBaseDamage)
                        + " "
                        + StatCollector.translateToLocal("text.attackdamageequipped"));

        if (ConfigHandler.magesMaceStaffMultiple != 1)
            list = TaintedMagicHelper.addTooltipStaffMultiplier(list, ConfigHandler.magesMaceStaffMultiple);
        list.add("");
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
        switch (rank) {
            case 1:
            case 2:
            case 3:
            case 4:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            case 5:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency, bloodlust };
            default:
                return null;
        }

    }

    @Override
    public boolean canApplyUpgrade(ItemStack stack, EntityPlayer player, FocusUpgradeType focusUpgradeType, int rank) {
        return (!focusUpgradeType.equals(bloodlust))
                || (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "BLOODLUSTUPGRADE"));
    }
}

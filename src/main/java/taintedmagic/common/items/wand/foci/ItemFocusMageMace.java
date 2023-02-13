package taintedmagic.common.items.wand.foci;

import static taintedmagic.common.helper.TaintedMagicHelper.getFocusDamageWithPotency;

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
    public final AspectList mageMaceCostBase = new AspectList().add(Aspect.EARTH, 120).add(Aspect.ENTROPY, 120)
            .add(Aspect.ORDER, 80);
    public final AspectList mageMAceCostBloodlust = mageMaceCostBase.copy().add(Aspect.FIRE, 80);

    public ItemFocusMageMace() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusMageMace");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusMageMace");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_depth");
        this.ornIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_orn");
    }

    public IIcon getFocusDepthLayerIcon(ItemStack s) {
        return this.depthIcon;
    }

    public IIcon getOrnament(ItemStack s) {
        return this.ornIcon;
    }

    public String getSortingHelper(ItemStack s) {
        return "MACE" + super.getSortingHelper(s);
    }

    public int getFocusColor(ItemStack s) {
        return 3289650;
    }

    public AspectList getVisCost(ItemStack stack) {
        return this.isUpgradedWith(stack, bloodlust) ? mageMAceCostBloodlust : mageMaceCostBase;
    }

    public int getActivationCooldown(ItemStack s) {
        return -1;
    }

    public boolean isVisCostPerTick(ItemStack s) {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack s) {
        return WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick(ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop) {
        return s;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        super.addInformation(stack, player, list, b);
        list.add("");
        list.add(
                EnumChatFormatting.BLUE + "+"
                        + getFocusDamageWithPotency(stack, ConfigHandler.magesMaceBaseDamage)
                        + " "
                        + StatCollector.translateToLocal("text.attackdamageequipped"));
        list.add(
                EnumChatFormatting.BLUE
                        + String.format("Staff gives x%.1f Damage", ConfigHandler.magesMaceStaffMultiple));
        list.add("");
    }

    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack s, int rank) {
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

    public boolean canApplyUpgrade(ItemStack s, EntityPlayer p, FocusUpgradeType t, int rank) {
        return (!t.equals(this.bloodlust))
                || (ThaumcraftApiHelper.isResearchComplete(p.getCommandSenderName(), "BLOODLUSTUPGRADE"));
    }
}

package taintedmagic.common.items.wand.foci;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityHomingShard;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * this class is based off of ItemFocusShard.class created by <Azanor> as part of Thaumcraft 5
 */
public class ItemFocusVisShard extends ItemFocusBasic {

    private static final AspectList costBase = new AspectList().add(Aspect.FIRE, 120).add(Aspect.ENTROPY, 120)
            .add(Aspect.AIR, 120);
    private static final AspectList costPersistant = new AspectList().add(Aspect.WATER, 120);

    public static FocusUpgradeType persistant = new FocusUpgradeType(
            69,
            new ResourceLocation("taintedmagic:textures/foci/IconPersistant.png"),
            "focus.upgrade.persistant.name",
            "focus.upgrade.persistant.text",
            new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));

    public ItemFocusVisShard() {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusVisShard");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusVisShard");
    }

    @Override
    public String getSortingHelper(ItemStack stack) {
        return "SHARD" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(ItemStack stack) {
        return 10037693;
    }

    @Override
    public int getActivationCooldown(ItemStack stack) {
        return 500;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop) {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        Entity look = EntityUtils.getPointedEntity(player.worldObj, player, 0.0D, 32.0D, 1.1F);
        if (look instanceof EntityLivingBase && wand.consumeAllVis(stack, player, getVisCost(stack), true, false)) {
            if (!world.isRemote) {
                EntityHomingShard blast = new EntityHomingShard(
                        world,
                        player,
                        (EntityLivingBase) look,
                        (int) TaintedMagicHelper.getFocusDamageWithPotency(
                                stack,
                                ConfigHandler.visShardBaseDamage,
                                ConfigHandler.visShardStaffMultiple),
                        isUpgradedWith(wand.getFocusItem(stack), persistant));
                world.spawnEntityInWorld(blast);
                world.playSoundAtEntity(blast, "taintedmagic:shard", 0.3F, 1.1F + world.rand.nextFloat() * 0.1F);
            }
            player.swingItem();
        }
        return stack;
    }

    @Override
    public AspectList getVisCost(ItemStack stack) {
        AspectList list = costBase.copy();
        if (TaintedMagicHelper.hasFocusUpgrade(stack, persistant)) {
            list.add(costPersistant);
        }

        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (ConfigHandler.taintStormStaffMultiple != 1) {
            list = TaintedMagicHelper.addTooltipDamageAndStaffMultiplier(
                    list,
                    ConfigHandler.visShardBaseDamage,
                    stack,
                    ConfigHandler.visShardStaffMultiple);
        }
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack s, int r) {
        switch (r) {
            case 1:
            case 2:
            case 3:
            case 4:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
            case 5:
                return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency, persistant };
            default:
                return null;
        }
    }
}

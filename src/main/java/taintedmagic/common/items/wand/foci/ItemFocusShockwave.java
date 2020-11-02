package taintedmagic.common.items.wand.foci;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
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
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusShockwave extends ItemFocusBasic
{
    private static final AspectList COST = new AspectList().add(Aspect.AIR, 150).add(Aspect.ENTROPY, 100);

    IIcon depthIcon;

    public ItemFocusShockwave ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemFocusShockwave");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusShockwave");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusShockwave_depth");
    }

    public IIcon getFocusDepthLayerIcon (ItemStack stack)
    {
        return this.depthIcon;
    }

    public String getSortingHelper (ItemStack stack)
    {
        return "SHOCKWAVE" + super.getSortingHelper(stack);
    }

    public int getFocusColor (ItemStack stack)
    {
        return 0xB0B7C4;
    }

    public AspectList getVisCost (ItemStack stack)
    {
        return COST;
    }

    public int getActivationCooldown (ItemStack stack)
    {
        return 10000;
    }

    public boolean isVisCostPerTick (ItemStack stack)
    {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack stack)
    {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);
        list.add(" ");
        list.add(EnumChatFormatting.BLUE + "+"
                + new String(this.isUpgradedWith(stack, FocusUpgradeType.enlarge)
                        ? Integer.toString(15 + this.getUpgradeLevel(stack, FocusUpgradeType.enlarge)) : "15")
                + " " + StatCollector.translateToLocal("text.radius"));
    }

    public ItemStack onFocusRightClick (ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        int potency = wand.getFocusPotency(stack);
        int enlarge = wand.getFocusEnlarge(stack);

        if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false))
        {
            List<EntityLivingBase> ents =
                    world.getEntitiesWithinAABB(EntityLivingBase.class,
                            AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ, player.posX + 1,
                                    player.posY + 1, player.posZ + 1)
                                    .expand(15.0D + enlarge, 15.0D + enlarge, 15.0D + enlarge));

            if (ents != null && ents.size() > 0) for (EntityLivingBase entity : ents)
            {
                if (entity != player && entity.isEntityAlive() && !entity.isEntityInvulnerable())
                {
                    double dist = TaintedMagicHelper.getDistanceTo(player, entity.posX, entity.posY, entity.posZ);

                    if (dist < 7.0D) entity.attackEntityFrom(DamageSource.magic, 2.0F + potency);

                    Vector3 vel = TaintedMagicHelper.getVectorBetweenEntities(entity, player);
                    entity.addVelocity(vel.x * (5.0D + potency), 1.5D + (potency * 0.1D), vel.z * (5.0D + potency));

                    if (world.isRemote) spawnParticles(world, player, entity);
                }
            }
            world.playSoundAtEntity(player, "taintedmagic:shockwave", 5.0F, 1.5F * (float) Math.random());
            return stack;
        }
        return null;
    }

    @SideOnly (Side.CLIENT)
    public static void spawnParticles (World world, EntityPlayer player, Entity entity)
    {
        FXLightningBolt bolt = new FXLightningBolt(world, player, entity, world.rand.nextLong(), 4);

        bolt.defaultFractal();
        bolt.setType(2);
        bolt.setWidth(0.125F);
        bolt.finalizeBolt();

        for (int a = 0; a < 5; a++)
        {
            Random random = player.worldObj.rand;
            float x = (float) entity.posX + (random.nextFloat() - random.nextFloat()) * 0.6F;
            float y = (float) entity.posY + (random.nextFloat() - random.nextFloat()) * 0.6F;
            float z = (float) entity.posZ + (random.nextFloat() - random.nextFloat()) * 0.6F;
            Thaumcraft.proxy.sparkle(x, y, z, 2.0F + random.nextFloat(), 2, 0.05F + random.nextFloat() * 0.05F);
        }
    }

    public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack stack, int rank)
    {
        switch (rank)
        {
        case 1 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
        case 2 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
        case 3 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
        case 4 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
        case 5 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
        }
        return null;
    }
}

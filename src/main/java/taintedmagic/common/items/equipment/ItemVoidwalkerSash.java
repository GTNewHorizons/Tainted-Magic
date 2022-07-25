package taintedmagic.common.items.equipment;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import taintedmagic.client.handler.SashClientHandler;
import taintedmagic.client.handler.SashServerHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.ItemRunic;
import thaumcraft.common.Thaumcraft;

public class ItemVoidwalkerSash extends ItemRunic implements IRunicArmor, IWarpingGear, IBauble {

    public ItemVoidwalkerSash () {
        super(20);
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemVoidwalkerSash");
        setMaxDamage(-1);
        setMaxStackSize(1);
        setUnlocalizedName("ItemVoidwalkerSash");

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public EnumRarity getRarity (final ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    public void addInformation (final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        if (TaintedMagic.proxy.isSashEnabled(player)) {
            list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("text.sash.speed.on"));
        }
        else {
            list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("text.sash.speed.off"));
        }

        list.add(" ");
    }

    @Override
    public int getWarp (final ItemStack stack, final EntityPlayer player) {
        return 2;
    }

    @Override
    public int getRunicCharge (final ItemStack stack) {
        return 20;
    }

    @Override
    public boolean canEquip (final ItemStack stack, final EntityLivingBase entity) {
        return true;
    }

    @Override
    public boolean canUnequip (final ItemStack stack, final EntityLivingBase entity) {
        return true;
    }

    @Override
    public BaubleType getBaubleType (final ItemStack stack) {
        return BaubleType.BELT;
    }

    @Override
    public void onEquipped (final ItemStack stack, final EntityLivingBase entity) {
        Thaumcraft.instance.runicEventHandler.isDirty = true;
    }

    @Override
    public void onUnequipped (final ItemStack stack, final EntityLivingBase entity) {
        Thaumcraft.instance.runicEventHandler.isDirty = true;
    }

    @Override
    public void onWornTick (final ItemStack stack, final EntityLivingBase entity) {
    }

    @Override
    public ItemStack onItemRightClick (final ItemStack stack, final World world, final EntityPlayer player) {
        if (player.isSneaking()) {
            if (world.isRemote) {
                SashClientHandler.toggle();
            } else {
                SashServerHandler.toggleSashStatus(player);
            }
        }
        return stack;
    }

}

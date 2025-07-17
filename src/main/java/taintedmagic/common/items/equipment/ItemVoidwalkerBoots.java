package taintedmagic.common.items.equipment;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.armor.Hover;
import thaumicboots.api.IBoots;

@Optional.InterfaceList({ @Optional.Interface(iface = "thaumicboots.api.IBoots", modid = "thaumicboots"),
        @Optional.Interface(iface = "gregtech.api.hazards.IHazardProtector", modid = "gregtech_nh") })
public class ItemVoidwalkerBoots extends ItemArmor
        implements IVisDiscountGear, IWarpingGear, IRunicArmor, IRepairable, ISpecialArmor, IBoots, IHazardProtector {

    public ItemVoidwalkerBoots(ArmorMaterial m, int j, int k) {
        super(m, j, k);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemVoidwalkerBoots");
        this.setTextureName("taintedmagic:ItemVoidwalkerBoots");

        MinecraftForge.EVENT_BUS.register(this);
    }

    public String getArmorTexture(ItemStack s, Entity e, int slot, String t) {
        return "taintedmagic:textures/models/ModelVoidwalkerBoots.png";
    }

    public EnumRarity getRarity(ItemStack s) {
        return EnumRarity.epic;
    }

    @Override
    public int getRunicCharge(ItemStack s) {
        return 0;
    }

    @Override
    public int getWarp(ItemStack s, EntityPlayer p) {
        return 5;
    }

    @Override
    public int getVisDiscount(ItemStack s, EntityPlayer p, Aspect a) {
        return 5;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase e, ItemStack s, DamageSource source, double dmg,
            int slot) {
        int priority = 0;
        double ratio = this.damageReduceAmount / 90.0D;

        if (source.isMagicDamage() == true) {
            priority = 1;
            ratio = this.damageReduceAmount / 80.0D;
        } else if ((source.isFireDamage() == true) || (source.isExplosion())) {
            priority = 1;
            ratio = this.damageReduceAmount / 80.0D;
        } else if (source.isUnblockable()) {
            priority = 0;
            ratio = 0.0D;
        }
        return new ISpecialArmor.ArmorProperties(priority, ratio, s.getMaxDamage() + 1 - s.getItemDamage());
    }

    @Override
    public int getArmorDisplay(EntityPlayer p, ItemStack s, int slot) {
        return this.damageReduceAmount;
    }

    @Override
    public void damageArmor(EntityLivingBase e, ItemStack s, DamageSource source, int dmg, int slot) {
        if (source != DamageSource.fall) s.damageItem(dmg, e);
    }

    public void addInformation(ItemStack s, EntityPlayer p, List l, boolean b) {
        l.add(
                EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount")
                        + ": "
                        + getVisDiscount(s, p, null)
                        + "%");
        super.addInformation(s, p, l, b);
    }

    public void onUpdate(ItemStack s, World w, Entity e, int j, boolean k) {
        super.onUpdate(s, w, e, j, k);

        if ((!w.isRemote) && (s.isItemDamaged()) && (e.ticksExisted % 20 == 0) && ((e instanceof EntityLivingBase)))
            s.damageItem(-1, (EntityLivingBase) e);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if ((!world.isRemote) && (itemStack.getItemDamage() > 0) && (player.ticksExisted % 20 == 0))
            itemStack.damageItem(-1, player);
        if (getIntertialState(itemStack) && player.moveForward == 0
                && player.moveStrafing == 0
                && player.capabilities.isFlying) {
            player.motionX *= 0.5;
            player.motionZ *= 0.5;
        }
        float bonus = 0.20F;
        movementEffects(player, bonus, itemStack);
        if (player.fallDistance > 0.0F) player.fallDistance = 0.0F;
    }

    public void movementEffects(EntityPlayer player, float bonus, ItemStack itemStack) {
        if (player.moveForward != 0.0F || player.moveStrafing != 0.0F || player.motionY != 0.0F) {
            if (TaintedMagic.isBootsActive) {
                boolean omniMode = isOmniEnabled(itemStack);
                if (player.moveForward <= 0F && !omniMode) {
                    return;
                }
            }
            if (player.worldObj.isRemote && !player.isSneaking()) {
                if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(player.getEntityId())) {
                    Thaumcraft.instance.entityEventHandler.prevStep.put(player.getEntityId(), player.stepHeight);
                }
                player.stepHeight = 1.0F;
            }

            float speedMod = (float) getSpeedModifier(itemStack);
            if (player.onGround || player.capabilities.isFlying || player.isOnLadder()) {

                bonus += sashBuff(player);
                bonus *= speedMod;
                if (TaintedMagic.isBootsActive) {
                    applyOmniState(player, bonus, itemStack);
                } else if (player.moveForward > 0.0) {
                    player.moveFlying(
                            0.0F,
                            player.moveForward,
                            player.capabilities.isFlying ? (bonus - 0.075F) : bonus);
                }
                player.jumpMovementFactor = 0.00002F;
            } else if (Hover.getHover(player.getEntityId())) {
                player.jumpMovementFactor = 0.03F;

            } else {
                player.jumpMovementFactor = 0.05F;
            }
        }
    }

    public float sashBuff(final EntityPlayer player) {
        final ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
        if (sash != null && sash.getItem() == ItemRegistry.ItemVoidwalkerSash && sashHasSpeedBoost(sash)) {
            return 0.4F; //sash speed buff
        }
        return 0.0F;
    }
    public boolean sashHasSpeedBoost(ItemStack s) {
        if (s.stackTagCompound == null) return true;

        else return s.stackTagCompound.getBoolean("mode");
    }

    @Optional.Method(modid = "thaumicboots")
    public void applyOmniState(EntityPlayer player, float bonus, ItemStack itemStack) {
        if (player.moveForward != 0.0) {
            player.moveFlying(0.0F, player.moveForward, bonus);
        }
        if (getOmniState(itemStack)) {
            if (player.moveStrafing != 0.0) {
                player.moveFlying(player.moveStrafing, 0.0F, bonus);
            }
            boolean jumping = Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed();
            boolean sneaking = player.isSneaking();
            float rise = Math.abs((float) player.motionY);
            if (sneaking && !jumping && !player.onGround) { //no moveFlying for vertical so this extracts the internals
                rise *= bonus / rise;
                player.motionY -= rise;
            }
            if (!sneaking && jumping) {
                rise *= bonus / rise;
                player.motionY += rise;
            }
        }
    }

    @SubscribeEvent
    public void playerJumps(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ItemStack boots = player.getCurrentArmor(0);
            ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);

            if (player.inventory.armorItemInSlot(0) != null
                    && player.inventory.armorItemInSlot(0).getItem() == ItemRegistry.ItemVoidwalkerBoots) {
                player.motionY += 0.35D * (float) getJumpModifier(boots);
                if ((sash != null) && sash.getItem() == ItemRegistry.ItemVoidwalkerSash && sashHasSpeedBoost(sash)){
                    player.motionY += 0.15F * (float) getJumpModifier(boots);
                }
            }
        }
    }

    // Avoid NSM Exception when ThaumicBoots is not present.
    public double getSpeedModifier(ItemStack stack) {
        if (stack.stackTagCompound != null) {
            return stack.stackTagCompound.getDouble("speed");
        }
        return 1.0;
    }

    public double getJumpModifier(ItemStack stack) {
        if (stack.stackTagCompound != null) {
            return stack.stackTagCompound.getDouble("jump");
        }
        return 1.0;
    }

    public boolean getOmniState(ItemStack stack) {
        if (stack.stackTagCompound != null) {
            return stack.stackTagCompound.getBoolean("omni");
        }
        return false;
    }

    public boolean getIntertialState(ItemStack stack) {
        if (stack.stackTagCompound != null) {
            return stack.stackTagCompound.getBoolean("inertiacanceling");
        }
        return false;
    }

    @Override
    @Optional.Method(modid = "gregtech_nh")
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }
}

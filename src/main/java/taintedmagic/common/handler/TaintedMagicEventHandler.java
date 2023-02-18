package taintedmagic.common.handler;

import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import taintedmagic.api.IBloodlust;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.equipment.ItemCrimsonSword;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class TaintedMagicEventHandler {

    Random randy = new Random();

    @SubscribeEvent
    public void livingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;

            if ((player.ticksExisted + 6) % 20 == 0 && !player.worldObj.isRemote) {
                updateAttackDamage(player);

                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    if (player.inventory.getStackInSlot(i) != null
                            && player.inventory.getStackInSlot(i).stackTagCompound != null
                            && player.inventory.getStackInSlot(i).stackTagCompound.getBoolean("voidtouched")) {
                        ItemStack stack = player.inventory.getStackInSlot(i);
                        if (stack.isItemDamaged()) stack.damageItem(-1, (EntityLivingBase) player);
                    }
                }
            }
        }
    }

    /*
     * Used to update the attack damage for the Mage's Mace focus
     */
    private void updateAttackDamage(EntityPlayer player) {
        ItemStack stack = player.getHeldItem();
        if (stack != null && stack.getItem() instanceof ItemWandCasting) {
            final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
            boolean isMageMaceActive;
            int countOfPotency;

            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("MageMace")) {
                isMageMaceActive = stack.getTagCompound().getCompoundTag("MageMace").getBoolean("isMageMaceActive");
                countOfPotency = stack.getTagCompound().getCompoundTag("MageMace").getInteger("potency");
            } else {
                isMageMaceActive = false;
                countOfPotency = 0;
            }

            // set new damage
            if (wand.getFocus(stack) == ItemRegistry.ItemFocusMageMace
                    && (!isMageMaceActive || countOfPotency != wand.getFocusPotency(stack))) {
                int newDamage = (int) TaintedMagicHelper.getFocusDamageWithPotency(
                        stack,
                        ConfigHandler.magesMaceBaseDamage,
                        ConfigHandler.magesMaceStaffMultiple);

                setDamage(stack, newDamage, wand.getFocusPotency(stack), true);
            }
            // set base damage
            else if (isMageMaceActive && wand.getFocus(stack) != ItemRegistry.ItemFocusMageMace) {
                if (wand.getRod(stack) instanceof StaffRod) setDamage(stack, 6, 0, false);
                else setDamage(stack, 0, 0, false);
            }
        }
    }

    private void setDamage(ItemStack stack, int newDamage, int potency, boolean isActive) {

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey("MageMace")) {
            stack.stackTagCompound.getCompoundTag("MageMace").setBoolean("isMageMaceActive", isActive);
        }

        NBTTagCompound tagMageMace = stack.stackTagCompound.getCompoundTag("MageMace");
        tagMageMace.setBoolean("isMageMaceActive", true);
        tagMageMace.setInteger("countOfPotency", potency);

        // AttributeModifier and damage
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList tags = new NBTTagList();
        tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
        AttributeModifier modifier = new AttributeModifier(uuid, "Weapon modifier", newDamage, 0);

        tag.setString("Name", modifier.getName());
        tag.setDouble("Amount", modifier.getAmount());
        tag.setInteger("Operation", modifier.getOperation());
        tag.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
        tag.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());

        tags.appendTag(tag);
        stack.stackTagCompound.setTag("AttributeModifiers", tags);
    }

    @SubscribeEvent
    public void entityAttacked(LivingAttackEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting) {
                final ItemStack stack = player.getHeldItem();
                final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
                final ItemStack cap = wand.getFocusItem(stack);

                if (wand.getFocus(stack) instanceof ItemFocusMageMace) {
                    final ItemFocusMageMace itemFocusMageMace = (ItemFocusMageMace) wand.getFocus(stack);
                    final boolean isEnoughVis = wand
                            .consumeAllVis(stack, player, itemFocusMageMace.getVisCost(cap), true, false);

                    if (!isEnoughVis) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    // End

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (UpdateHandler.show) event.player.addChatMessage(new ChatComponentText(UpdateHandler.updateStatus));
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        if (event.crafting == new ItemStack(ItemRegistry.ItemWandCap, 1, 0)) {
            EntityItem ent = event.player.entityDropItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), 0);
            ent.motionY += randy.nextFloat() * 0.05F;
            ent.motionX += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
            ent.motionZ += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
        }
        if (event.crafting == new ItemStack(ItemRegistry.ItemWandRod, 1, 0)) {
            EntityItem ent = event.player.entityDropItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), 0);
            ent.motionY += randy.nextFloat() * 0.05F;
            ent.motionX += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
            ent.motionZ += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
        }
        if (event.crafting.getItem() == ItemRegistry.ItemMaterial && event.crafting.getItemDamage() == 5)
            giveResearch(event.player);
    }

    public void giveResearch(EntityPlayer player) {
        if ((!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION")
                && (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATIONSHARD")
                        && !player.worldObj.isRemote))) {
            Thaumcraft.proxy.getResearchManager().completeResearch(player, "CREATION");
            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("CREATION"), (EntityPlayerMP) player);
            player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("text.1")));
            player.playSound("thaumcraft:wind", 1.0F, 5.0F);
            try {
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, 80, 0));
                player.addPotionEffect(new PotionEffect(Config.potionBlurredID, 200, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "OUTERREV")
                    && (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION")
                            && !player.worldObj.isRemote))) {
                Thaumcraft.proxy.getResearchManager().completeResearch(player, "OUTERREV");
                PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("OUTERREV"), (EntityPlayerMP) player);
            }
        }
    }

    @SubscribeEvent
    public void livingDrop(LivingDropsEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) event.source.getEntity();
            if (p.getHeldItem() != null && (p.getHeldItem().getItem() instanceof IBloodlust
                    || p.getHeldItem().getItem() instanceof ItemCrimsonSword)) {
                Random r = new Random();
                ItemStack drops = new ItemStack(ItemRegistry.ItemMaterial, r.nextInt(5), 7);
                addDropItem(event, drops);
            } else if (p.getHeldItem() != null && p.getHeldItem().getItem() instanceof ItemWandCasting
                    && ((ItemWandCasting) p.getHeldItem().getItem()).getFocus(p.getHeldItem()) != null
                    && ((ItemWandCasting) p.getHeldItem().getItem()).getFocus(p.getHeldItem()).isUpgradedWith(
                            ((ItemWandCasting) p.getHeldItem().getItem()).getFocusItem(p.getHeldItem()),
                            ItemFocusMageMace.bloodlust)) {
                                Random r = new Random();
                                ItemStack drops = new ItemStack(ItemRegistry.ItemMaterial, r.nextInt(5), 7);
                                addDropItem(event, drops);
                            }
        }
    }

    public void addDropItem(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(
                event.entityLiving.worldObj,
                event.entityLiving.posX,
                event.entityLiving.posY,
                event.entityLiving.posZ,
                drop);
        entityitem.delayBeforeCanPickup = 10;
        event.drops.add(entityitem);
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        if (event.itemStack.getItem() instanceof ItemFocusMageMace) {
            if (event.toolTip.contains(StatCollector.translateToLocal("item.Focus.cost1"))) {
                event.toolTip.remove(StatCollector.translateToLocal("item.Focus.cost1"));
                event.toolTip.add(1, StatCollector.translateToLocal("item.Focus.cost3"));
            }
        }
        if (event.itemStack.getItem() instanceof IBloodlust)
            event.toolTip.add("\u00A74" + StatCollector.translateToLocal("text.bloodlust"));
        if (event.itemStack.getItem() instanceof ItemCrimsonSword)
            event.toolTip.add("\u00A74" + StatCollector.translateToLocal("text.bloodlust"));
        if ((event.itemStack.getItem() instanceof ItemFocusMageMace && ((ItemFocusBasic) event.itemStack.getItem())
                .isUpgradedWith(event.itemStack, ItemFocusMageMace.bloodlust))
                || (event.itemStack.getItem() instanceof ItemWandCasting
                        && ((ItemWandCasting) event.itemStack.getItem()).getFocus(event.itemStack) != null
                        && ((ItemWandCasting) event.itemStack.getItem())
                                .getFocus(event.itemStack) instanceof ItemFocusMageMace
                        && ((ItemWandCasting) event.itemStack.getItem()).getFocus(event.itemStack).isUpgradedWith(
                                ((ItemWandCasting) event.itemStack.getItem()).getFocusItem(event.itemStack),
                                ItemFocusMageMace.bloodlust)))
            event.toolTip.add("\u00A74" + StatCollector.translateToLocal("text.bloodlust"));

        if (event.itemStack.stackTagCompound != null && event.itemStack.stackTagCompound.getBoolean("voidtouched"))
            event.toolTip.add("\u00A75" + StatCollector.translateToLocal("text.voidtouched"));
    }
}

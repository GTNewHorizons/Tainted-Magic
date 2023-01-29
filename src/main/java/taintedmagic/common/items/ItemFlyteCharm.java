package taintedmagic.common.items;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import org.lwjgl.opengl.GL11;

import taintedmagic.api.IRenderInventoryItem;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlyteCharm extends Item implements IWarpingGear, IRenderInventoryItem {

    // Vis costs per tick
    private static final AspectList COST_FLIGHT = new AspectList().add(Aspect.AIR, 15);
    private static final AspectList COST_GLIDE = new AspectList().add(Aspect.AIR, 5);

    // Magic circle texture
    private static final ResourceLocation MAGIC_CIRCLE = new ResourceLocation("taintedmagic:textures/misc/circle.png");

    // Stores flying players
    public static final Set<String> FLIGHT_MANAGER = new HashSet<>();

    public ItemFlyteCharm() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemFlyteCharm");
        setMaxStackSize(1);
        setTextureName("taintedmagic:ItemFlyteCharm");

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return TaintedMagic.rarityCreation;
    }

    @SubscribeEvent
    public void updateFlight(final LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) event.entityLiving;
            final String entry = getPlayerEntry(player, player.worldObj.isRemote);

            if (FLIGHT_MANAGER.contains(entry)) {
                if (canFly(player)) {
                    player.capabilities.allowFlying = true; // Allow flight

                    final boolean isFlying = player.capabilities.isFlying;
                    // use vis
                    TaintedMagicHelper.consumeVisFromInventory(player, COST_FLIGHT, isFlying);

                    if (!isFlying) {
                        // Glide
                        if (player.isSneaking() && !player.onGround
                                && player.fallDistance > 0.5F
                                && TaintedMagicHelper.consumeVisFromInventory(player, COST_GLIDE, true)) {
                            final double speed = 0.1D;
                            player.motionY = -speed;
                            player.motionX += Math.cos(Math.toRadians(player.rotationYawHead + 90)) * speed;
                            player.motionZ += Math.sin(Math.toRadians(player.rotationYawHead + 90)) * speed;
                        }
                    }
                } else {
                    if (!player.capabilities.isCreativeMode) {
                        player.capabilities.allowFlying = false;
                        player.capabilities.isFlying = false;
                    }
                    FLIGHT_MANAGER.remove(entry);
                }
            } else if (canFly(player)) {
                FLIGHT_MANAGER.add(entry);
                player.capabilities.allowFlying = true;
            }
        }
    }

    private boolean canFly(final EntityPlayer player) {
        final boolean hasVis = TaintedMagicHelper.consumeVisFromInventory(player, COST_FLIGHT, false);
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            final ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemFlyteCharm) return hasVis;
        }
        return false;
    }

    private static String getPlayerEntry(final EntityPlayer player, final boolean remote) {
        return player.getUniqueID().toString() + ":" + remote;
    }

    @SubscribeEvent
    private void onLogout(final PlayerEvent.PlayerLoggedOutEvent event) {
        FLIGHT_MANAGER.remove(getPlayerEntry(event.player, true));
        FLIGHT_MANAGER.remove(getPlayerEntry(event.player, false));
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 5;
    }

    // Render magic circle
    @Override
    public void render(final EntityPlayer player, final ItemStack stack, final float partialTicks) {
        final Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glTranslated(
                0,
                (player != Minecraft.getMinecraft().thePlayer ? 1.62F : 0F) - player.getDefaultEyeHeight()
                        + (player.isSneaking() ? 0.0625 : 0),
                0);

        GL11.glRotatef(45, -1, 0, -1);
        GL11.glTranslatef(0.0F, -0.5F, -0.2F);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glColor4f(1F, 1F, 1F, 0.8F);

        GL11.glScalef(0.4F, 0.4F, 0.4F);
        GL11.glRotatef(player.ticksExisted + partialTicks, 0F, 1F, 0F);

        UtilsFX.bindTexture(MAGIC_CIRCLE);

        t.startDrawingQuads();
        t.addVertexWithUV(-1, 0, -1, 0, 0);
        t.addVertexWithUV(-1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(1, 0, -1, 1, 0);
        t.draw();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }
}

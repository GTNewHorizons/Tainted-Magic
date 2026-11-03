package taintedmagic.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.dashboard.api.HudBounds;
import com.gtnewhorizons.dashboard.api.HudElement;
import com.gtnewhorizons.dashboard.api.HudRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.items.tools.ItemThaumicDisassembler;

/**
 * Lets players move, resize and hide the Katana charge runes and the Thaumic Disassembler mode text in Dashboard's HUD
 * editor. The only class using Dashboard, so only call it while Dashboard is loaded. Both are drawn shrunk, at spots
 * worked out from the screen center in their shrunk coordinates, so the default bounds copy that math.
 */
@SideOnly(Side.CLIENT)
public final class DashboardHud {

    private static final float KATANA_SCALE = 0.315f;
    private static final int RUNE_SIZE = 16;
    private static final int RUNE_COUNT = 16;
    private static final float DISASSEMBLER_SCALE = 0.8f;
    private static final int DISASSEMBLER_RAISE = 15;
    private static final ResourceLocation RUNES = new ResourceLocation("thaumcraft", "textures/misc/script.png");

    private static final HudElement KATANA_CHARGE = new HudElement(
            "taintedmagic:katana_charge",
            DashboardHud::getKatanaBounds);

    private static final HudElement DISASSEMBLER_MODE = new HudElement(
            "taintedmagic:disassembler_mode",
            DashboardHud::getDisassemblerBounds);

    private DashboardHud() {}

    public static void register() {
        KATANA_CHARGE.setPreview(
                (screenWidth, screenHeight) -> HudRegistry
                        .render(KATANA_CHARGE, () -> drawKatanaPreview(getKatanaBounds(screenWidth, screenHeight))));
        DISASSEMBLER_MODE.setPreview(
                (screenWidth, screenHeight) -> HudRegistry.render(
                        DISASSEMBLER_MODE,
                        () -> drawDisassemblerPreview(getDisassemblerBounds(screenWidth, screenHeight))));
        HudRegistry.register(KATANA_CHARGE);
        HudRegistry.register(DISASSEMBLER_MODE);
    }

    // False means the player hid the element, so skip drawing it and do not call endRender()

    public static boolean beginKatanaCharge() {
        return HudRegistry.beginRender(KATANA_CHARGE);
    }

    public static boolean beginDisassemblerMode() {
        return HudRegistry.beginRender(DISASSEMBLER_MODE);
    }

    public static void endRender() {
        HudRegistry.endRender();
    }

    /** A row of 16 runes, lower in creative, where the missing hearts leave room. */
    private static HudBounds getKatanaBounds(int screenWidth, int screenHeight) {
        int x = Math.round((screenWidth / 2 + 725) * KATANA_SCALE);
        int y = Math.round((screenHeight / 2 + (isCreative() ? 805 : 755)) * KATANA_SCALE);
        return new HudBounds(
                x,
                y,
                Math.round(RUNE_COUNT * RUNE_SIZE * KATANA_SCALE),
                Math.round(RUNE_SIZE * KATANA_SCALE) + 1);
    }

    private static HudBounds getDisassemblerBounds(int screenWidth, int screenHeight) {
        int x = Math.round((screenWidth / 2 + 100) * DISASSEMBLER_SCALE);
        int y = Math.round((screenHeight / 2 + (isCreative() ? 230 : 212)) * DISASSEMBLER_SCALE) - DISASSEMBLER_RAISE;
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(getDisassemblerText());
        return new HudBounds(x, y, Math.round(width * DISASSEMBLER_SCALE), Math.round(9 * DISASSEMBLER_SCALE));
    }

    private static boolean isCreative() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        return player != null && player.capabilities.isCreativeMode;
    }

    /** Like the real text, from the held Disassembler, or a sample. */
    private static String getDisassemblerText() {
        String mode = StatCollector.translateToLocal("text.mode") + ": ";
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack held = player != null ? player.getCurrentEquippedItem() : null;
        if (held != null && held.getItem() instanceof ItemThaumicDisassembler) {
            return mode + ItemThaumicDisassembler.getModeName(held)
                    + " ("
                    + ItemThaumicDisassembler.getEfficiency(held)
                    + ")";
        }
        return mode + "Default (3)";
    }

    /** Like the real runes, the first half charged. */
    private static void drawKatanaPreview(HudBounds bounds) {
        Minecraft.getMinecraft().renderEngine.bindTexture(RUNES);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        GL11.glTranslatef(bounds.x, bounds.y, 0);
        GL11.glScalef(KATANA_SCALE, KATANA_SCALE, 1);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (int rune = 0; rune < RUNE_COUNT; rune++) {
            if (rune < RUNE_COUNT / 2) {
                tessellator.setColorRGBA_F(1 - rune / 16f, rune / 16f, 0, 0.9f);
            } else {
                tessellator.setColorRGBA_F(0.8f, 0.7f, 0.4f, 0.7f);
            }
            float u = rune / 16f;
            int x = rune * RUNE_SIZE;
            tessellator.addVertexWithUV(x, RUNE_SIZE, 0, u, 1);
            tessellator.addVertexWithUV(x + RUNE_SIZE, RUNE_SIZE, 0, u + 1 / 16f, 1);
            tessellator.addVertexWithUV(x + RUNE_SIZE, 0, 0, u + 1 / 16f, 0);
            tessellator.addVertexWithUV(x, 0, 0, u, 0);
        }
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void drawDisassemblerPreview(HudBounds bounds) {
        GL11.glPushMatrix();
        GL11.glTranslatef(bounds.x, bounds.y, 0);
        GL11.glScalef(DISASSEMBLER_SCALE, DISASSEMBLER_SCALE, 1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getDisassemblerText(), 0, 0, 0xFFFFFF);
        GL11.glPopMatrix();
    }
}

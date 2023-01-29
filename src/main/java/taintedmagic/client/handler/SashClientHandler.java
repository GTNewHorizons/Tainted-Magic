package taintedmagic.client.handler;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class SashClientHandler {

    private static boolean sashEnabled = true;

    public static void toggle() {
        sashEnabled = !sashEnabled;
        if (sashEnabled) {
            HUDHandler.displayString(
                    EnumChatFormatting.GREEN + StatCollector.translateToLocal("text.sash.speed.on"),
                    300,
                    false);
        } else {
            HUDHandler.displayString(
                    EnumChatFormatting.RED + StatCollector.translateToLocal("text.sash.speed.off"),
                    300,
                    false);
        }
    }

    public static boolean isEnabled() {
        return sashEnabled;
    }
}

package taintedmagic.common.network;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import taintedmagic.common.TaintedMagic;

public class PacketHandler {

    static Marker SECURITY_MARKER = MarkerManager.getMarker("SuspiciousPackets");

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE
            .newSimpleChannel(TaintedMagic.MOD_ID.toLowerCase());

    public static void initPackets() {
        INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketSashToggle.class, PacketSashToggle.class, 1, Side.SERVER);
    }
}

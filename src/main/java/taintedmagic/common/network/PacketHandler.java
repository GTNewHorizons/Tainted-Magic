package taintedmagic.common.network;

import taintedmagic.common.TaintedMagic;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE
            .newSimpleChannel(TaintedMagic.MOD_ID.toLowerCase());

    public static void initPackets() {
        INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketSashToggle.class, PacketSashToggle.class, 1, Side.SERVER);
    }
}

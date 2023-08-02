package taintedmagic.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import taintedmagic.common.lib.LibStrings;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE
            .newSimpleChannel(LibStrings.MODID.toLowerCase());

    public static void init() {
        int i = 0;
        INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, i++, Side.SERVER);
    }
}

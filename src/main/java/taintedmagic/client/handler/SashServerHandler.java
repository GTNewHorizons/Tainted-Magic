package taintedmagic.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import taintedmagic.common.TaintedMagic;

public final class SashServerHandler {

    private static final String COMPOUND = TaintedMagic.MOD_ID;
    private static final String TAG_MODE = "sash_mode";

    private static NBTTagCompound getCompound(EntityPlayer player) {
        NBTTagCompound cmp = player.getEntityData();
        if (!cmp.hasKey(COMPOUND)) cmp.setTag(COMPOUND, new NBTTagCompound());

        return cmp.getCompoundTag(COMPOUND);
    }

    public static boolean isSashEnabled(EntityPlayer player) {
        NBTTagCompound cmp = getCompound(player);
        return !cmp.hasKey(TAG_MODE) || cmp.getBoolean(TAG_MODE);
    }

    public static void toggleSashStatus(EntityPlayer player) {
        NBTTagCompound cmp = getCompound(player);
        cmp.setBoolean(TAG_MODE, !cmp.getBoolean(TAG_MODE));
    }
}

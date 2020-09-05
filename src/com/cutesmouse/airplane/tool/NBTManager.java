package com.cutesmouse.airplane.tool;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public abstract class NBTManager {
    public abstract void modify(String key, Object object);
    public String get(String key) {
        NBTTagCompound nbt = getNBT();
        if (!nbt.hasKey(key)) return null;
        return nbt.getString(key);
    }
    protected int getInt(String key) {
        String s = get(key);
        if (s == null) return 0;
        return Integer.parseInt(s);
    }
    protected double getDouble(String key) {
        String s = get(key);
        if (s == null) return 0;
        return Double.parseDouble(s);
    }
    protected abstract NBTTagCompound getNBT();
}

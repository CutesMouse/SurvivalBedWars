package com.cutesmouse.airplane.tool;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemNBTManager extends NBTManager {
    private ItemStack item;
    public ItemNBTManager(ItemStack item) {
        this.item = item;
    }

    public static boolean hasTag(ItemStack item, String id) {
        return getTag(item, id) != null;
    }
    public static String getTag(ItemStack item, String id) {
        ItemNBTManager manager = new ItemNBTManager(item);
        return manager.get(id);
    }
    public static boolean tagEqualsTo(ItemStack item, String id, String expect) {
        String tag = getTag(item, id);
        if (tag == null) return false;
        return tag.equals(expect);
    }

    @Override
    public void modify(String key, Object object) {
        net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbt = nms.getTag();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            nms.setTag(nbt);
        }
        if (object instanceof Integer) {
            nms.getTag().set(key,new NBTTagInt(((Integer) object)));
        } else {
            nms.getTag().set(key,new NBTTagString(object.toString()));
        }
        item = CraftItemStack.asBukkitCopy(nms);
    }

    @Override
    protected NBTTagCompound getNBT() {
        net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbt = nms.getTag();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            nms.setTag(nbt);
        }
        return nbt;
    }

    public ItemStack getItemStack() {
        return item;
    }
}

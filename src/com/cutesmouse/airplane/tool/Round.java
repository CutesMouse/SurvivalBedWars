package com.cutesmouse.airplane.tool;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Round {
    public static String round(double d, int pos) {
        int times = 1;
        for (int i = 0 ; i < pos ; i++) times *= 10;
        return Double.toString(Math.round(d*times)/(double) times);
    }
    public static double toDeg(double rad) {
        return rad * (180D / Math.PI);
    }
    public static double toMCPosX(double cood) {
        return -cood;
    }
    public static double toCoodX(double mc) {
        return -mc;
    }
    public static Location StringtoLoc(String s) {
        String[] split = s.split("/");
        if (split.length == 6) return new Location(Bukkit.getWorld(split[0]),Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),Double.parseDouble(split[3]),Float.parseFloat(split[4]),Float.parseFloat(split[5]));
        return new Location(Bukkit.getWorld(split[0]),Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),Double.parseDouble(split[3]));
    }
    public static String LoctoString(Location loc) {
        return loc.getWorld().getName()+"/"+loc.getX()+"/"+loc.getY()+"/"+loc.getZ()+"/"+loc.getYaw()+"/"+loc.getPitch();
    }
    public static boolean sameLocation(Location a, Location b) {
        return (a.getBlockX() == b.getBlockX() &&
                a.getBlockY() == b.getBlockY() &&
                a.getBlockZ() == b.getBlockZ() &&
                a.getWorld().equals(b.getWorld()));
    }
    public static boolean similarLocation(Location a, Location b, int dif) {
        if (dif == 0) return false;
        return (Math.abs(a.getBlockX() - b.getBlockX()) <= dif &&
                Math.abs(a.getBlockY() - b.getBlockY()) <= dif &&
                Math.abs(a.getBlockZ() - b.getBlockZ()) <= dif &&
                a.getWorld().equals(b.getWorld()));
    }
    public static double distance(double dx, double dz) {
        return Math.sqrt(dx * dx + dz * dz);
    }
    public static double toMCAngle(double angle) {
        if (angle == 0) return 90;
        if (angle == 90) return 0;
        if (angle == -90) return 180;
        if (angle == 180 || angle == -180) return 270;
        if (angle > 90) return 450 - angle;
        if (angle < -270) return -270 - angle;
        return 90 - angle;
    }
    public static String Rome(int i) {
        switch (i) {
            case 0:
                return "0";
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }
        return "?";
    }
    public static boolean isArmor(Material m) {
        switch (m) {
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return true;
        }
        return false;
    }
    public static boolean isTools(Material m) {
        switch (m) {
            case SHEARS:
            case DIAMOND_AXE:
            case DIAMOND_PICKAXE:
            case IRON_AXE:
            case IRON_PICKAXE:
            case STONE_AXE:
            case STONE_PICKAXE:
            case GOLD_AXE:
            case GOLD_PICKAXE:
            case WOOD_AXE:
            case WOOD_PICKAXE:
                return true;
        }
        return false;
    }
    public static ItemStack changeAmount(ItemStack source, int amount) {
        ItemStack item = new ItemStack(source);
        item.setAmount(amount);
        return item;
    }
    public static String formedTime(int sec) {
        int mins = sec / 60;
        sec = sec - mins*60;
        return String.format("%02d:%02d",mins,sec);
    }
}

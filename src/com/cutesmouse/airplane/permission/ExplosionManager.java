package com.cutesmouse.airplane.permission;

import org.bukkit.Location;

import java.util.ArrayList;

public class ExplosionManager {
    public static ArrayList<Location> EXPLODE_BLACKLIST = new ArrayList<>();
    public static boolean canIExplode(Location loc) {
        if (EXPLODE_BLACKLIST.contains(loc)) return false;
        return BreakingManager.canIBreak(null,loc);
    }
}

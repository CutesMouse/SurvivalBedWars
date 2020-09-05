package com.cutesmouse.airplane.permission;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import org.bukkit.entity.Player;

public class CanWarpSurvivalManager {
    public static boolean canIWarpSurvival(Player p) {
        Team t = Team.getEntry(p.getName());
        if (t == null) return false;
        if (!t.T_canWarpSurvival()) return false;
        return BedWars.INSTANCE.canWarpSurvivalWorld();
    }
}

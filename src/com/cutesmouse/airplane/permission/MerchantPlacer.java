package com.cutesmouse.airplane.permission;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.tool.Round;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MerchantPlacer {
    public static boolean canIPlaceMerchant(Player p, Location pos) {
        /*
        1. 放置在距離重生點10格內
        2. 不能距離床5格內
         */
        Team placer = Team.getEntry(p.getName());
        if (placer == null) return false;
        if (!pos.getWorld().getName().equals(BedWars.DEFAULT.getName())) {
            p.sendMessage("§c不符合放置條件 - 需在床戰世界內放置");
            return true;
        }
        //距離重生點十格內
        if (!Round.similarLocation(placer.getSpawnpoint(),pos,10)) {
            p.sendMessage("§c不符合放置條件 - 距離重生點十格內");
            return false;
        }
        //不距離床5格內
        if (Round.similarLocation(placer.getBedLocation(),pos,5) || Round.similarLocation(placer.getSecondBedBlock(),pos,5)) {
            p.sendMessage("§c不符合放置條件 - 距離床五格外");
            return false;
        }
        return true;
    }
}

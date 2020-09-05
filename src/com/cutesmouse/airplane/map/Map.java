package com.cutesmouse.airplane.map;

import com.cutesmouse.airplane.Team;
import org.bukkit.Location;

public interface Map {
    String getName();
    Location getDefaultLocation();
    Location getBedHead(Team t);
    Location getBedBack(Team t);
    Location getPortalBlock(Team t);
    Location getSpawnPoint(Team t);
    Location getHomeGenerator(Team t);
    Location[] getDiamondGenerators();
    Location[] getEmeraldGenerators();
    int ownTeamProtectionRadius();
    int maxX();
    int maxY();
    int maxZ();
    int minX();
    int minY();
    int minZ();
}

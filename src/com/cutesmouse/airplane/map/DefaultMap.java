package com.cutesmouse.airplane.map;

import com.cutesmouse.airplane.Team;
import org.bukkit.Location;

import static com.cutesmouse.airplane.BedWars.DEFAULT;

public class DefaultMap implements Map {
    @Override
    public String getName() {
        return "古老遺跡";
    }

    @Override
    public Location getDefaultLocation() {
        return new Location(DEFAULT,0.5,151,0.5);
    }

    @Override
    public Location getBedHead(Team t) {
        switch (t.getId()) {
            case 1:
                return new Location(DEFAULT,0.5,101,76.5);
            case 2:
                return new Location(DEFAULT,-74.5,101,0.5);
            case 3:
                return new Location(DEFAULT,0.5,101,-74.5);
            case 4:
                return new Location(DEFAULT,73.5,101,0.5);
        }
        return null;
    }

    @Override
    public Location getBedBack(Team t) {
        switch (t.getId()) {
            case 1:
                return new Location(DEFAULT,0.5,101,77.5);
            case 2:
                return new Location(DEFAULT, -75.5, 101, 0.5);
            case 3:
                return new Location(DEFAULT,0.5,101,-75.5);
            case 4:
                return new Location(DEFAULT,74.5,101,0.5);
        }
        return null;
    }

    @Override
    public Location getPortalBlock(Team t) {
        switch (t.getId()) {
            case 1:
                return new Location(DEFAULT,-3,100,86);
            case 2:
                return new Location(DEFAULT,-85,100,-3);
            case 3:
                return new Location(DEFAULT,3,100,-85);
            case 4:
                return new Location(DEFAULT,83,100,3);
        }
        return null;
    }

    @Override
    public Location getSpawnPoint(Team t) {
        switch (t.getId()) {
            case 1:
                return new Location(DEFAULT,0.5,101,88.5,180,0);
            case 2:
                return new Location(DEFAULT,-86.5,101,0.5,-90,0);
            case 3:
                return new Location(DEFAULT,0.5,101,-86.5,0,0);
            case 4:
                return new Location(DEFAULT,85.5,101,0.5,90,0);
        }
        return null;
    }

    @Override
    public Location getHomeGenerator(Team t) {
        Location spawn = getSpawnPoint(t);
        double x = spawn.getX();
        double y = spawn.getY() - 0.3;
        double z = spawn.getZ();
        float pitch = spawn.getPitch();
        float yaw = spawn.getYaw();
        if (x > 40) return new Location(spawn.getWorld(),x+3,y,z,pitch,yaw);
        if (x < -40) return new Location(spawn.getWorld(),x-3,y,z,pitch,yaw);
        if (z > 40) return new Location(spawn.getWorld(),x,y,z+3,pitch,yaw);
        if (z < -40) return new Location(spawn.getWorld(),x,y,z-3,pitch,yaw);
        return spawn;
    }

    @Override
    public Location[] getDiamondGenerators() {
        return new Location[]{
                new Location(DEFAULT,-35.5,103.1,36.5),
                new Location(DEFAULT,-35.5,103.1,-35.5),
                new Location(DEFAULT,35.5,103.1,-35.5),
                new Location(DEFAULT,35.5,103.1,36.5)};
    }

    @Override
    public Location[] getEmeraldGenerators() {
        return new Location[]{
                new Location(DEFAULT,0.5,102.1,0.5),
                new Location(DEFAULT,0.5,107.1,0.5)};
    }

    @Override
    public int ownTeamProtectionRadius() {
        return 6;
    }

    @Override
    public int maxX() {
        return 130;
    }

    @Override
    public int maxY() {
        return 130;
    }

    @Override
    public int maxZ() {
        return 130;
    }

    @Override
    public int minX() {
        return -130;
    }

    @Override
    public int minY() {
        return 75;
    }

    @Override
    public int minZ() {
        return -130;
    }
}

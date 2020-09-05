package com.cutesmouse.airplane.map;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.tool.Round;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigMap implements Map {
    public static ConfigMap Get(String name) {
        return new ConfigMap(name);
    }
    public static File MAP_FOLDER;
    private String name;
    private boolean loaded;
    private YamlConfiguration c;
    private File conF;
    private ConfigMap(String name) {
        this.name = name;
        conF = new File(MAP_FOLDER, name + ".yml");
        if (!conF.exists()) {
            loaded = false;
            return;
        }
        c = YamlConfiguration.loadConfiguration(conF);
        loaded = true;
    }
    public boolean loaded() {
        return loaded;
    }

    @Override
    public String getName() {
        return c.getString("DisplayName");
    }

    @Override
    public Location getDefaultLocation() {
        return Round.StringtoLoc(c.getString("DefaultLocation"));
    }

    @Override
    public Location getBedHead(Team t) {
        return Round.StringtoLoc(c.getString("Teams."+t.getId()+".BedHead"));
    }

    @Override
    public Location getBedBack(Team t) {
        return Round.StringtoLoc(c.getString("Teams."+t.getId()+".BedBack"));
    }

    @Override
    public Location getPortalBlock(Team t) {
        return Round.StringtoLoc(c.getString("Teams."+t.getId()+".PortalBlock"));
    }

    @Override
    public Location getSpawnPoint(Team t) {
        return Round.StringtoLoc(c.getString("Teams."+t.getId()+".SpawnPoint"));
    }

    @Override
    public Location getHomeGenerator(Team t) {
        return Round.StringtoLoc(c.getString("Teams."+t.getId()+".Generator"));
    }

    @Override
    public Location[] getDiamondGenerators() {
        return c.getStringList("DiamondGenerators").stream().map(Round::StringtoLoc).toArray(Location[]::new);
    }

    @Override
    public Location[] getEmeraldGenerators() {
        return c.getStringList("EmeraldGenerators").stream().map(Round::StringtoLoc).toArray(Location[]::new);
    }

    @Override
    public int ownTeamProtectionRadius() {
        return Integer.parseInt(c.getString("TeamProtectionDistance"));
    }

    @Override
    public int maxX() {
        return Integer.parseInt(c.getString("maxX"));
    }

    @Override
    public int maxY() {
        return Integer.parseInt(c.getString("maxY"));
    }

    @Override
    public int maxZ() {
        return Integer.parseInt(c.getString("maxZ"));
    }

    @Override
    public int minX() {
        return Integer.parseInt(c.getString("minX"));
    }

    @Override
    public int minY() {
        return Integer.parseInt(c.getString("minY"));
    }

    @Override
    public int minZ() {
        return Integer.parseInt(c.getString("minZ"));
    }
}

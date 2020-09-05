package com.cutesmouse.airplane;

import com.cutesmouse.airplane.commands.Home;
import com.cutesmouse.airplane.generator.HomeGenerator;
import com.cutesmouse.airplane.tool.Round;
import com.cutesmouse.airplane.upgrades.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nullable;
import java.util.*;

public class Team {
    public static Team TEAM01;
    public static Team TEAM02;
    public static Team TEAM03;
    public static Team TEAM04;
    public static Team[] TEAMS;
    public static void loadTeams() {
        TEAM01 = new Team(1,BedWars.T1_SURVIVAL,"§c");
        TEAM02 = new Team(2,BedWars.T2_SURVIVAL,"§e");
        TEAM03 = new Team(3,BedWars.T3_SURVIVAL,"§b");
        TEAM04 = new Team(4,BedWars.T4_SURVIVAL,"§a");
        TEAMS = new Team[]{TEAM01,TEAM02,TEAM03,TEAM04};
    }
    @Nullable
    public static Team getByID(String id) {
        if (!id.matches("\\d")) return null;
        int i = Integer.parseInt(id);
        switch (i) {
            case 1:
                return TEAM01;
            case 2:
                return TEAM02;
            case 3:
                return TEAM03;
            case 4:
                return TEAM04;
        }
        return null;
    }
    @Nullable
    public static Team getByBedLocation(Location loc) {
        return Arrays.stream(TEAMS).filter(r -> r.hasBed() && Round.similarLocation(r.getBedLocation(), loc, 1)).findFirst().orElse(null);
    }
    @Nullable
    public static Team getByID(int id) {
        switch (id) {
            case 1:
                return TEAM01;
            case 2:
                return TEAM02;
            case 3:
                return TEAM03;
            case 4:
                return TEAM04;
        }
        return null;
    }
    @Nullable
    public static Team getEntry(String entry) {
        org.bukkit.scoreboard.Team t = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(entry);
        if (t == null) return null;
        if (t.equals(TEAM01.TEAM)) return TEAM01;
        if (t.equals(TEAM02.TEAM)) return TEAM02;
        if (t.equals(TEAM03.TEAM)) return TEAM03;
        if (t.equals(TEAM04.TEAM)) return TEAM04;
        return null;
    }
    public static void randomTeam(int teams) {
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);
        for (int i = 0 ; i < players.size() ; i++) {
            int teamID = (i % teams) + 1;
            Team team = getByID((teams == 2 && teamID == 2 ? 3 : teamID));
            if (team == null) continue;
            team.appendPlayer(players.get(i).getName());
        }
    }
    private org.bukkit.scoreboard.Team TEAM;
    private boolean t_canWarpSurvival;
    private Location SPAWNPOINT;
    private Location BED;
    private Location BED2;
    private Location SURVIVAL;
    private Location PORTAL;
    private String color;
    private int id;
    private int protection_level;
    private int efficiency_level;
    private HasteUpgrade UPGRADE_HASTE;
    private HomeGenerator GENERATOR;
    private ToolsEfficiencyUpgrade UPGRADE_TOOLS;
    private ArmorProtectionUpgrade UPGRADE_ARMOR;
    private ArrayList<TrapUpgrade> TRAPS;
    private Team(int id, Location survival, String color) {
        this.id = id;
        this.color = color;
        this.protection_level = 0;
        this.efficiency_level = 0;
        this.PORTAL = BedWars.MAP.getPortalBlock(this);
        this.SPAWNPOINT = BedWars.MAP.getSpawnPoint(this);
        this.BED = BedWars.MAP.getBedHead(this);
        this.BED2 = BedWars.MAP.getBedBack(this);
        this.SURVIVAL = survival;
        this.UPGRADE_ARMOR = new ArmorProtectionUpgrade(this);
        this.UPGRADE_TOOLS = new ToolsEfficiencyUpgrade(this);
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        if ((TEAM = sb.getTeam("bw"+id)) == null) {
            TEAM = sb.registerNewTeam("bw"+id);
        }
        TEAM.setAllowFriendlyFire(false);
        TEAM.setPrefix(color+"[0"+id+"] ");
        this.GENERATOR = new HomeGenerator(BedWars.MAP.getHomeGenerator(this),this);
        TEAM.setSuffix("§r");
        t_canWarpSurvival = true;
        this.UPGRADE_HASTE = new HasteUpgrade(this);
        TRAPS = new ArrayList<>(Arrays.asList(new FatigueTrapUpgrade(this),new BlindTrapUpgrade(this)));
    }

    public int getProtectionLevel() {
        return protection_level;
    }

    public int getEfficiencyLevel() {
        return efficiency_level;
    }

    public ArmorProtectionUpgrade getArmorUpgrader() {
        return UPGRADE_ARMOR;
    }
    public ToolsEfficiencyUpgrade getToolsUpgrader() {
        return UPGRADE_TOOLS;
    }

    public void setProtectionLevel(int protection_level) {
        this.protection_level = protection_level;
        membersForeach(p -> {
            PlayerInventory inv = p.getInventory();
            for (int i = 0 ; i < 41; i++) {
                ItemStack item = inv.getItem(i);
                if (item == null || item.getType().equals(Material.AIR)) continue;
                if (!Round.isArmor(item.getType())) continue;
                if (item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= protection_level) continue;
                item.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
                item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,protection_level);
                inv.setItem(i,item);
            }
        });
    }
    public void setEfficiencyLevel(int efficiency_level) {
        this.efficiency_level = efficiency_level;
        membersForeach(p -> {
            PlayerInventory inv = p.getInventory();
            for (int i = 0 ; i < 41; i++) {
                ItemStack item = inv.getItem(i);
                if (item == null || item.getType().equals(Material.AIR)) continue;
                if (!Round.isTools(item.getType())) continue;
                if (item.getEnchantmentLevel(Enchantment.DIG_SPEED) >= efficiency_level) continue;
                item.removeEnchantment(Enchantment.DIG_SPEED);
                item.addEnchantment(Enchantment.DIG_SPEED,efficiency_level);
                inv.setItem(i,item);
            }
        });
    }

    public void setT_canWarpSurvival(boolean t_canWarpSurvival) {
        this.t_canWarpSurvival = t_canWarpSurvival;
    }

    public boolean T_canWarpSurvival() {
        return t_canWarpSurvival;
    }

    public ArrayList<TrapUpgrade> getTraps() {
        return TRAPS;
    }

    public void membersForeach(PlayerTask pt) {
        for (String s : TEAM.getEntries()) {
            Player p = Bukkit.getPlayer(s);
            if (p == null) continue;
            pt.apply(p);
        }
    }

    public HomeGenerator getHomeGenerator() {
        return GENERATOR;
    }

    public org.bukkit.scoreboard.Team getScoreboardTeam() {
        return TEAM;
    }

    public void teleportAll(Location loc) {
        for (String e : TEAM.getEntries()) {
            Player p = Bukkit.getPlayer(e);
            if (p == null) continue;
            p.teleport(loc);
        }
    }

    public HasteUpgrade getUpgradeHaste() {
        return UPGRADE_HASTE;
    }

    public Location getSecondBedBlock() {
        return BED2;
    }

    public Player getFirstEntry() {
        ArrayList<String> e = new ArrayList<>(TEAM.getEntries());
        Collections.sort(e);
        for (String entry : e) {
            Player player = Bukkit.getPlayer(entry);
            if (player != null) return player;
        }
        return null;
    }

    public void close() {
        BED.getBlock().setType(Material.AIR);
        BED2.getBlock().setType(Material.AIR);
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return color+"§lT"+id;
    }

    public Location getSpawnpoint() {
        if (!Home.CanWarpHome) return SURVIVAL;
        return SPAWNPOINT;
    }

    public Location getBedLocation() {
        return BED;
    }

    public Location getPortal() {
        return PORTAL;
    }

    public Location getSurvivalSpawnpoint() {
        return SURVIVAL;
    }

    public boolean hasBed() {
        return BED.getBlock().getType().equals(Material.BED_BLOCK);
    }

    public int getAlivePeople() {
        int c = 0;
        for (String entry : TEAM.getEntries()) {
            Player p = Bukkit.getPlayer(entry);
            if (p == null) continue;
            if (!p.getGameMode().equals(GameMode.SPECTATOR)) c++;
        }
        return c;
    }

    public String getColor() {
        return color;
    }

    public void appendPlayer(String name) {
        TEAM.addEntry(name);
    }
    public void removePlayer(String name) {
        TEAM.removeEntry(name);
    }
    public void resetTeam() {
        for (String entry : TEAM.getEntries()) {
            TEAM.removeEntry(entry);
        }
    }
    public interface PlayerTask {
        void apply(Player p);
    }
}

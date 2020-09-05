package com.cutesmouse.airplane.permission;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.generator.DiamondGenerator;
import com.cutesmouse.airplane.generator.EmeraldGenerator;
import com.cutesmouse.airplane.tool.Round;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;

import static com.cutesmouse.airplane.BedWars.MAP;

public class BreakingManager {
    public static ArrayList<Location> BLOCKS = new ArrayList<>();
    public static boolean byPass = false;
    /*
    破壞規則:
        1.炸彈不能炸到100層以下區域
        2.以下是隊伍個別的保留區域
            - T1 : Z >= 79
            - T2 : X <= -81
            - T3 : Z <= -81
            - T4 : X >= 81
        3.公共設施附近不能放置/破壞任何方塊
            - 各島嶼重生點(熔爐) 3x3區域
            - 傳送門附近 3x3區域
            - 鑽石生成器附近 3x3區域
            - 綠寶石生成器附近 3x3區域
        4.可建築範圍
            X : -75 ~ 75
            Y : 75 ~ 130
            Z : -75 ~ 75
        5.預設的方塊除了杉木柵欄外其他都不能破壞
     */
    public static boolean canIBreak(@Nullable Player p, Location loc) {
        if (byPass) return true;
        //只有床戰世界有限制
        if (!loc.getWorld().getName().equals("world")) return true;
        //超過一定邊界不可放置/破壞方塊
        if (loc.getBlockX() > MAP.maxX() || loc.getBlockX() < MAP.minX()) return false;
        if (loc.getBlockY() > MAP.maxY() || loc.getBlockY() < MAP.minY()) return false;
        if (loc.getBlockZ() > MAP.maxZ() || loc.getBlockX() < MAP.minZ()) return false;
        //位在公共設施旁
        if (isSystemArea(null,loc)) return false;
        //個別島嶼
        if (ownTeamBase(p,loc)) return true;
        if (isTeamOwningArea(loc)) {
            if (p == null) return false;
            Team t = getTeamOwningArea(loc);
            if (t == null) return false;
            Team entry = Team.getEntry(p.getName());
            if (entry == null) return false;
            if (entry.equals(t)) return true;
            return false;
        }
        //玩家所放置的、非預設
        if (!BLOCKS.contains(loc)) return false;
        BLOCKS.removeIf(r -> Round.sameLocation(r,loc));
        ExplosionManager.EXPLODE_BLACKLIST.removeIf(r -> Round.sameLocation(r,loc));
        //可破壞的
        return true;
    }
    public static boolean canIPlace(Player p, Block block, Block placed) {
        if (byPass) return true;
        Location loc = block.getLocation();
        //只有床戰世界有限制
        if (!loc.getWorld().getName().equals("world")) return true;
        //禁止在空島放樹苗
        if (placed.getType().equals(Material.SAPLING)) return false;
        //超過一定邊界不可放置/破壞方塊
        if (loc.getBlockX() > MAP.maxX() || loc.getBlockX() < MAP.minX()) return false;
        if (loc.getBlockY() > MAP.maxY() || loc.getBlockY() < MAP.minY()) return false;
        if (loc.getBlockZ() > MAP.maxZ() || loc.getBlockX() < MAP.minZ()) return false;
        //位在公共設施旁
        if (isSystemArea(p,loc)) return false;
        //個別島嶼
        if (isTeamOwningArea(loc)) {
            Team t = getTeamOwningArea(loc);
            if (t == null) return false;
            Team entry = Team.getEntry(p.getName());
            if (entry == null) return false;
            if (entry.equals(t)) return true;
            return false;
        }
        //可放置
        return true;
    }

    public static boolean isTeamOwningArea(Location loc) {
        if (!loc.getWorld().equals(BedWars.DEFAULT)) return false;
        return getTeamOwningArea(loc) != null;
    }
    /*
            - T1 : Z >= 82
            - T2 : X <= -81
            - T3 : Z <= -81
            - T4 : X >= 79
     */
    public static Team getTeamOwningArea(Location loc) {
        for (Team team : Team.TEAMS) {
            if (Round.similarLocation(loc,team.getSpawnpoint(),MAP.ownTeamProtectionRadius())) return team;
        }
        return null;
    }
    public static boolean ownTeamBase(Player p, Location loc) {
        if (p == null) return false;
        Team t = Team.getEntry(p.getName());
        if (t == null) return false;
        return Round.similarLocation(t.getSpawnpoint(),loc,20);
    }
    public static boolean isSystemArea(Player p, Location loc) {
        for (Team t : Team.TEAMS) {
            if (Round.similarLocation(loc,t.getSpawnpoint(),1)) return true;
            if (Round.similarLocation(loc,t.getPortal(),1)) return true;
            if (Round.similarLocation(loc,t.getHomeGenerator().getLoc(),1)) return true;
        }
        for (DiamondGenerator diamond : BedWars.DIAMOND_GENERATORS) {
            if (Round.similarLocation(loc,diamond.getLoc(),1)) return true;
        }
        for (EmeraldGenerator emerald : BedWars.EMERALD_GENERATORS) {
            if (Round.similarLocation(loc,emerald.getLoc(),1)) return true;
        }
        return false;
    }
}

package com.cutesmouse.airplane;

import com.cutesmouse.airplane.commands.AdminCommand;
import com.cutesmouse.airplane.commands.Home;
import com.cutesmouse.airplane.commands.TeamSelectCommand;
import com.cutesmouse.airplane.generator.ItemBank;
import com.cutesmouse.airplane.gmListener.BeforeGamePlayerListener;
import com.cutesmouse.airplane.gmListener.GameplayListener;
import com.cutesmouse.airplane.gui.TeamSelectorGUI;
import com.cutesmouse.airplane.map.ConfigMap;
import com.cutesmouse.airplane.map.DefaultMap;
import com.cutesmouse.airplane.map.Map;
import com.cutesmouse.airplane.tool.NPCManager;
import com.cutesmouse.airplane.tool.ObjectiveData;
import com.cutesmouse.airplane.tool.Round;
import com.cutesmouse.airplane.tool.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.cutesmouse.airplane.BedWars.MAP;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        loadConfig();
        Map conf = getConfig().getString("map") == null ? new DefaultMap() : (ConfigMap.Get(getConfig().getString("map")));
        MAP = ((conf instanceof ConfigMap && !((ConfigMap) conf).loaded()) ? new DefaultMap() : conf);
        ItemBank.loadBank();
        BedWars.loadPrivateWorld();
        loadCommands();
        loadEvents();
        Team.loadTeams();
        TeamSelectorGUI.loadTeamSelector();
        BedWars.loadEntities();
        loadScoreboard();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getWorld().equals(BedWars.DEFAULT)) p.teleport(BedWars.MAP.getDefaultLocation());
            p.setAllowFlight(true);
        }
    }

    @Override
    public void onDisable() {
        NPCManager.removeNPCS();
    }

    private void loadConfig() {
        File map = new File(getDataFolder(),"maps");
        if (!map.exists()) map.mkdirs();
        saveResource("maps/example.yml",false);
        ConfigMap.MAP_FOLDER = map;
        getConfig().addDefault("map","default");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    private void loadCommands() {
        getCommand("home").setExecutor(new Home());
        getCommand("bw").setExecutor(new AdminCommand());
        getCommand("team").setExecutor(new TeamSelectCommand());
    }
    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new BeforeGamePlayerListener(),this);
        GameplayListener listener = new GameplayListener();
        getServer().getPluginManager().registerEvents(listener,this);
        new BukkitRunnable() {
            @Override
            public void run() {
                listener.tickEvent();
            }
        }.runTaskTimer(this,0L,1L);
    }
    private void loadScoreboard() {
        getServer().getPluginManager().registerEvents(ScoreboardManager.INSTANCE,this);
        ObjectiveData data = new ObjectiveData();
        data.set(10,s -> "§6歡迎 §e" + s.getName());
        data.set(9,s -> "§6參加本屆");
        data.set(8,s -> "§e§l「飛機盃 生存床戰」");
        data.set(7, s -> "§6伺服器人數: §e"+ Bukkit.getOnlinePlayers().size());
        data.set(6, s-> {
            Team t = Team.getEntry(s.getName());
            return "§6所在隊伍: §e"+(t == null ? "無" : t.getColor()+"T"+t.getId());
        });
        data.set(5, s -> "§a");
        data.set(4,s -> "§6地圖: §e"+BedWars.MAP.getName());
        data.set(3, s -> "§c");
        data.set(1,s -> "§7"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        ScoreboardManager.INSTANCE.setObjective_Data(data);
        ScoreboardManager.INSTANCE.setObjective_DisplayName("§d◤§b§l飛機盃 §a生存床戰§d◢");
        ScoreboardManager.INSTANCE.setObjective_Name("list");
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(r -> r.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE
                ,0,true,false)));
                ScoreboardManager.INSTANCE.updateSidebarData();
            }
        }.runTaskTimer(this,0L,10L);
    }
}

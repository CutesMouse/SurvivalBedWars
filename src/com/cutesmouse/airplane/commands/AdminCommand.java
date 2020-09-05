package com.cutesmouse.airplane.commands;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.gui.MapSetGUI;
import com.cutesmouse.airplane.gui.TeamSelectorGUI;
import com.cutesmouse.airplane.map.ConfigMap;
import com.cutesmouse.airplane.npc.PlayerNPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class AdminCommand implements CommandExecutor {
    private final String INTRO = "§6用法: §f";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§c無權限!");
            return true;
        }
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) {
            sender.sendMessage(INTRO+"/bw <tp|team|start|time|map>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "tp":
                tp(((Player) sender),args);
                break;
            case "team":
                team(((Player) sender),args);
                break;
            case "start":
                BedWars.INSTANCE.start(((Player) sender));
                break;
            case "time":
                time(((Player) sender),args);
                break;
            case "speed":
                speed(((Player) sender),args);
                break;
            case "map":
                map(((Player) sender),args);
                break;
        }
        return true;
    }
    private void map(Player sender, String[] args) {
        // /bw map <name>
        // /bw map create <name>
        if (args.length == 2) {
            ConfigMap map = ConfigMap.Get(args[1]);
            if (!map.loaded()) {
                sender.sendMessage("§c找不到目標地圖!");
                return;
            }
            BedWars.MAP = map;
            sender.sendMessage("§a已更新!");
            return;
        }
        sender.sendMessage(INTRO+"/bw map <地圖名稱>");
    }
    private void speed(Player sender, String[] args) {
        // /bw speed 10
        if (args.length != 2) {
            sender.sendMessage(INTRO+"/bw speed <倍率> §c§l此功能僅做測試用!!");
            return;
        }
        if (!args[1].matches("\\d+")) {
            sender.sendMessage("§c必須填入數字!");
        }
        BedWars.INSTANCE.setSpeed(Integer.parseInt(args[1]));
        sender.sendMessage("§a已設定!");
    }
    private void time(Player sender, String[] args) {
        // /bw time 30:10
        if (args.length != 2) {
            sender.sendMessage(INTRO+"/bw time <10:00> (分:秒)");
            return;
        }
        String[] time_s = args[1].split(":");
        if (time_s.length != 2 || !time_s[0].matches("\\d{1,2}") || !time_s[1].matches("\\d{1,2}")) {
            sender.sendMessage(INTRO+"/bw time <10:00> (分:秒)");
            return;
        }
        int mins = Integer.parseInt(time_s[0]);
        int secs = Integer.parseInt(time_s[1]);
        BedWars.INSTANCE.setTime(mins*60*20+secs*20);
        sender.sendMessage("§a已設定!");
    }
    private void tp(Player sender, String[] args) {
        // /bw tp team01
        if (args.length != 2) {
            sender.sendMessage(INTRO+"/bw tp <世界名稱>");
            sender.sendMessage("§7可用: "+ Bukkit.getWorlds().stream().map(w -> ", "+w.getName()).collect(Collectors.joining()).substring(2));
            return;
        }
        World w = Bukkit.getWorld(args[1]);
        if (w == null) {
            sender.sendMessage("§7可用: "+ Bukkit.getWorlds().stream().map(world -> ", "+world.getName()).collect(Collectors.joining()).substring(2));
            return;
        }
        sender.teleport(new Location(w,0,w.getHighestBlockYAt(0,0)+1,0));
        sender.sendMessage("§a已傳送!");
    }
    private void team(Player sender, String[] args) {
        // /bw team random
        // /bw team set <player> <team>
        // /bw reset
        // /bw remove
        // /bw team selector [true|false]
        if (args.length < 2) {
            sender.sendMessage(INTRO+"/bw team <random|set|reset|remove|selector>");
            sender.sendMessage("§7可用: 1, 2, 3, 4");
            return;
        }
        switch (args[1].toLowerCase()) {
            case "selector":
                if (args.length != 3) {
                    sender.sendMessage(INTRO+"/bw team selector <true|false>");
                    sender.sendMessage("§7當前狀態: "+ TeamSelectorGUI.STATUS);
                    return;
                }
                TeamSelectorGUI.STATUS = args[2].toLowerCase().equals("true");
                sender.sendMessage("§a已更新當前狀態為 "+TeamSelectorGUI.STATUS);
                break;
            case "random":
                // bw team random 4
                int teams = 4;
                if (args.length == 3 && args[2].matches("\\d")) {
                    teams = Integer.parseInt(args[2]);
                    if (teams > 4) {
                        sender.sendMessage("§c隊伍數量不得大於4!");
                        return;
                    }
                } else if (args.length == 3) {
                    sender.sendMessage("§c隊伍數量必須是一個小於4的正整數!");
                    return;
                }
                Team.randomTeam(teams);
                break;
            case "set":
                if (args.length != 4) {
                    sender.sendMessage(INTRO+"/bw team set <玩家名稱> <隊伍名稱>");
                    sender.sendMessage("§7可用: 1, 2, 3, 4");
                    return;
                }
                Team setTarget = Team.getByID(args[3]);
                if (setTarget == null) {
                    sender.sendMessage("§c找不到目標隊伍!");
                    return;
                }
                setTarget.appendPlayer(args[2]);
                sender.sendMessage("§a成功將 §b"+args[2]+" §a新增至隊伍 §bT"+args[3]+"§a !");
                break;
            case "reset":
                Team.TEAM01.resetTeam();
                Team.TEAM02.resetTeam();
                Team.TEAM03.resetTeam();
                Team.TEAM04.resetTeam();
                sender.sendMessage("§a隊伍重置完成!");
                break;
            case "remove":
                if (args.length != 3) {
                    sender.sendMessage(INTRO+"/bw team remove <玩家名稱>");
                    return;
                }
                Team.TEAM01.removePlayer(args[2]);
                Team.TEAM02.removePlayer(args[2]);
                Team.TEAM03.removePlayer(args[2]);
                Team.TEAM04.removePlayer(args[2]);
                sender.sendMessage("§a已將 §b"+args[2]+" §a從所有隊伍中移除!");
                break;
            default:
                sender.sendMessage(INTRO+"/bw team <random|set|reset|remove>");
                sender.sendMessage("§7可用: 1, 2, 3, 4");
        }
    }
}

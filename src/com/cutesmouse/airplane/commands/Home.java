package com.cutesmouse.airplane.commands;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {
    public static boolean CanWarpHome = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        Team t = Team.getEntry(sender.getName());
        if (t == null) {
            sender.sendMessage("§c你不在任何隊伍中! 無法使用此指令");
            return true;
        }
        if (!CanWarpHome) {
            sender.sendMessage("§c要10分鐘後才能回島嶼喔!");
            return true;
        }
        if (((Player) sender).getWorld().equals(BedWars.DEFAULT)) {
            sender.sendMessage("§c只有在生存世界可以使用此指令!");
            return true;
        }
        ((Player) sender).teleport(t.getSpawnpoint());
        sender.sendMessage("§a已傳送!");
        return true;
    }
}

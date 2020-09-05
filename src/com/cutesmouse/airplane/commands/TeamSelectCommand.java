package com.cutesmouse.airplane.commands;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.gui.TeamSelectorGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamSelectCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (!TeamSelectorGUI.STATUS) {
            sender.sendMessage("§c未開放的指令!");
            return true;
        }
        if (BedWars.INSTANCE.hasStarted()) {
            sender.sendMessage("§c遊戲已經開始! 無法使用此指令!");
            return true;
        }
        TeamSelectorGUI.OpenTeamSelector(((Player) sender));
        return true;
    }
}

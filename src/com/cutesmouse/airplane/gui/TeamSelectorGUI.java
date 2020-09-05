package com.cutesmouse.airplane.gui;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.tool.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class TeamSelectorGUI {
    public static Inventory TeamSelector;
    public static boolean STATUS = false;
    public static void loadTeamSelector() {
        TeamSelector = Bukkit.createInventory(null,9,"§d◤§b§l飛機盃 §a隊伍選擇§d◢");
    }
    public static Team getJoinTeam() {
        List<Team> teams = new ArrayList<>(Arrays.asList(Team.TEAMS));
        Collections.shuffle(teams);
        return teams.stream().min(Comparator.comparing(t -> t.getScoreboardTeam().getEntries().size())).orElse(teams.get(0));
    }
    public static void OpenTeamSelector(Player p) {
        for (int i = 0 ; i < 9 ; i ++) TeamSelector.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 7));
        for (Team t : Team.TEAMS) {
            ArrayList<String> lores = new ArrayList<>(Collections.singletonList(
                    (t.getScoreboardTeam().getEntries().size() >= getMaxPlayer() ? "§c已滿!" :
                            "§a點擊加入! (" + t.getScoreboardTeam().getSize() + "/" + getMaxPlayer() + ")"
                    )));
            if (t.getScoreboardTeam().getSize() != 0) {
                lores.add("§6成員列表: ");
                lores.addAll(t.getScoreboardTeam().getEntries().stream().map(s -> "§e"+s).collect(Collectors.toCollection(ArrayList::new)));
            } else {
                lores.add("§6無成員!");
            }
            TeamSelector.setItem(t.getId()-1, new ItemStackBuilder(Material.WOOL).setDurability(getColorCode(t))
                    .setName(t.getColor()+"隊伍 T"+t.getId()).setLore(lores).withTag("invClickEvent","setTeam")
                    .withTag("targetTeam",Integer.toString(t.getScoreboardTeam().getSize() >= getMaxPlayer() ? 0 : t.getId())).build());
        }
        p.openInventory(TeamSelector);
    }
    public static int getMaxPlayer() {
        int players = Bukkit.getOnlinePlayers().size();
        return (players / 2) +1;
    }
    private static short getColorCode(Team t) {
        if (t == null) return 0;
        switch (t.getId()) {
            case 1:
                return 14;
            case 2:
                return 4;
            case 3:
                return 11;
            case 4:
                return 13;
        }
        return 0;
    }
}

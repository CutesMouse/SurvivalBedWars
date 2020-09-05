package com.cutesmouse.airplane.gui;

import com.cutesmouse.airplane.map.ConfigMapBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MapSetGUI {
    private static HashMap<String, ConfigMapBuilder> PLAYERS = new HashMap<>();
    public static void OpenMapSetGUI(Player p, String name) {
        if (PLAYERS.containsKey(p.getName())) {
            PLAYERS.get(p.getName()).OpenSettingInventory(p);
            return;
        }
        if (name == null) return;
        ConfigMapBuilder builder = new ConfigMapBuilder(name);
        builder.OpenSettingInventory(p);
        PLAYERS.put(p.getName(),builder);
    }
}

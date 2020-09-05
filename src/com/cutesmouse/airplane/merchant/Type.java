package com.cutesmouse.airplane.merchant;

import com.cutesmouse.mgui.GUI;
import com.cutesmouse.mgui.GUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Type {
    private final ArrayList<Item> POOL;
    private final Material display;
    private final String typeName;
    public Type(String typeName, Material display, ArrayList<Item> pool) {
        this.typeName = typeName;
        this.POOL = pool;
        this.display = display;
    }
    public GUIItem getGuiItem() {
        return new GUIItem(display,null,"§9"+typeName,(e,i) -> {
            e.setCancelled(true);
            GUI gui = new GUI("§d◤§b§l飛機盃 §a玩家商店 §9"+typeName+"§d◢",3, e.getWhoClicked());
            for (int ie = 0 ; ie < 27 ; ie++) gui.addItem(ie,new GUIItem(Material.STAINED_GLASS_PANE,null,"§r",GUI.blank(),7));
            int first = getFirst(POOL.size());
            int index = 0;
            for (int p = first; index < POOL.size() ; p++) {
                if (p == 13 && POOL.size() % 2 == 0) continue;
                gui.addItem(p,POOL.get(index).getGuiItem());
                index++;
            }
            e.getWhoClicked().openInventory(gui.getInv());
        });
    }
    private int getFirst(int amount) {
        switch (amount) {
            case 9:
            case 8:
                return 9;
            case 7:
            case 6:
                return 10;
            case 5:
            case 4:
                return 11;
            case 3:
            case 2:
                return 12;
            case 1:
                return 13;
        }
        return 0;
    }

}

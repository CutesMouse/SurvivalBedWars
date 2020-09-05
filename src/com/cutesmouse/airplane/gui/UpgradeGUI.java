package com.cutesmouse.airplane.gui;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.generator.ItemBank;
import com.cutesmouse.airplane.merchant.UpgradeItem;
import com.cutesmouse.airplane.merchant.Upgradeable;
import com.cutesmouse.airplane.tool.ItemStackBuilder;
import com.cutesmouse.airplane.tool.Round;
import com.cutesmouse.airplane.upgrades.TrapUpgrade;
import com.cutesmouse.mgui.GUI;
import com.cutesmouse.mgui.GUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UpgradeGUI {
    public static void OpenUpgrade(Player p) {
        Team t = Team.getEntry(p.getName());
        if (t == null) {
            p.sendMessage("§c系統找不到你的隊伍! 無法開啟介面");
            return;
        }
        GUI gui = new GUI("§d◤§b§l飛機盃 §a升級商店§d◢",3,p);
        for (int i = 0 ; i < 27 ; i++) gui.addItem(i,new GUIItem(Material.STAINED_GLASS_PANE,null,"§r",GUI.blank(),7));
        gui.addItem(10,genItem(t.getHomeGenerator(),Material.FURNACE));
        gui.addItem(11,genItem(t.getUpgradeHaste(),Material.GOLD_PICKAXE));
        gui.addItem(12,genItem(t.getArmorUpgrader(),Material.IRON_CHESTPLATE));
        gui.addItem(14,genItem(t.getToolsUpgrader(),Material.IRON_PICKAXE));
        gui.addItem(15,genItem(t.getTraps().get(0),t.getTraps().get(0).getDisplay()));
        gui.addItem(16,genItem(t.getTraps().get(1),t.getTraps().get(1).getDisplay()));
        p.openInventory(gui.getInv());
    }
    private static GUIItem genItem(Upgradeable upgrade, Material m) {
        return new UpgradeItem(Round.changeAmount(ItemBank.ENCHANTED_DIAMOND,upgrade.nextLVCost()),upgrade,
                new ItemStackBuilder(m)
                        .setName("§9"+upgrade.getItemName()+(upgrade instanceof TrapUpgrade ? "" : (" "+Round.Rome(Math.min(upgrade.getLevel()+1,upgrade.getMaxLevel())))))
                        .setLore(new ArrayList<>(upgrade.getIntro())).build()).getGuiItem();
    }
}

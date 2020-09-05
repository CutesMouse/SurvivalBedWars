package com.cutesmouse.airplane.merchant;

import com.cutesmouse.airplane.gui.UpgradeGUI;
import com.cutesmouse.mgui.GUI;
import com.cutesmouse.mgui.GUIItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeItem {
    private static final HashMap<String,Long> COOLDOWN = new HashMap<>();
    private final ItemStack COST;
    private final Upgradeable GET;
    private final ItemStack DISPLAY;
    private final int LEVEL;
    public UpgradeItem(ItemStack cost, Upgradeable get, ItemStack display) {
        this.COST = cost;
        this.GET = get;
        this.DISPLAY = display;
        this.LEVEL = get.getLevel();
    }
    public GUIItem getGuiItem() {
        ItemStack display = new ItemStack(DISPLAY);
        if (GET.getLevel() == GET.getMaxLevel()) return new GUIItem(DISPLAY, GUI.blank());
        ItemMeta meta = display.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7花費: "+COST.getAmount()+" "+COST.getItemMeta().getDisplayName());
        if (meta.hasLore()) {
            lore.addAll(meta.getLore());
        }
        meta.setLore(lore);
        display.setItemMeta(meta);
        return new GUIItem(display,(e,i) -> {
            e.setCancelled(true);
            if (LEVEL != GET.getLevel()) {
                UpgradeGUI.OpenUpgrade(((Player) e.getWhoClicked()));
                return;
            }
            switch (purchase(((Player) e.getWhoClicked()))) {
                case NOTENOUGHSOURCE:
                    e.getWhoClicked().sendMessage("§c升級失敗! 您沒有足夠數量的"+COST.getItemMeta().getDisplayName()+"§c!");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,
                            1,1);
                    break;
                case SUCCESS:
                    GET.getTeam().membersForeach(r -> r.sendMessage("§a§l"+e.getWhoClicked().getName()+" §a升級了 §9"+DISPLAY.getItemMeta().getDisplayName()));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_PLING,
                            1,1);
                    UpgradeGUI.OpenUpgrade(((Player) e.getWhoClicked()));
                    break;
            }
        });
    }
    public PurchaseResult purchase(Player p) {
        if (COOLDOWN.containsKey(p.getName()) && System.currentTimeMillis() - COOLDOWN.get(p.getName()) < 100L) {
            return PurchaseResult.SLOWDOWN;
        }
        int own_amount = getItemCount(p,COST);
        if (own_amount < COST.getAmount()) {
            return PurchaseResult.NOTENOUGHSOURCE;
        }
        removeItem(p.getInventory(),COST,COST.getAmount());
        GET.upgrade();
        COOLDOWN.put(p.getName(),System.currentTimeMillis());
        return PurchaseResult.SUCCESS;
    }
    private void removeItem(Inventory inv, ItemStack item, int amount) {
        ItemStack[] c = inv.getContents();
        for (int id = 0; id < c.length; id++) {
            if (amount == 0) return;
            ItemStack i = c[id];
            if (i == null) continue;
            if (!i.isSimilar(item)) continue;
            if (i.getAmount() >= amount) {
                ItemStack newStack = new ItemStack(item);
                newStack.setAmount(i.getAmount() - amount);
                inv.setItem(id, newStack);
                amount = 0;
            }
            else {
                inv.setItem(id,null);
                amount -= i.getAmount();
            }
        }
    }
    private int getItemCount(Player p, ItemStack item) {
        ItemStack[] s = p.getInventory().getStorageContents();
        int sum = 0;
        for (ItemStack r : s) {
            if (r == null) continue;
            if (!r.isSimilar(item)) continue;
            sum += r.getAmount();
        }
        return sum;
    }

}

package com.cutesmouse.airplane.merchant;

import com.cutesmouse.mgui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item {
    private static final HashMap<String,Long> COOLDOWN = new HashMap<>();
    private final ItemStack COST;
    private final ItemStack GET;
    public Item(ItemStack cost, ItemStack get) {
        this.COST = cost;
        this.GET = get;
    }

    public GUIItem getGuiItem() {
        ItemStack display = new ItemStack(GET);
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
            switch (purchase(((Player) e.getWhoClicked()))) {
                case NOTENOUGHSOURCE:
                    e.getWhoClicked().sendMessage("§c購買失敗! 您沒有足夠數量的"+COST.getItemMeta().getDisplayName()+"§c!");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,
                            1,1);
                    break;
                case NOTENOUGHSPACE:
                    e.getWhoClicked().sendMessage("§c購買失敗! 您的物品欄空間不足!");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,
                            1,1);
                    break;
                case SUCCESS:
                    e.getWhoClicked().sendMessage("§a已成功購買!");
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_PLING,
                            1,1);
                    break;

            }
        });
    }
    public PurchaseResult purchase(Player p) {
        if (COOLDOWN.containsKey(p.getName()) && System.currentTimeMillis() - COOLDOWN.get(p.getName()) < 100L) {
            return PurchaseResult.SLOWDOWN;
        }
        PlayerInventory inv = p.getInventory();
        int empty = inv.firstEmpty();
        if (empty == -1) {
            return PurchaseResult.NOTENOUGHSPACE;
        }
        int own_amount = getItemCount(p,COST);
        if (own_amount < COST.getAmount()) {
            return PurchaseResult.NOTENOUGHSOURCE;
        }
        removeItem(p.getInventory(),COST,COST.getAmount());
        p.getInventory().addItem(new ItemStack(GET));
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

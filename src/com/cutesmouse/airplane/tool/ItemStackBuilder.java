package com.cutesmouse.airplane.tool;

import com.cutesmouse.airplane.Team;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemStackBuilder {
    private ItemStack item;
    public ItemStackBuilder(Material m) {
        item = new ItemStack(m);
        ENCHS = new HashMap<>();
        hideEnchant = false;
        amount = 1;
    }
    public Material getType() {
        return item.getType();
    }
    private String itemName;
    private ArrayList<String> itemLore;
    private short durability = 0;
    private int amount;
    private boolean hideEnchant;
    private HashMap<Enchantment, Integer> ENCHS;
    public ItemStackBuilder setName(String name) {
        this.itemName = name;
        return this;
    }
    public ItemStackBuilder withTag(String tag, Object value) {
        ItemNBTManager manager = new ItemNBTManager(item);
        manager.modify(tag,value);
        item = manager.getItemStack();
        return this;
    }
    public ItemStackBuilder checkLowestEnchant(Player p) {
        Team t = Team.getEntry(p.getName());
        if (t == null) return this;
        if (Round.isTools(item.getType())) {
            if (t.getEfficiencyLevel() != 0) return addEnchant(Enchantment.DIG_SPEED,t.getEfficiencyLevel());
        }
        if (Round.isArmor(item.getType())) {
            if (t.getProtectionLevel() != 0) return addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,t.getProtectionLevel());
        }
        return this;
    }
    public ItemStackBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    public ItemStackBuilder setDurability(short s) {
        this.durability = s;
        return this;
    }
    public ItemStackBuilder hideEnchant() {
        hideEnchant = true;
        return this;
    }
    public ItemStackBuilder setLore(ArrayList<String> lores) {
        this.itemLore = lores;
        return this;
    }
    public ItemStackBuilder addEnchant(Enchantment enchant, int level) {
        ENCHS.put(enchant,level);
        return this;
    }
    public ItemStack build() {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        if (itemLore != null) meta.setLore(itemLore);
        if (itemName != null) meta.setDisplayName(itemName);
        if (ENCHS.size() != 0) ENCHS.forEach((a,b) -> meta.addEnchant(a,b,true));
        if (hideEnchant) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.setAmount(amount);
        if (durability != 0) item.setDurability(durability);
        return item;
    }
}

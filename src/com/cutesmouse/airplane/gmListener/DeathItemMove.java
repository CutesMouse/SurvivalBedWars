package com.cutesmouse.airplane.gmListener;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.generator.ItemBank;
import com.cutesmouse.airplane.tool.ItemNBTManager;
import com.cutesmouse.airplane.tool.Round;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DeathItemMove {
    /*
    鐵、金、綠寶石、鑽石、煤炭
     */
    public DeathItemMove(Player deathEntity, Player killer) {
        ArrayList<String> GIVE = new ArrayList<>();
        target(deathEntity,killer,GIVE,Material.IRON_INGOT,"§3鐵錠");
        target(deathEntity,killer,GIVE,Material.GOLD_INGOT,"§3金錠");
        target(deathEntity,killer,GIVE,Material.EMERALD,"§3綠寶石");
        target(deathEntity,killer,GIVE,Material.DIAMOND,"§3鑽石");
        target(deathEntity,killer,GIVE,Material.COAL,"§3煤炭");
        target(deathEntity,killer,GIVE,ItemBank.ENCHANTED_EMERALD);
        target(deathEntity,killer,GIVE,ItemBank.ENCHANTED_IRON);
        target(deathEntity,killer,GIVE,ItemBank.ENCHANTED_GOLD);
        target(deathEntity,killer,GIVE,ItemBank.ENCHANTED_DIAMOND);
        GIVE.forEach(s -> {
            deathEntity.sendMessage("§c- "+s);
            killer.sendMessage("§a+ "+s);
        });
        killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1F,1F);
    }
    private void target(Player deathEntity,Player killer,ArrayList<String> GIVE,ItemStack item) {
        Team t = Team.getEntry(deathEntity.getName());
        if (t == null) return;
        ItemStack TARGET = new ItemStack(item);
        int has = getItemCount(deathEntity,TARGET);
        int give = t.hasBed() ? (has / 2) : has;
        if (give != 0) {
            GIVE.add(item.getItemMeta().getDisplayName()+"§r x"+give);
            removeItem(deathEntity.getInventory(),TARGET,give);
            if (killer.getGameMode().equals(GameMode.SURVIVAL)) killer.getWorld().dropItem(killer.getLocation(), Round.changeAmount(TARGET,give)).setPickupDelay(0);
            else killer.getInventory().addItem(Round.changeAmount(TARGET,give));
        }
    }
    private void target(Player deathEntity,Player killer,ArrayList<String> GIVE,Material item, String name) {
        Team t = Team.getEntry(deathEntity.getName());
        if (t == null) return;
        ItemStack TARGET = new ItemStack(item);
        int has = getItemCount(deathEntity,TARGET);
        int give = t.hasBed() ? (has / 2) : has;
        if (give != 0) {
            GIVE.add(name+"§r x"+give);
            removeItem(deathEntity.getInventory(),TARGET,give);
            if (killer.getGameMode().equals(GameMode.SURVIVAL)) killer.getWorld().dropItem(killer.getLocation(), Round.changeAmount(TARGET,give)).setPickupDelay(0);
            else killer.getInventory().addItem(Round.changeAmount(TARGET,give));
        }
    }
    private void removeItem(Inventory inv, ItemStack item, int amount) {
        ItemStack[] c = inv.getContents();
        for (int id = 0; id < c.length; id++) {
            if (amount == 0) return;
            ItemStack i = c[id];
            if (i == null) continue;
            if (!i.isSimilar(item)) continue;
            if (getCraftStatus(i) != getCraftStatus(item)) continue;
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
            if (getCraftStatus(r) != getCraftStatus(item)) continue;
            sum += r.getAmount();
        }
        return sum;
    }
    private boolean getCraftStatus(ItemStack i) {
        ItemNBTManager manager = new ItemNBTManager(i);
        String craft = manager.get("craft");
        if (craft == null || craft.equals("true")) return true;
        return false;
    }
}

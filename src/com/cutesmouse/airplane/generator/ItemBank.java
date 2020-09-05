package com.cutesmouse.airplane.generator;

import com.cutesmouse.airplane.tool.ItemNBTManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemBank {
    public static ItemStack ENCHANTED_IRON;
    public static ItemStack ENCHANTED_GOLD;
    public static ItemStack ENCHANTED_EMERALD;
    public static ItemStack ENCHANTED_DIAMOND;
    public static ItemStack MERCHANT_EGG;
    public static ItemStack UPGRADER_EGG;
    public static ItemStack DISABLED;
    public static void loadBank() {
        {
            ENCHANTED_IRON = noCraft(new ItemStack(Material.IRON_INGOT));
            ItemMeta meta = ENCHANTED_IRON.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName("§7附魔鐵錠");
            meta.setLore(new ArrayList<>(Arrays.asList("§6可在基地中與NPC交易的鐵錠", "§6但是 §c§l不可 §6當作一般鐵錠使用")));
            ENCHANTED_IRON.setItemMeta(meta);
        }
        {
            ENCHANTED_GOLD = noCraft(new ItemStack(Material.GOLD_INGOT));
            ItemMeta meta = ENCHANTED_GOLD.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName("§e附魔金錠");
            meta.setLore(new ArrayList<>(Arrays.asList("§6可在基地中與NPC交易的金錠", "§6但是 §c§l不可 §6當作一般黃金使用")));
            ENCHANTED_GOLD.setItemMeta(meta);
        }
        {
            ENCHANTED_EMERALD = noCraft(new ItemStack(Material.EMERALD));
            ItemMeta meta = ENCHANTED_EMERALD.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName("§a附魔綠寶石");
            meta.setLore(new ArrayList<>(Arrays.asList("§6可在基地中與NPC交易的綠寶石", "§a§l可 §6當作一般綠寶石使用")));
            ENCHANTED_EMERALD.setItemMeta(meta);
        }
        {
            ENCHANTED_DIAMOND = noCraft(new ItemStack(Material.DIAMOND));
            ItemMeta meta = ENCHANTED_DIAMOND.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName("§b附魔鑽石");
            meta.setLore(new ArrayList<>(Arrays.asList("§6可在基地中升級基地的鑽石", "§6但是 §c§l不可 §6當作一般鑽石使用")));
            ENCHANTED_DIAMOND.setItemMeta(meta);
        }
        {
            MERCHANT_EGG = noCraft(new ItemStack(Material.MONSTER_EGG));
            SpawnEggMeta meta = ((SpawnEggMeta) MERCHANT_EGG.getItemMeta());
            meta.setSpawnedType(EntityType.VILLAGER);
            meta.setDisplayName("§b流浪商人 生成蛋");
            meta.setLore(new ArrayList<>(Arrays.asList("§6放置後將可以用家中資源購買物資","§6此物品無法扔出",
                    "§6放置後不可取消、移動","§a✔ 只能放置在距離重生點10格內的地方",
                    "§c✘ 必須距離床5格以上",
                    "§c遺失將不會補發此物品")));
            MERCHANT_EGG.setItemMeta(meta);
            ItemNBTManager manager = new ItemNBTManager(MERCHANT_EGG);
            manager.modify("lockedWith","true");
            manager.modify("item_effect","merchant");
            MERCHANT_EGG = manager.getItemStack();
        }
        {
            UPGRADER_EGG = noCraft(new ItemStack(Material.MONSTER_EGG));
            SpawnEggMeta meta = ((SpawnEggMeta) UPGRADER_EGG.getItemMeta());
            meta.setSpawnedType(EntityType.VILLAGER);
            meta.setDisplayName("§b進級的鉅人 生成蛋");
            meta.setLore(new ArrayList<>(Arrays.asList("§6放置後將可以用家中資源升級設備","§6此物品無法扔出",
                    "§6放置後不可取消、移動","§a✔ 只能放置在距離重生點10格內的地方",
                    "§c✘ 必須距離床5格以上",
                    "§c遺失將不會補發此物品")));
            UPGRADER_EGG.setItemMeta(meta);
            ItemNBTManager manager = new ItemNBTManager(UPGRADER_EGG);
            manager.modify("lockedWith","true");
            manager.modify("item_effect","upgrader");
            UPGRADER_EGG = manager.getItemStack();
        }
        {
            DISABLED = new ItemStack(Material.BARRIER);
            ItemMeta meta = DISABLED.getItemMeta();
            meta.setDisplayName("§c敬請期待");
            DISABLED.setItemMeta(meta);
        }
    }
    private static ItemStack noCraft(ItemStack item) {
        ItemNBTManager manager = new ItemNBTManager(item);
        manager.modify("craft","false");
        return manager.getItemStack();
    }
    private ItemBank() {

    }
}

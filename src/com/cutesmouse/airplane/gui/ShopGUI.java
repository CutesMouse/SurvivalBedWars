package com.cutesmouse.airplane.gui;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.generator.ItemBank;
import com.cutesmouse.airplane.merchant.Item;
import com.cutesmouse.airplane.merchant.Type;
import com.cutesmouse.airplane.tool.ItemStackBuilder;
import com.cutesmouse.airplane.tool.PotionItemStackBuilder;
import com.cutesmouse.airplane.tool.Round;
import com.cutesmouse.mgui.GUI;
import com.cutesmouse.mgui.GUIItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ShopGUI {
    public static void OpenSHOP(Player p) {
        GUI gui = new GUI("§d◤§b§l飛機盃 §a玩家商店§d◢",3,p);
        /*
        武器
            木劍[鋒利三] <1金錠>
            石劍[鋒利二] <24鐵錠>
            鐵劍[鋒利一] <8金錠>
            鑽劍 <2綠寶石>
            擊退棒[擊退I] <1金錠>
         */
        for (int i = 0 ; i < 27 ; i++) gui.addItem(i,new GUIItem(Material.STAINED_GLASS_PANE,null,"§r",GUI.blank(),7));
        gui.addItem(10,new Type("武器", Material.GOLD_SWORD,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,1),new ItemStackBuilder(Material.WOOD_SWORD)
                .addEnchant(Enchantment.DAMAGE_ALL,3).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,24),new ItemStackBuilder(Material.STONE_SWORD)
                .addEnchant(Enchantment.DAMAGE_ALL,2).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,8),new ItemStackBuilder(Material.IRON_SWORD)
                .addEnchant(Enchantment.DAMAGE_ALL,1).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,2),new ItemStackBuilder(Material.DIAMOND_SWORD)
                .build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,12),new ItemStackBuilder(Material.STICK)
                .addEnchant(Enchantment.KNOCKBACK,1).build())
        ))).getGuiItem());
        /*
        工具
            煉冶石鎬 <1金錠>
            黑曜石殺手(只能用一次) <2綠寶石>
            鑽石斧 <1綠寶石>
            鑽石鎬 <1綠寶石>
            金斧[效率二] <8金錠>
            剪刀 <30鐵錠>
         */
        gui.addItem(11,new Type("工具", Material.STONE_PICKAXE,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,1),new ItemStackBuilder(Material.STONE_PICKAXE).checkLowestEnchant(p).withTag("item_ability","fur")
                        .withTag("craft","false")
                        .addEnchant(Enchantment.PROTECTION_FALL,1).setLore(new ArrayList<>(Arrays.asList("§7物品免燒","§6此物品無法進行修補"))).hideEnchant().build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,2),new ItemStackBuilder(Material.GOLD_PICKAXE)
                        .withTag("craft","false").hideEnchant().setName("§b黑曜石剋星").setLore(new ArrayList<>(Collections.singletonList("§6此物品無法進行修補")))
                        .addEnchant(Enchantment.DIG_SPEED,100).setDurability((short) 32).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,1),new ItemStackBuilder(Material.DIAMOND_AXE)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,1),new ItemStackBuilder(Material.DIAMOND_PICKAXE)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,30),new ItemStackBuilder(Material.SHEARS)
                        .checkLowestEnchant(p).build())
        ))).getGuiItem());
        /*
        弓箭
            弓 <12金錠>
            弓[強力I] <24金錠>
            弓[擊退I] <4綠寶石>
            箭矢x12 <4金錠>
         */
        gui.addItem(12,new Type("弓箭", Material.BOW,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,12),new ItemStackBuilder(Material.BOW)
                        .build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,24),new ItemStackBuilder(Material.BOW)
                        .addEnchant(Enchantment.ARROW_DAMAGE,1).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,12),new ItemStackBuilder(Material.BOW)
                        .addEnchant(Enchantment.ARROW_KNOCKBACK,1).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,4),new ItemStackBuilder(Material.ARROW)
                        .setAmount(12).build())
        ))).getGuiItem());
        /*
        防具
            皮革Helmet 5 / 8 / 7 / 5
            鎖鏈 5 / 8 / 7 / 5
         */
        gui.addItem(13,new Type("防具",Material.IRON_HELMET,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,5),new ItemStackBuilder(Material.LEATHER_HELMET)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,8),new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,7),new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,5),new ItemStackBuilder(Material.LEATHER_BOOTS)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,5),new ItemStackBuilder(Material.CHAINMAIL_HELMET)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,8),new ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,7),new ItemStackBuilder(Material.CHAINMAIL_LEGGINGS)
                        .checkLowestEnchant(p).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,5),new ItemStackBuilder(Material.CHAINMAIL_BOOTS)
                        .checkLowestEnchant(p).build())
        ))).getGuiItem());
        /*
        方塊
            羊毛x16 <4鐵錠>
            木材x16 <4金錠>
            終界石x12 <24鐵錠>
            防爆玻璃x4 <12鐵錠>
            黑曜石x4 <4綠寶石>
         */
        gui.addItem(14,new Type("方塊", Material.SANDSTONE,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,4),new ItemStackBuilder(Material.WOOL)
                        .setAmount(16).setDurability(getColorCode(p)).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,4),new ItemStackBuilder(Material.WOOD)
                        .setAmount(16).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,24),new ItemStackBuilder(Material.ENDER_STONE)
                        .setAmount(12).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,12),new ItemStackBuilder(Material.STAINED_GLASS)
                        .setName("§f防爆玻璃").setLore(new ArrayList<>(Arrays.asList("§6一般的染色玻璃沒有防爆效果","§6但可欺騙對手")))
                        .hideEnchant().addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,0)
                        .withTag("item_ability","exp_blacklist").withTag("craft","false")
                        .setAmount(4).setDurability(getColorCode(p)).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,4),new ItemStackBuilder(Material.OBSIDIAN)
                        .setAmount(4).build())
        ))).getGuiItem());
        /*
        藥水
            隱形藥水 <2綠寶石>
            跳躍藥水 <1綠寶石>
            速度藥水 <1綠寶石>
         */
        gui.addItem(15, new Type("藥水", Material.BREWING_STAND_ITEM,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,2),new PotionItemStackBuilder(Material.POTION)
                .setColor(Color.BLUE).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,30*20,0,
                                false,true)).hidePotionTags().withTag("item_ability","invisible")
                        .setName("§f隱形藥水").setLore(new ArrayList<>(Arrays.asList("§9隱形 (0:30)","§6會連同裝備一起隱形"
                        ,"§6但如果被玩家擊中，裝備就會現形"))).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,1),new PotionItemStackBuilder(Material.POTION)
                .setColor(Color.LIME).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,30*20,4,
                                false,true)).hidePotionTags()
                        .setName("§f跳躍藥水").setLore(new ArrayList<>(Collections.singletonList("§9跳躍提升 V (0:30)"))).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,1),new PotionItemStackBuilder(Material.POTION)
                .setColor(Color.AQUA).addPotionEffect(new PotionEffect(PotionEffectType.SPEED,30*20,1,
                                false,true)).hidePotionTags()
                        .setName("§f速度藥水").setLore(new ArrayList<>(Collections.singletonList("§9加速 II (0:30)"))).build())
        ))).getGuiItem());
        /*
        道具
            TNT <12金錠>
            界伏盒 <1綠寶石>
            終界珍珠 <4綠寶石>
            經驗瓶x16 <20鐵錠>
            橡木樹苗x1 <5鐵錠>
            書本x1 <1綠寶石>
         */
        gui.addItem(16,new Type("道具", Material.TNT,new ArrayList<>(Arrays.asList(
                new Item(Round.changeAmount(ItemBank.ENCHANTED_GOLD,12),new ItemStackBuilder(Material.TNT).withTag("craft","false")
                        .build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,4),new ItemStackBuilder(Material.ENDER_PEARL)
                        .build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,1),new ItemStackBuilder(getShulkerBox(p))
                        .build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,20),new ItemStackBuilder(Material.EXP_BOTTLE)
                        .setAmount(16).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_IRON,5),new ItemStackBuilder(Material.SAPLING)
                        .setLore(new ArrayList<>(Collections.singletonList("§c✘ 只能放置在生存世界中"))).build()),
                new Item(Round.changeAmount(ItemBank.ENCHANTED_EMERALD,1),new ItemStackBuilder(Material.BOOK)
                .build())
        ))).getGuiItem());
        p.openInventory(gui.getInv());
    }
    private static short getColorCode(Player p) {
        Team t = Team.getEntry(p.getName());
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
    private static Material getShulkerBox(Player p) {
        Team t = Team.getEntry(p.getName());
        if (t == null) return Material.WHITE_SHULKER_BOX;
        switch (t.getId()) {
            case 1:
                return Material.RED_SHULKER_BOX;
            case 2:
                return Material.YELLOW_SHULKER_BOX;
            case 3:
                return Material.BLUE_SHULKER_BOX;
            case 4:
                return Material.GREEN_SHULKER_BOX;
        }
        return Material.WHITE_SHULKER_BOX;
    }
}

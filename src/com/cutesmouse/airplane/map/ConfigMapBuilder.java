package com.cutesmouse.airplane.map;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.tool.ItemStackBuilder;
import com.cutesmouse.mgui.GUI;
import com.cutesmouse.mgui.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConfigMapBuilder {
    public enum InsertType {
        LOCATION,LOCATIONS,INT,TEAMS
    }
    public enum Options {
        DefaultLocation("DefaultLocation", "預設重生點",InsertType.LOCATION),
        BedHead("Teams.%d.BedHead", "隊伍核心(床頭)",InsertType.TEAMS),
        BedBack("Teams.%d.BedBack", "隊伍核心(床尾)",InsertType.TEAMS),
        Portal("Teams.%d.PortalBlock", "隊伍傳送門",InsertType.TEAMS),
        SpawnPoint("Teams.%d.SpawnPoint", "隊伍重生點",InsertType.TEAMS),
        Generator("Teams.%d.Generator", "隊伍資源生成器",InsertType.TEAMS),
        Diamond_Generators("DiamondGenerators", "公共鑽石生成器",InsertType.LOCATIONS),
        Emerald_Generators("EmeraldGenerators", "公共綠寶石生成器",InsertType.LOCATIONS),
        TeamProtectionDistance("TeamProtectionDistance", "隊伍核心保護距離",InsertType.INT),
        maxX("maxX", "地圖最大X值",InsertType.INT),
        maxY("maxY", "地圖最大Y值",InsertType.INT),
        maxZ("maxZ", "地圖最大Z值",InsertType.INT),
        minX("minX", "地圖最小X值",InsertType.INT),
        minY("minY", "地圖最小Y值",InsertType.INT),
        minZ("minZ", "地圖最小Z值",InsertType.INT);
        String conId;
        String description;
        InsertType type;
        Options(String conId, String description, InsertType type) {
            this.conId = conId;
            this.description = description;
            this.type = type;
        }
        public ItemStack getSetItem(Team t) {
            return new ItemStackBuilder(Material.WOOL).setLore(new ArrayList<>(Arrays.asList(
                    "§6設定 §e" + description,
                    "§6隊伍 §e" + (t == null ? "無" : "T" + t.getId()),
                    "",
                    "§b左鍵使用此物品破壞 >> §3指定該位置",
                    "§b右鍵使用此物品放置 >> §3指定該位置",
                    "§bQ鍵拋棄 >> §3指定玩家自身位置(包含仰角)"
            ))).withTag("map_setting", conId).withTag("team", (t == null ? "0" : t.getId())).setName("§b"+description).build();
        }
        public ItemStack getSetItem() {
            return new ItemStackBuilder(Material.WOOL).setLore(new ArrayList<>(Arrays.asList(
                    "§6設定 §e" + description,
                    "",
                    "§b左鍵使用此物品破壞 >> §3指定該位置",
                    "§b右鍵使用此物品放置 >> §3指定該位置",
                    "§bQ鍵拋棄 >> §3指定玩家自身位置(包含仰角)"
            ))).withTag("map_setting", conId).setName("§a"+description).build();
        }
        public ItemStack getSetItem(int pos) {
            return new ItemStackBuilder(Material.WOOL).setLore(new ArrayList<>(Arrays.asList(
                    "§6設定 §e" + description,
                    "§6編號 §e"+pos,
                    "",
                    "§b左鍵使用此物品破壞 >> §3指定該位置",
                    "§b右鍵使用此物品放置 >> §3指定該位置",
                    "§bQ鍵拋棄 >> §3指定玩家自身位置(包含仰角)"
            ))).withTag("map_setting", conId).withTag("pos", pos).setName("§6"+description+" "+pos).build();
        }
    }
    private HashMap<Options,Object> SETS = new HashMap<>();
    private int diamond_generators = 4;
    private int emerald_generators = 2;
    private String name;
    public ConfigMapBuilder(String name) {
        this.name = name;
    }
    public void OpenSettingInventory(Player p) {
        GUI gui = new GUI("§d◤§b§l飛機盃 §a地圖設定§d◢",2,p);
        gui.addItem(0,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+diamond_generators,"§7(MAX 8)"),
                "§c鑽石生成器數量",GUI.signText("範圍 0~8",t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    diamond_generators = Integer.parseInt(t.getLines()[0]);
                    OpenSettingInventory(p);
        })));
        gui.addItem(1,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+emerald_generators,"§7(MAX 8)"),
                "§c綠寶石生成器數量",GUI.signText("範圍 0~4",t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    emerald_generators = Integer.parseInt(t.getLines()[0]);
                    OpenSettingInventory(p);
        })));
        gui.addItem(2,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.TeamProtectionDistance,6),"§7(以重生點為中心)"),
                "§c隊伍核心保護距離",GUI.signText("範圍 正整數或0",t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.TeamProtectionDistance,Integer.parseInt(R));
                    OpenSettingInventory(p);
        })));
        gui.addItem(8,new GUIItem(Material.GOLD_BLOCK,null,"§a座標設定工具", (e,i) -> {
            e.setCancelled(true);
            Inventory inv = Bukkit.createInventory(null,54,"§d◤§b§l飛機盃 §a座標設定工具§d◢");
            for (Options option : Options.values()) {
                switch (option.type) {
                    case TEAMS:
                        for (Team t : Team.TEAMS) {
                            inv.addItem(option.getSetItem(t));
                        }
                        break;
                    case LOCATIONS:
                        for (int pos = 0; pos < (option.equals(Options.Diamond_Generators) ? diamond_generators : emerald_generators) ; pos++) {
                            inv.addItem(option.getSetItem(pos+1));
                        }
                        break;
                    case LOCATION:
                        inv.addItem(option.getSetItem());
                        break;
                }
            }
            e.getWhoClicked().openInventory(inv);
        }));
        gui.addItem(9,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.maxX,130)),
                "§c玩家活動最大X",GUI.signText("範圍 "+(SETS.containsKey(Options.minX) ? "> "+SETS.get(Options.minX) : "無"),t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+") && !R.matches("-\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    int r = Integer.parseInt(R);
                    if (SETS.containsKey(Options.minX) && r <= Integer.parseInt(SETS.get(Options.minX).toString())) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.maxX,r);
                    OpenSettingInventory(p);
        })));
        gui.addItem(10,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.maxY,130)),
                "§c玩家活動最大Y",GUI.signText("範圍 "+(SETS.containsKey(Options.minY) ? "> "+SETS.get(Options.minY) : "無"),t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+") && !R.matches("-\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    int r = Integer.parseInt(R);
                    if (SETS.containsKey(Options.minY) && r <= Integer.parseInt(SETS.get(Options.minY).toString())) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.maxY,r);
                    OpenSettingInventory(p);
        })));
        gui.addItem(11,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.maxZ,130)),
                "§c玩家活動最大Z",GUI.signText("範圍 "+(SETS.containsKey(Options.minZ) ? "> "+SETS.get(Options.minZ) : "無"),t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+") && !R.matches("-\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    int r = Integer.parseInt(R);
                    if (SETS.containsKey(Options.minZ) && r <= Integer.parseInt(SETS.get(Options.minZ).toString())) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.maxZ,r);
                    OpenSettingInventory(p);
        })));
        gui.addItem(12,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.minX,-130)),
                "§c玩家活動最小X",GUI.signText("範圍 "+(SETS.containsKey(Options.maxX) ? "< "+SETS.get(Options.maxX) : "無"),t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+") && !R.matches("-\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    int r = Integer.parseInt(R);
                    if (SETS.containsKey(Options.maxX) && r >= Integer.parseInt(SETS.get(Options.maxX).toString())) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.minX,r);
                    OpenSettingInventory(p);
        })));
        gui.addItem(13,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.minY,75)),
                "§c玩家活動最小Y",GUI.signText("範圍 "+(SETS.containsKey(Options.maxY) ? "< "+SETS.get(Options.maxY) : "無"),t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+") && !R.matches("-\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    int r = Integer.parseInt(R);
                    if (SETS.containsKey(Options.maxY) && r >= Integer.parseInt(SETS.get(Options.maxY).toString())) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.minY,r);
                    OpenSettingInventory(p);
        })));
        gui.addItem(14,new GUIItem(Material.GOLD_BLOCK,Arrays.asList("§6目前設定 §e"+SETS.getOrDefault(Options.minZ,-130)),
                "§c玩家活動最小Z",GUI.signText("範圍 "+(SETS.containsKey(Options.maxZ) ? "< "+SETS.get(Options.maxZ) : "無"),t-> {
                    String R = t.getLines()[0];
                    if (!R.matches("\\d+") && !R.matches("-\\d+")) {
                        OpenSettingInventory(p);
                        return;
                    }
                    int r = Integer.parseInt(R);
                    if (SETS.containsKey(Options.maxZ) && r >= Integer.parseInt(SETS.get(Options.maxZ).toString())) {
                        OpenSettingInventory(p);
                        return;
                    }
                    SETS.put(Options.minZ,r);
                    OpenSettingInventory(p);
        })));
        p.openInventory(gui.getInv());
    }
}

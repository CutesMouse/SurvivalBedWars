package com.cutesmouse.airplane.generator;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.tool.Round;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmeraldGenerator implements Generator {
    private ArmorStand Armor;
    private ArmorStand Title;
    private Location loc;
    private int level;
    private int EFFICIENT;
    private int TICKPAST;
    public EmeraldGenerator(Location loc) {
        level = 1;
        EFFICIENT = 120 * 20;
        this.loc = loc;
        ArrayList<Entity> entities = loc.getWorld().getNearbyEntities(loc, 1, 1, 1).stream()
                .filter(e -> e.getType().equals(EntityType.ARMOR_STAND) && e.getScoreboardTags().contains("emeraldGenerator"))
                .collect(Collectors.toCollection(ArrayList::new));
        if (entities.size() > 0) Armor = ((ArmorStand) entities.get(0));
        else {
            Armor = ((ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND));
        }
        Armor.setInvulnerable(true);
        Armor.setGravity(false);
        Armor.setVisible(false);
        Armor.setMarker(true);
        Armor.setCustomName("§e下一顆綠寶石將在 §a- §e秒後出現");
        Armor.setSmall(true);
        Armor.setCustomNameVisible(true);
        Armor.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
        Armor.addScoreboardTag("emeraldGenerator");

        ArrayList<Entity> entities2 = loc.getWorld().getNearbyEntities(loc, 1, 2, 1).stream()
                .filter(e -> e.getType().equals(EntityType.ARMOR_STAND) && e.getScoreboardTags().contains("emeraldGenerator_Title"))
                .collect(Collectors.toCollection(ArrayList::new));
        if (entities2.size() > 0) Title = ((ArmorStand) entities2.get(0));
        else {
            Title = ((ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.ARMOR_STAND));
        }
        Title.setInvulnerable(true);
        Title.setGravity(false);
        Title.setVisible(false);
        Title.setMarker(true);
        Title.setCustomName("§6綠寶石生成器 - 等級"+ Round.Rome(level));
        Title.setSmall(true);
        Title.setCustomNameVisible(true);
        Title.addScoreboardTag("emeraldGenerator_Title");
    }

    @Override
    public void upgrade() {
        if (level == 4) return;
        level++;
        switch (level) {
            case 2:
                EFFICIENT = 70*20;
                break;
            case 3:
                EFFICIENT = 45*20;
                break;
            case 4:
                EFFICIENT = 20*20;
                break;
        }
        Title.setCustomName("§6綠寶石生成器 - 等級"+ Round.Rome(level));
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int nextLVCost() {
        return 0;
    }

    @Override
    public List<String> getIntro() {
        return null;
    }

    @Override
    public Team getTeam() {
        return null;
    }

    @Override
    public int getEfficient() {
        return EFFICIENT;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public String getItemName() {
        return "綠寶石生成器";
    }

    @Override
    public Location getLoc() {
        return loc;
    }

    @Override
    public void generate() {
        TICKPAST = 0;
        loc.getWorld().dropItemNaturally(loc,new ItemStack(ItemBank.ENCHANTED_EMERALD)).addScoreboardTag("emerald");
    }


    @Override
    public void spin() {
        TICKPAST+= BedWars.INSTANCE.TimeSpeed();
        if (TICKPAST >= EFFICIENT) generate();
        Armor.setCustomName("§e下一顆綠寶石將在 §a"+((EFFICIENT/20) - (TICKPAST/20))+" §e秒後出現");
        Location location = Armor.getLocation().clone();
        location.setYaw(location.getYaw()+5);
        Armor.teleport(location);
    }
}

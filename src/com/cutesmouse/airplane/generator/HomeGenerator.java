package com.cutesmouse.airplane.generator;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeGenerator implements Generator {
    private Location loc;
    private int level;
    private int EMERALD;
    private int IRON;
    private int TICKPAST;
    private Team t;
    public HomeGenerator(Location loc, Team t) {
        level = 1;
        IRON = 0;
        this.t = t;
        this.loc = loc;
    }

    @Override
    public void upgrade() {
        if (level == 5) return;
        level++;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int nextLVCost() {
        switch (level) {
            case 1:
                return 4;
            case 2:
                return 8;
            case 3:
                return 16;
        }
        return 0;
    }

    @Override
    public List<String> getIntro() {
        ArrayList<String> lores = new ArrayList<>(Arrays.asList(
                "§a增加自家熔爐的產量",
                "§a生成4個§7附魔鐵錠§a➡生成1個§e附魔金錠",
                "§a生成16個§e附魔金錠§a➡生成1個§a附魔綠寶石"));
        lores.add("§a✔ §7附魔鐵錠");
        lores.add("§a✔ §e附魔金錠");
        lores.add((level == 1 ? "§c✘ §a附魔綠寶石" : (level == 2 ? "§c✘ §a附魔綠寶石 §d➲ §a✔" :
                "§a✔ §a附魔綠寶石")));
        if (level == getMaxLevel()) {
            lores.add("§7當前狀態: "+(getEfficient()/20) +"秒生成一個附魔鐵錠 §d(MAX)");
            return lores;
        }
        lores.add("§7當前狀態: "+(getEfficient() /20) +"秒生成一個附魔鐵錠 §d("+(getEfficient()/20) +" ➲ "+(int) (getEfficient()*0.8/20)+")");
        return lores;
    }

    @Override
    public Team getTeam() {
        return t;
    }

    @Override
    public int getEfficient() {
        switch (level) {
            case 1:
                return 20*6;
            case 2:
                return 20*4;
            case 3:
                return 20*3;
            case 4:
                return 20*2;
        }
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public String getItemName() {
        return "熔爐";
    }

    @Override
    public Location getLoc() {
        return loc;
    }

    @Override
    public void generate() {
        TICKPAST = 0;
        IRON++;
        if (IRON >= 4) {
            loc.getWorld().dropItemNaturally(loc, new ItemStack(ItemBank.ENCHANTED_GOLD)).addScoreboardTag("gold_ingot");
            IRON = 0;
            EMERALD++;
        }
        if (EMERALD >= 16 && level >= 3) {
            loc.getWorld().dropItemNaturally(loc, new ItemStack(ItemBank.ENCHANTED_EMERALD)).addScoreboardTag("emerald");
            EMERALD = 0;
        }
        loc.getWorld().dropItemNaturally(loc,new ItemStack(ItemBank.ENCHANTED_IRON)).addScoreboardTag("iron_ingot");
    }


    @Override
    public void spin() {
        TICKPAST+= BedWars.INSTANCE.TimeSpeed();
        if (TICKPAST >= getEfficient()) generate();
    }
}

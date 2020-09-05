package com.cutesmouse.airplane.upgrades;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.merchant.Upgradeable;
import com.cutesmouse.airplane.tool.Round;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolsEfficiencyUpgrade implements Upgradeable {
    private int level;
    private final Team TEAM;
    public ToolsEfficiencyUpgrade(Team t) {
        this.TEAM = t;
        level = 0;
    }
    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getItemName() {
        return "工具效率";
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void upgrade() {
        level++;
        TEAM.setEfficiencyLevel(level);
    }

    @Override
    public int nextLVCost() {
        return 8+8*level;
    }

    @Override
    public List<String> getIntro() {
        return new ArrayList<>(Arrays.asList("§a升級工具的初始效率附魔","§a將會立即更新你的物品欄及之後合成的物品","§a在箱子的物品將不會被更新到",
                "§7當前狀態: "+(level == 0 ? "無" : Round.Rome(level))+"§d ("+(getMaxLevel() == level ?
                        "MAX" : (level == 0 ? "無" : Round.Rome(level))+" ➲ "+Round.Rome(level + 1))+")"));
    }

    @Override
    public Team getTeam() {
        return TEAM;
    }
}

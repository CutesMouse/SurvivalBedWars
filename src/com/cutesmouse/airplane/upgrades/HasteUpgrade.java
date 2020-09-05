package com.cutesmouse.airplane.upgrades;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.merchant.Upgradeable;
import com.cutesmouse.airplane.tool.Round;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HasteUpgrade implements Upgradeable {
    private Team TEAM;
    private int level;
    public HasteUpgrade(Team t) {
        this.TEAM = t;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getItemName() {
        return "挖掘加速";
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void upgrade() {
        level++;
        TEAM.membersForeach(p -> p.getActivePotionEffects().forEach(i -> p.removePotionEffect(i.getType())));
    }

    @Override
    public int nextLVCost() {
        return 2*(level+1);
    }

    @Override
    public List<String> getIntro() {
        ArrayList<String> lores = new ArrayList<>(Collections.singletonList("§a提高自身挖掘速度"));
        if (level == getMaxLevel()) {
            lores.add("§7當前狀態: 挖掘加速 II §d(MAX)");
            return lores;
        }
        if (level == 0) {
            lores.add("§7當前狀態: 無  §d➲ 挖掘加速 I");
            return lores;
        }
        lores.add("§7當前狀態: 挖掘加速 "+ Round.Rome(level)+ " §d("+ Round.Rome(level) +" ➲ "+Round.Rome(level+1)+")");
        return lores;
    }

    @Override
    public Team getTeam() {
        return TEAM;
    }
}

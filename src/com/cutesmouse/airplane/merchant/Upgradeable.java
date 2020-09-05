package com.cutesmouse.airplane.merchant;

import com.cutesmouse.airplane.Team;

import java.util.List;

public interface Upgradeable {
    int getMaxLevel();
    String getItemName();
    int getLevel();
    void upgrade();
    int nextLVCost();
    List<String> getIntro();
    Team getTeam();
}

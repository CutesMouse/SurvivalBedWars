package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.BedWars;

public class BoardShrinkEvent extends Event {

    @Override
    public void toggleTask() {
        BedWars.DEFAULT.getWorldBorder().setSize(150);
        BedWars.DEFAULT.getWorldBorder().setSize(20D,10*60L);
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a即將漸縮邊界至20x20! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "漸縮邊界至10x10";
    }

    @Override
    public String ToggleText() {
        return "§c邊界已開始收縮!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return 20*60*80;
    }
}

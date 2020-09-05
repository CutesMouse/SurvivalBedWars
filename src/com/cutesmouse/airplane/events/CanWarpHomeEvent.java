package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.commands.Home;

public class CanWarpHomeEvent extends Event {
    private int toggleTick;
    public CanWarpHomeEvent() {
        toggleTick = 20*5*60;
    }

    @Override
    public void toggleTask() {
        Home.CanWarpHome = true;
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a即將開放各隊玩家輸入 §b/home (/i) §a回島! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "開放回島";
    }

    @Override
    public String ToggleText() {
        return "§d現在開始大家可以回家建造基地囉!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return toggleTick;
    }

}

package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;

public class BreakAllCoresEvent extends Event {
    @Override
    public void toggleTask() {
        for (Team t : Team.TEAMS) {
            if (t.hasBed()) BedWars.INSTANCE.breakBed(null,t,null);
            t.close();
        }
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a即將摧毀所有隊伍的床! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "摧毀全隊核心";
    }

    @Override
    public String ToggleText() {
        return "§c已摧毀所有隊伍的核心!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return 20*60*60;
    }
}

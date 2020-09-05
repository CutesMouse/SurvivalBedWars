package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;

public class TeamBlockSurvivalWorldEvent extends Event {
    private Team t;
    private int current;
    public TeamBlockSurvivalWorldEvent(Team t, int currentTime) {
        this.t = t;
        current = currentTime;
    }
    @Override
    public void toggleTask() {
        t.setT_canWarpSurvival(false);
        t.membersForeach(p -> {
            if (!p.getWorld().equals(BedWars.DEFAULT)) p.teleport(t.getSpawnpoint());
        });
    }

    public Team getTeam() {
        return t;
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a禁止 "+t.getColor()+"T"+t.getId()+" §a前往生存世界! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "關閉生存世界 - "+t.getColor()+"T"+t.getId();
    }

    @Override
    public String ToggleText() {
        return "§a已關閉 "+t.getColor()+"T"+t.getId()+" §a的生存世界!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return current + 20*120;
    }
}

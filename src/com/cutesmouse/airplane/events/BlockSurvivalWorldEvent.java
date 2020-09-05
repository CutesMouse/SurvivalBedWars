package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;

public class BlockSurvivalWorldEvent extends Event {
    @Override
    public void toggleTask() {
        for (Team t : Team.TEAMS) {
            t.membersForeach(p -> {
                if (p.getLocation().getWorld().equals(BedWars.DEFAULT)) return;
                p.teleport(t.getSpawnpoint());
            });
        }
        BedWars.INSTANCE.disableSurvivalWorld();
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a即將關閉各隊專屬生存世界! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "關閉生存世界";
    }

    @Override
    public String ToggleText() {
        return "§c已傳送所有在生存世界的玩家! 現在已經無法前往生存世界!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return 20*60*50;
    }
}

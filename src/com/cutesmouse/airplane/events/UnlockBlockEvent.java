package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.permission.BreakingManager;

public class UnlockBlockEvent extends Event {
    @Override
    public void toggleTask() {
        BreakingManager.byPass = true;
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a即將解鎖所有方塊放置/破壞限制! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "解除方塊放置/破壞限制";
    }

    @Override
    public String ToggleText() {
        return "§d現在大家已經可以無條件拆除/爆破/放置所有方塊!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return 20*60*65;
    }
}

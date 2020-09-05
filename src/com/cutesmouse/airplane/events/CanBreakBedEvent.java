package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.BedWars;

public class CanBreakBedEvent extends Event {
    @Override
    public void toggleTask() {
        BedWars.INSTANCE.ableBreakBed();
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a即將開放各隊拆床! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "開放拆床";
    }

    @Override
    public String ToggleText() {
        return "§d現在開始大家可以拆床囉!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return 20*60*30;
    }
}

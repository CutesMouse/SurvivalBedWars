package com.cutesmouse.airplane.events;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.generator.DiamondGenerator;
import com.cutesmouse.airplane.generator.EmeraldGenerator;
import com.cutesmouse.airplane.tool.Round;

public class PublicGeneratorUpgradeEvent extends Event {
    private int level; // 此為當前等級
    public PublicGeneratorUpgradeEvent(int level) {
        this.level = level;
    }

    @Override
    public void toggleTask() {
        for (EmeraldGenerator emerald : BedWars.EMERALD_GENERATORS) {
            emerald.upgrade();
        }
        for (DiamondGenerator diamond : BedWars.DIAMOND_GENERATORS) {
            diamond.spin();
        }
    }

    @Override
    public String getTimerName(int secTillToggle) {
        return String.format("§a鑽石和綠寶石生成器升級! 還有 §d%d§a 秒", secTillToggle);
    }

    @Override
    public String getSidebarName() {
        return "公共資源生成器升級";
    }

    @Override
    public String ToggleText() {
        return "§3鑽石和綠寶石生成器已升級至 §b"+ Round.Rome(level+1) +" §3級!";
    }

    @Override
    public int ToggleTimeInTicks() {
        return 5*60*20+level*60*10*20;
    }

    @Override
    public boolean needTimer() {
        return false;
    }
}

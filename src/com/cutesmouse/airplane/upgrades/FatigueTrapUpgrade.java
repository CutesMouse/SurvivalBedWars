package com.cutesmouse.airplane.upgrades;

import com.cutesmouse.airplane.Team;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FatigueTrapUpgrade extends TrapUpgrade {
    private Team t;
    public FatigueTrapUpgrade(Team t) {
        this.t = t;
        enabled = false;
    }
    private boolean enabled;

    @Override
    public Material getDisplay() {
        return Material.IRON_SPADE;
    }

    @Override
    public PotionEffect getEffect() {
        return new PotionEffect(PotionEffectType.SLOW_DIGGING,20*4,1,
                false,false);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getItemName() {
        return "挖掘緩速陷阱";
    }

    @Override
    public int nextLVCost() {
        return 1;
    }

    @Override
    public List<String> getIntro() {
        return new ArrayList<>(Arrays.asList("§a當偵測到敵人距離自家核心8格以內","§a將會給予該敵人4秒的挖掘緩速 II效果",
                "§7當前狀態: "+(isEnabled() ? "開 §d(MAX)" : "關 §d(關 ➲ 開)")));
    }

    @Override
    public Team getTeam() {
        return t;
    }
}

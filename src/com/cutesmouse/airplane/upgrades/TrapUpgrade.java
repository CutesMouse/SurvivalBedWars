package com.cutesmouse.airplane.upgrades;

import com.cutesmouse.airplane.merchant.Upgradeable;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public abstract class TrapUpgrade implements Upgradeable {
    @Override
    public int getMaxLevel() {
        return 1;
    }

    public abstract Material getDisplay();

    @Override
    public int getLevel() {
        return (isEnabled() ? 1 : 0);
    }

    @Override
    public void upgrade() {
        setEnabled(true);
    }

    public void toggle(Player p) {
        getTeam().membersForeach(t -> {
            t.playSound(t.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT,1F,1F);
            t.sendMessage("§c您的"+getItemName()+"已經被觸發！");
            t.sendTitle("§c陷阱觸發","",5,30,5);
        });
        p.addPotionEffect(getEffect(),true);
        setEnabled(false);
    }
    public abstract PotionEffect getEffect();
    public abstract boolean isEnabled();
    public abstract void setEnabled(boolean enabled);
}

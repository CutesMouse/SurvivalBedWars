package com.cutesmouse.airplane.tool;

import org.bukkit.Location;

public class FacingArrow {
    private Location p;
    private Location t;
    private double distance;
    public FacingArrow(Location player, Location target) {
        p = player;
        t = target;
    }
    private double getAngle() {
        double px = -p.getX();
        double pz = p.getZ();
        double tx = -t.getX() - 0.5D;
        double tz = t.getZ() + 0.5D;
        double dx = Math.abs(px - tx);
        double dz = Math.abs(pz - tz);
        distance = Round.distance(dx,dz);
        if (distance <= 10) return 10000;
        if (px == tx) return 0;
        double atan = Math.atan((pz - tz) / (px - tx));
        if (px > tx) {
            return 180+Round.toDeg(atan);
        }
        else if (px < tx && pz > tz) {
            return 360 + Round.toDeg(atan);
        }
        else {
            return Round.toDeg(atan);
        }
        //return 0;
    }
    public String getArrow() {
        if (!p.getWorld().equals(t.getWorld())) {
            return "★";
        }
        double angle = getAngle() - Round.toMCAngle(p.getYaw());
        if (angle > 180) angle -= 360;
        if (angle < -180) angle += 360;
        return getSymbol(angle);
    }
    private String getSymbol(double angle) {
        /*
         ⬆ 22.5 ~ -22.5
         ⬇ all
         ⬅ 67.5 ~ 112.5
         ➡ -67.5 ~ -112.5
         ⬉ 22.5 ~ 67.5
         ⬈ -22.5 ~ -67.5
         ⬊ -112.5 ~ -157.5
         ⬋ 112.5 ~ 157.5
         */
        if (angle >= 1000) return "● 您的所在位置";
        String distance = Round.round(this.distance, 1);
        if (angle < 22.5 && angle > -22.5) return "⬆";
        if (angle >= 22.5 && angle <= 67.5) return "⬉";
        if (angle > 67.5 && angle < 112.5) return "⬅";
        if (angle >= 112.5 && angle <= 157.5) return "⬋";

        if (angle <= -22.5 && angle >= -67.5) return "⬈";
        if (angle < -67.5 && angle > -112.5) return "➡";
        if (angle <= -112.5 && angle >= -157.5) return "⬊";
        return "⬇";
    }
}

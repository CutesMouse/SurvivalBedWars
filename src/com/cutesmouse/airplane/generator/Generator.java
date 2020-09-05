package com.cutesmouse.airplane.generator;

import com.cutesmouse.airplane.merchant.Upgradeable;
import org.bukkit.Location;

public interface Generator extends Upgradeable {
    void generate();
    void spin();
    int getEfficient();
    Location getLoc();
}

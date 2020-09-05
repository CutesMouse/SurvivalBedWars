package com.cutesmouse.airplane.tool;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class PotionItemStackBuilder extends ItemStackBuilder {
    public PotionItemStackBuilder(Material m) {
        super(m);
        PotionEffects = new ArrayList<>();
    }

    private ArrayList<PotionEffect> PotionEffects;
    private Color color;
    private boolean potionTags = false;
    public PotionItemStackBuilder addPotionEffect(PotionEffect p) {
        PotionEffects.add(p);
        return this;
    }
    public PotionItemStackBuilder setColor(Color color) {
        this.color = color;
        return this;
    }
    public PotionItemStackBuilder hidePotionTags() {
        potionTags = true;
        return this;
    }


    @Override
    public ItemStack build() {
        ItemStack build = super.build();
        PotionMeta meta = ((PotionMeta) build.getItemMeta());
        PotionEffects.forEach(p -> meta.addCustomEffect(p,true));
        if (color != null) meta.setColor(color);
        if (potionTags) meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        build.setItemMeta(meta);
        return build;
    }
}

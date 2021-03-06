package com.cutesmouse.airplane.merchant;

import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.npc.PlayerNPC;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;

public class Upgrader {
    public static ArrayList<NPC> UPGRADERS = new ArrayList<>();
    public static final String NAME = "§a進級的鉅人";
    public static void Register(Location loc, Player p) {
        /*
        Villager villager = ((Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER));
        // 外觀改變
        villager.setGravity(false);
        villager.setProfession(Villager.Profession.LIBRARIAN);
        villager.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
        villager.setInvulnerable(true);
        villager.setCustomName("§a進級的鉅人");
        villager.setCustomNameVisible(true);
        villager.setSilent(true);
        //
        UPGRADERS.add(villager);
        */
        PlayerNPC pn = new PlayerNPC("§a進級的鉅人");
        pn.spawnAt(loc);
        Team team = Team.getEntry(p.getName());
        if (team != null) {
            Player firstEntry = team.getFirstEntry();
            if (firstEntry != null)
                pn.setSkinName(firstEntry.getName());
        }
        pn.lookAt(p);
        UPGRADERS.add(pn.getNpc());
    }
}

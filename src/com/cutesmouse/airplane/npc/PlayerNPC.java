package com.cutesmouse.airplane.npc;

import com.cutesmouse.airplane.tool.NPCManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerNPC {
    private String name;
    private Location loc;
    private String skinName;
    private NPC npc;
    public PlayerNPC(String name) {
        if (NPCManager.NPCS == null) NPCManager.NPCS = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        this.name = name;
        this.npc = NPCManager.NPCS.createNPC(EntityType.PLAYER,name);
        NPCManager.register(this);
    }
    public void spawnAt(Location loc) {
        this.loc = loc;
        this.npc.spawn(loc);
    }
    public void lookAt(Player player) {
        this.npc.faceLocation(player.getLocation());
        this.loc = npc.getStoredLocation();
    }
    public void setSkinName(String skinName) {
        // Need to spawn first
        if (loc == null) return;
        SkinnableEntity se = ((SkinnableEntity) npc.getEntity());
        this.skinName = skinName;
        se.setSkinName(skinName,true);
    }

    public NPC getNpc() {
        return npc;
    }

    public void despawn() {
        this.npc.despawn();
    }
    public void respawn() {
        if (loc == null) return;
        this.spawnAt(loc);
    }
}

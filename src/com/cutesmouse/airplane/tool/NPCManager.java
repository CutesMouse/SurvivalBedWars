package com.cutesmouse.airplane.tool;

import com.cutesmouse.airplane.npc.PlayerNPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import java.util.ArrayList;

public class NPCManager {
    public static NPCRegistry NPCS;
    private final static ArrayList<PlayerNPC> NPC = new ArrayList<>();
    public static void register(PlayerNPC npc) {
        NPC.add(npc);
    }
    public static void removeNPCS() {
        if (NPCS == null) return;
        NPCS.deregisterAll();
    }
    public static void update() {
        for (PlayerNPC npc : NPC) {
            npc.despawn();
            npc.respawn();
        }
    }
}

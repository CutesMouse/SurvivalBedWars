package com.cutesmouse.airplane.tool;

import com.cutesmouse.airplane.Team;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.NetworkManager;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorManager {
    private Player p;
    private Team t;
    public ArmorManager(Player p) {
        this.p = p;
        t = Team.getEntry(p.getName());
    }
    public void hideToOthers() {
        PacketPlayOutEntityEquipment Head = new PacketPlayOutEntityEquipment(p.getEntityId(),
                EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment Chest = new PacketPlayOutEntityEquipment(p.getEntityId(),
                EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment Legs = new PacketPlayOutEntityEquipment(p.getEntityId(),
                EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment Feet = new PacketPlayOutEntityEquipment(p.getEntityId(),
                EnumItemSlot.FEET, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.equals(p)) continue;
            if (t != null && t.equals(Team.getEntry(players.getName()))) continue;
            NetworkManager connect = ((CraftPlayer) players).getHandle().playerConnection.networkManager;
            connect.sendPacket(Head);
            connect.sendPacket(Chest);
            connect.sendPacket(Legs);
            connect.sendPacket(Feet);
        }
    }
}

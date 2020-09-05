package com.cutesmouse.airplane.gmListener;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.tool.NPCManager;
import com.cutesmouse.airplane.gui.TeamSelectorGUI;
import com.cutesmouse.airplane.tool.ItemNBTManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class BeforeGamePlayerListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Team t = Team.getEntry(e.getPlayer().getName());
        e.setFormat((t == null ? "[無] " : "["+t.getColor()+"T"+t.getId()+"§r] ")+e.getPlayer().getName()+"§7: §r"+e.getMessage());
    }
    @EventHandler
    public void onPlayerDestroy(BlockBreakEvent e) {
        if (e.getPlayer().isOp()) return;
        if (!BedWars.INSTANCE.hasStarted()) e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e) {
        if (e.getPlayer().isOp()) return;
        if (!BedWars.INSTANCE.hasStarted()) e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) {
            if (!e.getEntityType().equals(EntityType.PLAYER)) return;
            e.setCancelled(true);
            if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                e.getEntity().teleport(BedWars.MAP.getDefaultLocation());
            }
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) {
            if (Math.abs(e.getFrom().getX()) > 130) {
                e.setTo(BedWars.MAP.getDefaultLocation());
                e.getPlayer().sendMessage("§c您無法飛出此邊界!");
            }
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        NPCManager.update();
        if (!BedWars.INSTANCE.hasStarted()) {
            e.getPlayer().teleport(BedWars.MAP.getDefaultLocation());
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().setAllowFlight(true);
            if (!TeamSelectorGUI.STATUS) return;
            Team t = Team.getEntry(e.getPlayer().getName());
            if (t == null) {
                if (e.getPlayer().isOp()) {
                    e.getPlayer().sendMessage("§c系統提醒: 您還沒有分配到隊伍");
                    return;
                }
                t = TeamSelectorGUI.getJoinTeam();
                t.appendPlayer(e.getPlayer().getName());
                e.getPlayer().sendMessage("§a系統已將您自動分配至隊伍 "+t.getDisplayName());
                e.getPlayer().sendMessage("§a如要轉隊 可使用 /team 找尋可用隊伍!");
            }
        }
    }
    @EventHandler
    public void onInventoryClicked(InventoryClickEvent e) {
        if (BedWars.INSTANCE.hasStarted()) return;
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().equals(TeamSelectorGUI.TeamSelector)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            ItemStack item = e.getCurrentItem();
            if (!ItemNBTManager.tagEqualsTo(item,"invClickEvent","setTeam")) return;
            int teamId = Integer.parseInt(ItemNBTManager.getTag(item,"targetTeam"));
            if (teamId == 0) return;
            Team t = Team.TEAMS[teamId - 1];
            t.appendPlayer(e.getWhoClicked().getName());
            e.getWhoClicked().sendMessage("§a已加入隊伍 "+t.getDisplayName());
            TeamSelectorGUI.OpenTeamSelector(((Player) e.getWhoClicked()));
        }
    }
}

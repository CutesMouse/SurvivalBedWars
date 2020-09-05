package com.cutesmouse.airplane.gmListener;

import com.cutesmouse.airplane.BedWars;
import com.cutesmouse.airplane.Main;
import com.cutesmouse.airplane.Team;
import com.cutesmouse.airplane.merchant.Merchant;
import com.cutesmouse.airplane.gui.ShopGUI;
import com.cutesmouse.airplane.gui.UpgradeGUI;
import com.cutesmouse.airplane.merchant.Upgrader;
import com.cutesmouse.airplane.permission.BreakingManager;
import com.cutesmouse.airplane.permission.CanWarpSurvivalManager;
import com.cutesmouse.airplane.permission.ExplosionManager;
import com.cutesmouse.airplane.permission.MerchantPlacer;
import com.cutesmouse.airplane.tool.ArmorManager;
import com.cutesmouse.airplane.tool.ItemNBTManager;
import com.cutesmouse.airplane.tool.ItemStackBuilder;
import com.cutesmouse.airplane.tool.Round;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;

public class GameplayListener implements Listener {
    private static class DamageData {
        private long time;
        private String damager;
        private DamageData(long time, String damager) {
            this.time = time;
            this.damager = damager;
        }
    }
    private static HashMap<String,DamageData> DAMAGETABLE = new HashMap<>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        Team team = Team.getEntry(e.getEntity().getName());
        if (team != null && !team.hasBed()) {
            e.setDeathMessage(e.getDeathMessage()+" §c§lFINAL KILL!");
        }
        e.getEntity().spigot().respawn();
        death(e.getEntity());
    }
    @EventHandler
    public void lastAttack(EntityDamageByEntityEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (!e.getEntity().getType().equals(EntityType.PLAYER)) return;
        if (e.getDamager().getType().equals(EntityType.ARROW) || e.getDamager().getType().equals(EntityType.SPECTRAL_ARROW) || e.getDamager().getType().equals(EntityType.TIPPED_ARROW)) {
            if (!(((Arrow) e.getDamager()).getShooter() instanceof Player)) return;
            if (((Player) e.getEntity()).getHealth() > 0) ((Player) ((Arrow) e.getDamager()).getShooter()).sendMessage("§a"+e.getEntity().getName()+" §7現在有 §c"+ Round.round(((Player) e.getEntity()).getHealth(),1)+" §7HP");
            DAMAGETABLE.put(e.getEntity().getName(),new DamageData(System.currentTimeMillis(),((Player) ((Arrow) e.getDamager()).getShooter()).getName()));
            return;
        }
        if (!e.getDamager().getType().equals(EntityType.PLAYER)) return;
        DAMAGETABLE.put(e.getEntity().getName(),new DamageData(System.currentTimeMillis(),e.getDamager().getName()));
    }
    @EventHandler
    public void onPlayerDroppedIntoVoid(EntityDamageEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (!e.getEntityType().equals(EntityType.PLAYER)) return;
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) return;
        death(((Player) e.getEntity()));
        Team team = Team.getEntry(e.getEntity().getName());
        DamageData data = DAMAGETABLE.get(e.getEntity().getName());
        if (data == null || System.currentTimeMillis() - data.time > 15000L) Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(e.getEntity().getName()+" 掉到世界外面了" + ((team != null && !team.hasBed()) ?
                " §c§lFINAL KILL!" : "")));
        else Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(e.getEntity().getName()+" 被 "+data.damager+" 打入虛空" + ((team != null && !team.hasBed()) ?
                " §c§lFINAL KILL!" : "")));
    }
    @EventHandler
    public void onPlayerEnteredNether(PortalCreateEvent e) {
        e.setCancelled(true);
    }
    private void death(Player p) {
        p.setGameMode(GameMode.SPECTATOR);
        if (DAMAGETABLE.containsKey(p.getName())) {
            DamageData data = DAMAGETABLE.get(p.getName());
            if ((System.currentTimeMillis() - data.time) <= 15000L) {
                Player killer = Bukkit.getPlayer(data.damager);
                if (killer != null) new DeathItemMove(p,killer);
            }
        }
        p.getActivePotionEffects().forEach(pot -> p.removePotionEffect(pot.getType()));
        p.setHealth(20D);
        p.teleport(BedWars.MAP.getDefaultLocation());
        Team team = Team.getEntry(p.getName());
        if (team == null) return;
        if (!team.hasBed()) {
            p.sendMessage("§c抱歉，您已遭淘汰!");
            p.sendMessage("§c如對死亡有任何疑慮，請私訊管理員!");
            if (team.getAlivePeople() == 0) BedWars.INSTANCE.ace(team);
            return;
        }
        new BukkitRunnable() {
            int left = 5;
            @Override
            public void run() {
                left--;
                if (left == -1) {
                    this.cancel();
                    p.setGameMode(GameMode.SURVIVAL);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,5*20,255,true));
                    p.teleport(team.getSpawnpoint());
                    return;
                }
                p.sendTitle("§c您已經死亡!","§c您將在 §e"+left+" §c秒後復活!",0,30,0);
            }
        }.runTaskTimer(Main.getProvidingPlugin(Main.class),0L,20L);
    }
    @EventHandler
    public void onPortalEntered(PlayerToggleSneakEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (!CanWarpSurvivalManager.canIWarpSurvival(e.getPlayer())) return;
        if (!e.getPlayer().isSneaking()) return;
        Team t = Team.getEntry(e.getPlayer().getName());
        if (t == null) return;
        if (!Round.sameLocation(e.getPlayer().getLocation(),t.getPortal())) return;
        e.getPlayer().teleport(t.getSurvivalSpawnpoint());
    }

    @EventHandler
    public void onItemDamaged(PlayerItemDamageEvent e) {
        switch (e.getItem().getType()) {
            case DIAMOND_AXE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SPADE:
            case DIAMOND_SWORD:
            case DIAMOND_HOE:
                e.setDamage(e.getDamage()*4);
                break;
            case GOLD_AXE:
            case GOLD_PICKAXE:
            case GOLD_SPADE:
            case GOLD_SWORD:
            case GOLD_HOE:
            case WOOD_AXE:
            case WOOD_PICKAXE:
            case WOOD_SPADE:
            case WOOD_SWORD:
            case WOOD_HOE:
            case IRON_AXE:
            case IRON_PICKAXE:
            case IRON_SPADE:
            case IRON_SWORD:
            case IRON_HOE:
            case STONE_AXE:
            case STONE_PICKAXE:
            case STONE_SPADE:
            case STONE_SWORD:
            case STONE_HOE:
                e.setDamage(e.getDamage()*3);
                break;
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case SHIELD:
            case FISHING_ROD:
                e.setDamage(e.getDamage()*2);
                break;
        }
    }

    @EventHandler
    public void onEnderPearled(PlayerTeleportEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) return;
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT,1F,1F));
    }

    @EventHandler
    public void onTNTPut(EntityExplodeEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        e.blockList().removeIf(p -> !ExplosionManager.canIExplode(p.getLocation()));
    }

    @EventHandler
    public void onPlayerDrinkPotion(PlayerItemConsumeEvent e) {
        if (e.getItem() == null) return;
        if (ItemNBTManager.tagEqualsTo(e.getItem(),"item_ability","invisible")) {
            e.setCancelled(true);
            e.getPlayer().getInventory().setItemInMainHand(null);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,30*20,0,false,true));
            new ArmorManager(e.getPlayer()).hideToOthers();
        }
    }

    public void tickEvent() {
        if (!BedWars.INSTANCE.hasStarted()) return;
        BedWars.INSTANCE.past();
        for (Team t : Team.TEAMS) {
            if (t.getUpgradeHaste().getLevel() > 0) {
                t.membersForeach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,
                        Integer.MAX_VALUE,t.getUpgradeHaste().getLevel()-1,true)));
            }
        }
        BedWars.spin();
    }
    @EventHandler
    public void onPistonActivate(BlockPistonExtendEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void itemStack(ItemMergeEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (e.getEntity().getScoreboardTags().contains("diamond") && e.getTarget().getScoreboardTags().contains("diamond")) {
            if (e.getEntity().getItemStack().getAmount() >= 8) {
                e.getTarget().remove();
                e.setCancelled(true);
            }
        }
        if (e.getEntity().getScoreboardTags().contains("emerald") && e.getTarget().getScoreboardTags().contains("emerald")) {
            if (e.getEntity().getItemStack().getAmount() >= 2) {
                e.getTarget().remove();
                e.setCancelled(true);
            }
        }
        if (e.getEntity().getScoreboardTags().contains("iron_ingot") && e.getTarget().getScoreboardTags().contains("iron_ingot")) {
            if (e.getEntity().getItemStack().getAmount() >= 48) {
                e.getTarget().remove();
                e.setCancelled(true);
            }
        }
        if (e.getEntity().getScoreboardTags().contains("gold_ingot") && e.getTarget().getScoreboardTags().contains("gold_ingot")) {
            if (e.getEntity().getItemStack().getAmount() >= 16) {
                e.getTarget().remove();
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (e.getBlock().getType().equals(Material.BED_BLOCK)) {
            e.setDropItems(false);
            Team broken = Team.getByBedLocation(e.getBlock().getLocation());
            if (broken == null) {
                return;
            }
            Team t = Team.getEntry(e.getPlayer().getName());
            if (t == null) {
                e.getPlayer().sendMessage("§c你沒有任何隊伍! 無法破壞床");
                e.setCancelled(true);
                return;
            }
            if (broken.equals(t)) {
                e.getPlayer().sendMessage("§c你無法破壞自己的床!");
                e.setCancelled(true);
                return;
            }
            if (!BedWars.INSTANCE.canBreakBed()) {
                e.getPlayer().sendMessage("§c要30分鐘後才能拆床喔!");
                e.setCancelled(true);
                return;
            }
            BedWars.INSTANCE.breakBed(e.getPlayer(),broken,t);
            return;
        }
        if (!e.getBlock().getLocation().getWorld().equals(BedWars.DEFAULT)) {
            if (e.getBlock() == null) return;
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item == null) return;
            if (!ItemNBTManager.tagEqualsTo(item,"item_ability","fur")) return;
            switch (e.getBlock().getType()) {
                case IRON_ORE:
                    e.setDropItems(false);
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5,0.5,0.5),new ItemStack(Material.IRON_INGOT));
                    ((ExperienceOrb) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation().clone().add(0.5,0.5,0.5), EntityType.EXPERIENCE_ORB)).setExperience(1);
                    break;
                case GOLD_ORE:
                    e.setDropItems(false);
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5,0.5,0.5),new ItemStack(Material.GOLD_INGOT));
                    ((ExperienceOrb) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation().clone().add(0.5,0.5,0.5), EntityType.EXPERIENCE_ORB)).setExperience(2);
                    break;
            }
        }
        if (!BreakingManager.canIBreak(e.getPlayer(),e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c你不能破壞此處的方塊!");
        }
    }
    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (!BreakingManager.canIPlace(e.getPlayer(),e.getBlock(),e.getBlockPlaced())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c你不能在此處放置方塊!");
        } else {
            if (e.getBlockPlaced().getType().equals(Material.TNT)) {
                e.getBlockPlaced().setType(Material.AIR);
                ((TNTPrimed) e.getBlockPlaced().getWorld().spawnEntity(e.getBlockPlaced().getLocation().clone().add(0.5,0,0.5),
                        EntityType.PRIMED_TNT)).setFuseTicks(20*4);
                return;
            }
            if (e.getItemInHand() != null && ItemNBTManager.tagEqualsTo(e.getItemInHand(),
                    "item_ability","exp_blacklist")) {
                ExplosionManager.EXPLODE_BLACKLIST.add(e.getBlockPlaced().getLocation());
            }
            BreakingManager.BLOCKS.add(e.getBlock().getLocation());
        }
    }
    @EventHandler
    public void onPlayerAnviled(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getType() == null) return;
        if (!e.getClickedInventory().getType().equals(InventoryType.ANVIL)) return;
        if (e.getSlot() != 2) return;
        AnvilInventory ai = (AnvilInventory) e.getClickedInventory();
        if (ai.getItem(2) == null) return;
        ItemStack i1 = ai.getItem(0);
        ItemStack i2 = ai.getItem(1);
        if (i1 == null || i2 == null) return;
        if (ItemNBTManager.tagEqualsTo(i1,"craft","false") || ItemNBTManager.tagEqualsTo(i2,"craft","false")) {
            e.getWhoClicked().sendMessage("§c您的合成原料中有禁止合成標籤!");
            e.setCancelled(true);
        }

    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (item == null) return;
        ItemNBTManager manager = new ItemNBTManager(item);
        String locked = manager.get("lockedWith");
        if (locked == null) return;
        if (locked.equals("true")) e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerClicked(PlayerInteractEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.CHEST)) {
            if (BreakingManager.isTeamOwningArea(e.getClickedBlock().getLocation())) {
                Team t = Team.getEntry(e.getPlayer().getName());
                if (!BreakingManager.getTeamOwningArea(e.getClickedBlock().getLocation()).equals(t)) {
                    e.getPlayer().sendMessage("§c此箱子受到保護!");
                    e.setCancelled(true);
                }
            }
        }
        ItemStack item = e.getItem();
        if (item == null) return;
        ItemNBTManager manager = new ItemNBTManager(item);
        String effect = manager.get("item_effect");
        if (effect == null) return;
        switch (effect) {
            case "merchant":
                e.setCancelled(true);
                if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
                if (MerchantPlacer.canIPlaceMerchant(e.getPlayer(), e.getClickedBlock().getLocation())) {
                    Merchant.Register(e.getClickedBlock().getLocation().add(0.5,1,0.5),e.getPlayer());
                    e.getPlayer().getInventory().setItemInMainHand(null);
                }
                break;
            case "upgrader":
                e.setCancelled(true);
                if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
                if (MerchantPlacer.canIPlaceMerchant(e.getPlayer(), e.getClickedBlock().getLocation())) {
                    Upgrader.Register(e.getClickedBlock().getLocation().add(0.5,1,0.5),e.getPlayer());
                    e.getPlayer().getInventory().setItemInMainHand(null);
                }
                break;
        }
    }
    /*
    @EventHandler
    public void onPlayerClickedEntity(PlayerInteractEntityEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        Entity clicked = e.getRightClicked();
        if (clicked == null) return;
        if (!clicked.getType().equals(EntityType.VILLAGER)) return;
        if (Merchant.MERCHANTS.contains(clicked)) {
            ShopGUI.OpenSHOP(e.getPlayer());
            e.setCancelled(true);
        }
        if (Upgrader.UPGRADERS.contains(clicked)) {
            UpgradeGUI.OpenUpgrade(e.getPlayer());
            e.setCancelled(true);
        }
    }
     */
    @EventHandler
    public void onPlayerClickedNPC(NPCRightClickEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        NPC clicked = e.getNPC();
        if (clicked == null) return;
        if (clicked.getName().equals(Merchant.NAME)) {
            ShopGUI.OpenSHOP(e.getClicker());
        }
        if (clicked.getName().equals(Upgrader.NAME)) {
            UpgradeGUI.OpenUpgrade(e.getClicker());
        }
    }
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        if (Arrays.stream(e.getInventory().getMatrix()).anyMatch(r -> {
            if (r == null) return false;
            String noCraft = new ItemNBTManager(r).get("craft");
            if (noCraft != null && noCraft.equals("false")) return true;
            return false;
        })) {
            e.getWhoClicked().sendMessage("§c您的合成原料中有禁止合成標籤!");
            e.setCancelled(true);
            return;
        }
        if (e.getRecipe().getResult().getType().equals(Material.BED)) {
            e.getWhoClicked().sendMessage("§c您無法在遊戲中合成床!");
            e.setCancelled(true);
        }
        if (Round.isArmor(e.getCurrentItem().getType())) {
            Team t = Team.getEntry(e.getWhoClicked().getName());
            if (t == null) return;
            if (t.getProtectionLevel() == 0) return;
            e.setCurrentItem(new ItemStackBuilder(e.getCurrentItem().getType()).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,t.getProtectionLevel()).build());
        }
        if (Round.isTools(e.getCurrentItem().getType())) {
            Team t = Team.getEntry(e.getWhoClicked().getName());
            if (t == null) return;
            if (t.getEfficiencyLevel() == 0) return;
            e.setCurrentItem(new ItemStackBuilder(e.getCurrentItem().getType()).addEnchant(Enchantment.DIG_SPEED,t.getEfficiencyLevel()).build());
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!BedWars.INSTANCE.hasStarted()) return;
        Team t = Team.getEntry(e.getPlayer().getName());
        if (t == null) return;
        for (Team teams : Team.TEAMS) {
            if (teams.equals(t)) continue;
            if (!Round.similarLocation(teams.getBedLocation(), e.getTo(), 8)) continue;
            teams.getTraps().forEach(trap -> {
                if (trap.isEnabled()) trap.toggle(e.getPlayer());
            });
            return;
        }
    }
}

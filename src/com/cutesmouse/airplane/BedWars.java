package com.cutesmouse.airplane;

import com.cutesmouse.airplane.events.*;
import com.cutesmouse.airplane.generator.DiamondGenerator;
import com.cutesmouse.airplane.generator.EmeraldGenerator;
import com.cutesmouse.airplane.generator.HomeGenerator;
import com.cutesmouse.airplane.generator.ItemBank;
import com.cutesmouse.airplane.map.DefaultMap;
import com.cutesmouse.airplane.map.Map;
import com.cutesmouse.airplane.tool.FacingArrow;
import com.cutesmouse.airplane.tool.ObjectiveData;
import com.cutesmouse.airplane.tool.Round;
import com.cutesmouse.airplane.tool.ScoreboardManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

public class BedWars {
    public static BedWars INSTANCE;
    static {
        INSTANCE = new BedWars();
    }
    public static final World DEFAULT = Bukkit.getWorld("world");
    public static Map MAP;
    public static DiamondGenerator[] DIAMOND_GENERATORS;
    public static EmeraldGenerator[] EMERALD_GENERATORS;
    public static World T1_WORLD;
    public static World T2_WORLD;
    public static World T3_WORLD;
    public static World T4_WORLD;
    public static Location T1_SURVIVAL;
    public static Location T2_SURVIVAL;
    public static Location T3_SURVIVAL;
    public static Location T4_SURVIVAL;

    private boolean started;
    private BedWars() {
        started = false;
        canBreakBed = false;
        canWarpSurvivalWorld = true;
        EVENTS = new LinkedList<>();
    }
    public boolean hasStarted() {
        return started;
    }
    public static void loadEntities() {
        DIAMOND_GENERATORS = Arrays.stream(MAP.getDiamondGenerators()).map(DiamondGenerator::new).toArray(DiamondGenerator[]::new);
        EMERALD_GENERATORS = Arrays.stream(MAP.getEmeraldGenerators()).map(EmeraldGenerator::new).toArray(EmeraldGenerator[]::new);
        DEFAULT.getEntitiesByClasses(Item.class).forEach(Entity::remove);
    }

    public static void spin() {
        for (DiamondGenerator diamond : DIAMOND_GENERATORS) {
            diamond.spin();
        }
        for (EmeraldGenerator emerald : EMERALD_GENERATORS) {
            emerald.spin();
        }
        for (Team t : Team.TEAMS) {
            t.getHomeGenerator().spin();
        }
    }
    public static void loadPrivateWorld() {
        T1_WORLD = Bukkit.createWorld(new WorldCreator("T1_WORLD").seed(new Random().nextLong()));
        T2_WORLD = Bukkit.createWorld(new WorldCreator("T2_WORLD").seed(new Random().nextLong()));
        T3_WORLD = Bukkit.createWorld(new WorldCreator("T3_WORLD").seed(new Random().nextLong()));
        T4_WORLD = Bukkit.createWorld(new WorldCreator("T4_WORLD").seed(new Random().nextLong()));
        T1_WORLD.getWorldBorder().setSize(300);
        T1_WORLD.getWorldBorder().setCenter(0,0);
        T2_WORLD.getWorldBorder().setSize(300);
        T2_WORLD.getWorldBorder().setCenter(0,0);
        T3_WORLD.getWorldBorder().setSize(300);
        T3_WORLD.getWorldBorder().setCenter(0,0);
        T4_WORLD.getWorldBorder().setSize(300);
        T4_WORLD.getWorldBorder().setCenter(0,0);
        DEFAULT.getWorldBorder().setSize(100000);
        loadWorldSettings(T1_WORLD);
        loadWorldSettings(T2_WORLD);
        loadWorldSettings(T3_WORLD);
        loadWorldSettings(T4_WORLD);
        loadWorldSettings(DEFAULT);
        DEFAULT.setPVP(true);
        T1_SURVIVAL = new Location(T1_WORLD,0,T1_WORLD.getHighestBlockYAt(0,0)+1,0);
        T2_SURVIVAL = new Location(T2_WORLD,0,T2_WORLD.getHighestBlockYAt(0,0)+1,0);
        T3_SURVIVAL = new Location(T3_WORLD,0,T3_WORLD.getHighestBlockYAt(0,0)+1,0);
        T4_SURVIVAL = new Location(T4_WORLD,0,T4_WORLD.getHighestBlockYAt(0,0)+1,0);
    }
    private int SPEED = 1;
    public int TimeSpeed() {
        return SPEED;
    }
    public void setSpeed(int speed) {
        this.SPEED = speed;
    }
    private int tick_duration;
    private boolean canBreakBed;
    public void start(Player starter) {
        if (Arrays.stream(Team.TEAMS).noneMatch(r -> r.getAlivePeople() > 0)) {
            starter.sendMessage("§c沒有任何隊伍中有玩家! 無法開始!");
            return;
        }
        String[] intro = new String[]{"§9Go!","§4Ready?","§dYou","§aAre","","","","","","",""};
        new BukkitRunnable() {
            int left = 10;
            @Override
            public void run() {
                if (left == 0) {
                    for (Entity items : DEFAULT.getEntitiesByClasses(Item.class)) {
                        items.remove();
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getGameMode().equals(GameMode.SURVIVAL)) p.setAllowFlight(false);
                        p.getInventory().clear();
                        p.getInventory().setHelmet(null);
                        p.getInventory().setChestplate(null);
                        p.getInventory().setLeggings(null);
                        p.getInventory().setBoots(null);
                        p.getInventory().setItemInOffHand(null);
                        p.closeInventory();
                        p.setFallDistance(0);
                        p.setFireTicks(0);
                        p.getActivePotionEffects().forEach(o -> p.removePotionEffect(o.getType()));
                        p.setHealth(20.0);
                        p.sendTitle(intro[left],"§e開始",5,10,5);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING,1F,1F);
                    }
                    for (Team t : Team.TEAMS) {
                        if (t.getAlivePeople() == 0) {
                            t.close();
                            continue;
                        }
                        t.membersForeach(p -> p.setGameMode(GameMode.SURVIVAL));
                        t.teleportAll(t.getSurvivalSpawnpoint());
                        Player egg_holder = t.getFirstEntry();
                        egg_holder.getInventory().setItem(8, ItemBank.MERCHANT_EGG);
                        egg_holder.getInventory().setItem(7, ItemBank.UPGRADER_EGG);
                    }
                    initEvents();
                    initScoreboard();
                    started = true;
                    this.cancel();
                    return;
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(intro[left],"§a遊戲準備開始，剩下 §e"+left+" §a秒",5,10,5);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT,1F,1F);
                }
                left--;
            }
        }.runTaskTimer(Main.getProvidingPlugin(Main.class),0L,20L);
        //started = true;
    }

    public void disableSurvivalWorld() {
        canWarpSurvivalWorld = false;
    }

    public boolean canWarpSurvivalWorld() {
        return canWarpSurvivalWorld;
    }

    public boolean canBreakBed() {
        return canBreakBed;
    }

    public void ableBreakBed() {
        canBreakBed = true;
    }

    public int getTick_duration() {
        return tick_duration;
    }
    public void setTime(int tick) {
        tick_duration = tick;
    }
    public void past() {
        tick_duration+=SPEED;
        if (tick_duration % 20 != 0) return;
        for (Event e : EVENTS) {
            if (!e.hadTimered() && e.shouldTimer(tick_duration)) {
                if (!e.needTimer()) continue;
                new BukkitRunnable() {
                    int left = 10 / SPEED;
                    @Override
                    public void run() {
                        if (left == 0) {
                            this.cancel();
                            return;
                        }
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 1F, 1F);
                            p.sendMessage(e.getTimerName(left));
                        });
                        left--;
                    }
                }.runTaskTimer(Main.getProvidingPlugin(Main.class),0L,20L);
            }
            if (!e.shouldToggle(tick_duration)) continue;
            e.toggleTask();
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING,1F,1F);
                p.sendMessage(e.ToggleText());
            });
        }
        EVENTS.removeIf(Event::hadToggled);
    }
    private boolean canWarpSurvivalWorld;
    private LinkedList<Event> EVENTS;
    private void initEvents() {
        EVENTS.add(new CanBreakBedEvent());
        EVENTS.add(new UnlockBlockEvent());
        EVENTS.add(new CanWarpHomeEvent());
        EVENTS.add(new PublicGeneratorUpgradeEvent(1));
        EVENTS.add(new PublicGeneratorUpgradeEvent(2));
        EVENTS.add(new PublicGeneratorUpgradeEvent(3));
        EVENTS.add(new BlockSurvivalWorldEvent());
        EVENTS.add(new BreakAllCoresEvent());
        EVENTS.add(new BoardShrinkEvent());
        EVENTS.sort(Comparator.comparing(Event::ToggleTimeInTicks));
    }
    private void initScoreboard() {
        ObjectiveData data = new ObjectiveData();
        int pointer = 1;
        data.set(pointer--,s -> "§6遊戲時間: §e"+ Round.formedTime(tick_duration / 20));
        data.set(pointer--,s -> teamInfoSidebar(s,Team.TEAM01));
        data.set(pointer--,s -> teamInfoSidebar(s,Team.TEAM02));
        data.set(pointer--,s -> teamInfoSidebar(s,Team.TEAM03));
        data.set(pointer--,s -> teamInfoSidebar(s,Team.TEAM04));
        data.set(pointer--, s-> {
            Team t = Team.getEntry(s.getName());
            return "§6所在隊伍: §e"+(t == null ? "無" : t.getColor()+"T"+t.getId());
        });
        data.set(pointer--,s -> "§2⋯⋯⋯⋯⋯事件列表一覽⋯⋯⋯⋯⋯");
        data.set(pointer--,s -> "§e- §6"+(EVENTS.size() >= 1 ? Round.formedTime(EVENTS.get(0).ToggleTimeInTicks()/20)+" §e"+EVENTS.get(0).getSidebarName() : "§1"));
        data.set(pointer--,s -> "§e- §6"+(EVENTS.size() >= 2 ? Round.formedTime(EVENTS.get(1).ToggleTimeInTicks()/20)+" §e"+EVENTS.get(1).getSidebarName() : "§2"));
        data.set(pointer--,s -> "§e- §6"+(EVENTS.size() >= 3 ? Round.formedTime(EVENTS.get(2).ToggleTimeInTicks()/20)+" §e"+EVENTS.get(2).getSidebarName() : "§3"));
        data.set(pointer--,s -> "§2⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
        data.set(pointer-1,s -> "§7"+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        ScoreboardManager.INSTANCE.setObjective_Data(data);
        ScoreboardManager.INSTANCE.reloadSidebarData();
    }
    private String teamInfoSidebar(Player p, Team t) {
        return t.getColor()+"§lT"+t.getId()+"§7: "+(t.hasBed() ? "§a✔ §7("+new FacingArrow(p.getLocation(),t.getBedLocation()).getArrow()+")" : (t.getAlivePeople() > 0 ? "§e"+t.getAlivePeople()+" §7("+ new FacingArrow(p.getLocation(),t.getBedLocation()).getArrow()+")" : "§c✘"));
    }
    public void breakBed(Player p, Team beBroken,Team breaker) {
        /*
        §7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯
        §6➢ §b§l飛機盃 §a「生存床戰」
        <隊伍> 的床已經被 <玩家> 破壞
        該隊伍(你)將無法再重生!
        ⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯
         */
        if (canWarpSurvivalWorld && !new BlockSurvivalWorldEvent().shouldToggle(tick_duration + 20*120)) {
            EVENTS.add(new TeamBlockSurvivalWorldEvent(beBroken,tick_duration));
            EVENTS.sort(Comparator.comparing(Event::ToggleTimeInTicks));
            beBroken.membersForeach(pl -> pl.sendMessage("§c您的專屬生存世界將在兩分鐘後關閉！"));
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            Team current = Team.getEntry(player.getName());
            if (beBroken.equals(current)) player.sendTitle("§c床已被破壞","§c你將無法再復活!",10,40,10);
            if (p == null || breaker == null) {
                player.sendMessage("§7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
                player.sendMessage("§6➢ §b§l飛機盃 §a「生存床戰」");
                player.sendMessage(beBroken.getDisplayName()+" §6的床已經被 §2§l系統 §6破壞!");
                player.sendMessage("§c"+(beBroken.equals(current) ? "你" : "該隊伍")+"將無法再重生!");
                player.sendMessage("§7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
                player.playSound(player.getLocation(), (beBroken.equals(current) ? Sound.ENTITY_WITHER_DEATH : Sound.ENTITY_ENDERDRAGON_AMBIENT),1F,1F);
                continue;
            }
            player.sendMessage("§7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
            player.sendMessage("§6➢ §b§l飛機盃 §a「生存床戰」");
            player.sendMessage(beBroken.getDisplayName()+" §6的床已經被 "+breaker.getColor()+"§l"+p.getName()+" §6破壞!");
            player.sendMessage("§c"+(beBroken.equals(current) ? "你" : "該隊伍")+"將無法再重生!");
            player.sendMessage("§7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
            player.playSound(player.getLocation(), (beBroken.equals(current) ? Sound.ENTITY_WITHER_DEATH : Sound.ENTITY_ENDERDRAGON_AMBIENT),1F,1F);
        }
    }
    public void ace(Team t) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
            player.sendMessage("§6➢ §b§l飛機盃 §a「生存床戰」");
            player.sendMessage(t.getDisplayName()+" §6已全員淘汰!");
            player.sendMessage("§7⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯⋯");
        }
    }
    private static void loadWorldSettings(World w) {
        w.setGameRuleValue("doDaylightCycle","false");
        w.setGameRuleValue("doWeatherCycle","false");
        w.setGameRuleValue("keepInventory","true");
        w.setGameRuleValue("naturalRegeneration","false");
        w.setGameRuleValue("doFireTick","false");
        w.setGameRuleValue("announceAdvancements","false");
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setTime(6000L);
        w.setPVP(false);
        w.setThundering(false);
        w.setStorm(false);
    }
}

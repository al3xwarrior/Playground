package com.al3x.housing2;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.Actions.ExplosionAction;
import com.al3x.housing2.Action.Actions.ParticleAction;
import com.al3x.housing2.Action.ParentActionExecutor;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.LaunchPad;
import com.al3x.housing2.Listeners.LobbyListener;
import com.al3x.housing2.MineSkin.SkinData;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Instances.HousingScoreboard;
import com.al3x.housing2.Utils.tablist.HousingTabList;
import com.google.gson.Gson;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.al3x.housing2.Instances.Hologram.rainbow;
import static com.al3x.housing2.Instances.Hologram.rainbowIndex;
import static com.al3x.housing2.MineSkin.MineskinHandler.sendRequestForSkins;
import static com.al3x.housing2.Utils.Color.colorize;

public class Runnables {
    //Description, Runnable
    private static HashMap<String, BukkitTask> runnables = new HashMap<>();
    public static ConcurrentHashMap<UUID, List<Entity>> entityMap = new ConcurrentHashMap<>();
    private static Gson gson = new Gson();

    public static void startRunnables(Main main) {
        runnables.put("unloadIfEmpty", new BukkitRunnable() {
            @Override
            public void run() {
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    if (house.getWorld().getPlayers().isEmpty()) {
                        main.getHousesManager().saveHouseAndUnload(house);
                    }
                }
            }
        }.runTaskTimer(main, 0, 20 * 5L));

        runnables.put("cacheNearbyEntities", new BukkitRunnable() {
            @Override
            public void run() {
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    for (Player player : house.getWorld().getPlayers()) {
                        List<Entity> entities = new ArrayList<>();
                        for (Entity entity : player.getNearbyEntities(50, 50, 50)) {
                            if (!(entity instanceof LivingEntity)) continue;
                            entities.add(entity);
                        }
                        entityMap.put(player.getUniqueId(), entities);
                    }
                }
            }
        }.runTaskTimer(main, 0, 5));

        runnables.put("despawnProjectiles", new BukkitRunnable() {
            @Override
            public void run() {
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    house.getWorld().getEntities().forEach(entity -> {
                        if (entity.hasMetadata("projectile")) {
                            entity.remove();
                        }
                    });
                }
            }
        }.runTaskTimer(main, 0, 20 * 5L));

        runnables.put("updateThings", new BukkitRunnable() {
            @Override
            public void run() {
                //Update tablist
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    house.getWorld().getPlayers().forEach(player -> HousingTabList.setTabList(player, house));
                }
            }
        }.runTaskTimer(main, 0, 20L));

        runnables.put("updateCooldowns", new BukkitRunnable() {
            @Override
            public void run() {
                ParticleAction.particlesCooldownMap.clear();
                ExplosionAction.amountDone.clear();
            }
        }.runTaskTimer(main, 0, 40L));

        runnables.put("saveAll", new BukkitRunnable() {
            @Override
            public void run() {
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    house.save();
                }
            }
        }.runTaskTimerAsynchronously(main, 0L, 20 * 60L));

        //Run function actions
        runnables.put("runFunctionActions", new BukkitRunnable() {
            public static int TICKS = 0;

            @Override
            public void run() {
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    if (house == null || house.getFunctions() == null) continue;
                    house.getFunctions().forEach(function -> {
                        if (function.getTicks() != null && TICKS % function.getTicks() == 0) {
                            Bukkit.getScheduler().runTask(main, () -> function.execute(main, null, house, true, null));
                        }
                    });
                }
                TICKS++;
            }
        }.runTaskTimerAsynchronously(main, 0L, 1L));

        //Start loading npc skins
        runnables.put("loadNPCSkins", new BukkitRunnable() {
            int page = 1;

            @Override
            public void run() {
                PaginationList<SkinData> skins = new PaginationList<>(HousingNPC.loadedSkins, 21);
                List<SkinData> currentSkins = skins.getPage(page);
                if (currentSkins == null) {
                    List<SkinData> lastPage = skins.getPage(page - 1);
                    if (lastPage != null) {
                        sendRequestForSkins(lastPage.getLast().getUuid());
                    }

                    if (page == 1) {
                        sendRequestForSkins(null);
                    }
                } else {
                    page++;
                }
            }
        }.runTaskTimerAsynchronously(main, 0L, 20L)); // Every 10 ticks load a new page of skins

        runnables.put("protoolsParticles", new BukkitRunnable() {
            @Override
            public void run() {
                for (HousingWorld house : main.getHousesManager().getConcurrentLoadedHouses().values()) {
                    for (Player player : house.getWorld().getPlayers()) {
                        Duple<Location, Location> selection = main.getProtoolsManager().getSelection(player);
                        if (selection != null) {
                            main.getProtoolsManager().drawParticles(player, selection.getFirst(), selection.getSecond());
                        }
                    }
                }
            }
        }.runTaskTimer(main, 0L, 5L));

        runnables.put("updateHolograms", new BukkitRunnable() {
            @Override
            public void run() {
                for (HousingWorld house : main.getHousesManager().getConcurrentLoadedHouses().values()) {
                    for (Hologram holo : house.getHolograms()) {
                        holo.updateHologramEntity();
                    }
                    for (HousingNPC npc : house.getNPCs()) {
                        npc.getHologram().updateHologramEntity();
                    }
                    rainbowIndex = rainbowIndex + 16;
                    if (rainbowIndex >= rainbow.size()) {
                        rainbowIndex = 0;
                    }
                }
            }
        }.runTaskTimer(main, 0L, 5L)); // might lower this. Right now it matches housings 2 second refresh. I will do it for you Al3x - Sin_ender <3

        runnables.put("checkRegion", new BukkitRunnable() {
            @Override
            public void run() {
                for (HousingWorld house : main.getHousesManager().getConcurrentLoadedHouses().values()) {
                    for (Player player : house.getWorld().getPlayers()) {
                        house.getRegions().forEach(region -> {
                            if (!region.isLoaded()) return;
                            double maxX = Math.max(region.getFirst().getX(), region.getSecond().getX());
                            double maxY = Math.max(region.getFirst().getY(), region.getSecond().getY());
                            double maxZ = Math.max(region.getFirst().getZ(), region.getSecond().getZ());
                            double minX = Math.min(region.getFirst().getX(), region.getSecond().getX());
                            double minY = Math.min(region.getFirst().getY(), region.getSecond().getY());
                            double minZ = Math.min(region.getFirst().getZ(), region.getSecond().getZ());

                            if (player.getLocation().getBlockX() >= minX && player.getLocation().getBlockX() <= maxX &&
                                    player.getLocation().getBlockY() >= minY && player.getLocation().getBlockY() <= maxY &&
                                    player.getLocation().getBlockZ() >= minZ && player.getLocation().getBlockZ() <= maxZ) {
                                if (!region.getPlayersInRegion().contains(player.getUniqueId())) {
                                    region.getPlayersInRegion().add(player.getUniqueId());
                                    ParentActionExecutor parent = new ParentActionExecutor();
                                    ActionExecutor executor = new ActionExecutor(parent);
                                    executor.addActions(region.getEnterActions());
                                    parent.execute(player, house, null);
                                }
                            } else if (region.getPlayersInRegion().contains(player.getUniqueId())) {
                                ParentActionExecutor parent = new ParentActionExecutor();
                                ActionExecutor executor = new ActionExecutor(parent);
                                executor.addActions(region.getExitActions());
                                parent.execute(player, house, null);
                                region.getPlayersInRegion().remove(player.getUniqueId());
                            }
                        });
                    }
                }
            }
        }.runTaskTimer(main, 0L, 1L));

        runnables.put("items", new BukkitRunnable() {
            @Override
            public void run() {
                LobbyListener.lobbyItems(main);
            }
        }.runTaskTimer(main, 0L, 20));

        runnables.put("cookieReset", new BukkitRunnable() {
            @Override
            public void run() {
                final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                // Check if it's midnight on Sunday
                if (dayOfWeek == Calendar.SUNDAY && hourOfDay == 0 && minute == 0) {
                    main.getCookieManager().newWeek();
                }
            }
        }.runTaskTimer(main, 0L, 20));

        runnables.put("launchPadParticles", new BukkitRunnable() {
            @Override
            public void run() {
                for (HousingWorld house : main.getHousesManager().getConcurrentLoadedHouses().values()) {
                    if (house.getWorld().getPlayers().isEmpty()) continue;

                    for (LaunchPad launchPad : house.getLaunchPads()) {
                        Location clone = launchPad.getLocation().clone();
                        house.getWorld().spawnParticle(Particle.ITEM_SLIME, clone.add(Math.random(), 1, Math.random()), 1, 0, 0, 0, 0);
                    }
                }
            }
        }.runTaskTimer(main, 0L, 2));

        /* Will bring this back after the beta.
        runnables.put("lobbyDisplays", new BukkitRunnable() {
            @Override
            public void run() {
                main.getLobbyDisplays().updateLobbyDisplays();
            }
        }.runTaskTimer(main, 0L, 40));
         */
    }

    public static void stopRunnables() {
        for (BukkitTask runnable : runnables.values()) {
            runnable.cancel();
        }
    }

    public static BukkitTask getRunnable(String description) {
        return runnables.get(description);
    }
}

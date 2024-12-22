package com.al3x.housing2;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Instances.Hologram;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.MineSkin.SkinData;
import com.al3x.housing2.MineSkin.SkinResponse;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Utils.scoreboard.HousingScoreboard;
import com.al3x.housing2.Utils.tablist.HousingTabList;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.*;

import static com.al3x.housing2.MineSkin.MineskinHandler.sendRequestForSkins;

public class Runnables {
    //Description, Runnable
    private static HashMap<String, BukkitTask> runnables = new HashMap<>();
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

        runnables.put("updateScoreboard", new BukkitRunnable() {
            @Override
            public void run() {
                //Update scoreboard
                Bukkit.getOnlinePlayers().forEach(HousingScoreboard::updateScoreboard);

                //Update tablist
                Collection<HousingWorld> houses = main.getHousesManager().getConcurrentLoadedHouses().values();
                for (HousingWorld house : houses) {
                    house.getWorld().getPlayers().forEach(player -> HousingTabList.setTabList(player, house));
                }
            }
        }.runTaskTimer(main, 0, 20L));

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
                            Bukkit.getScheduler().runTask(main, () -> function.execute(main, null, house));
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
                                    ActionExecutor executor = new ActionExecutor();
                                    executor.addActions(region.getEnterActions());
                                    executor.execute(player, house, null);
                                }
                            } else if (region.getPlayersInRegion().contains(player.getUniqueId())) {
                                ActionExecutor executor = new ActionExecutor();
                                executor.addActions(region.getExitActions());
                                executor.execute(player, house, null);
                                region.getPlayersInRegion().remove(player.getUniqueId());
                            }
                        });
                    }
                }
            }
        }.runTaskTimer(main, 0L, 1L));
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

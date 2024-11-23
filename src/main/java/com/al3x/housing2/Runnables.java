package com.al3x.housing2;

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
                if (main.getMineSkinKey() != null) {
                    PaginationList<SkinData> skins = new PaginationList<>(HousingNPC.loadedSkins, 21);
                    List<SkinData> currentSkins = skins.getPage(page);
                    if (currentSkins == null) {
                        sendRequestForSkins(skins.isEmpty() ? null : skins.getLast().getUuid());
                        page++;
                    }
                }
            }
        }.runTaskTimerAsynchronously(main, 0L, 10L)); // Every 10 ticks load a new page of skins

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

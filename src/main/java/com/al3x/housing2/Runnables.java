package com.al3x.housing2;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.scoreboard.HousingScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Runnables {
    //Description, Runnable
    private static HashMap<String, BukkitTask> runnables = new HashMap<>();

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
                Bukkit.getOnlinePlayers().forEach(HousingScoreboard::updateScoreboard);
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

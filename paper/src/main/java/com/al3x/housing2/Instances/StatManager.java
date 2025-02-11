package com.al3x.housing2.Instances;

import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Instances.HousingData.StatData;
import org.bukkit.entity.Player;

import java.util.*;

public class StatManager {

    private List<Stat> globalStats;
    private HousingWorld house;

    public StatManager(HousingWorld house) {
        this.house = house;
        this.globalStats = new ArrayList<>();
    }

    public Stat getPlayerStatByName(Player player, String name) {
        List<Stat> playerStats = house.loadOrCreatePlayerData(player).getCacheStats();
        if (playerStats == null) {
            return new Stat(name, "0.0");
        }
        for (Stat stat : playerStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        return new Stat(name, "0.0");

    }

    public Stat getPlayerStatByName(PlayerData player, String name) {
        List<Stat> playerStats = StatData.Companion.toList(player.getStats());

        for (Stat stat : playerStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        return new Stat(name, "0.0");

    }

    public Stat getGlobalStatByName(String name) {
        for (Stat stat : globalStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        Stat defaultStat = new Stat(name, "0.0");
//        globalStats.add(defaultStat);
        return defaultStat;
    }

    public List<Stat> getPlayerStats(Player player) {
        return house.loadOrCreatePlayerData(player).getCacheStats();
    }

    public void addPlayerStat(Player player, Stat stat) {
        List<Stat> playerStats = house.loadOrCreatePlayerData(player).getCacheStats();
        playerStats.add(stat);
    }

    public boolean hasStat(Player player, String name) {
        List<Stat> playerStats = house.loadOrCreatePlayerData(player).getCacheStats();

        for (Stat stat : playerStats) {
            if (stat.getStatName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasGlobalStat(String name) {
        for (Stat stat : globalStats) {
            if (stat.getStatName().equals(name)) {
                return true;
            }
        }

        return false;
    }


    public List<Stat> getGlobalStats() {
        return globalStats;
    }

    public void setGlobalStats(List<Stat> globalStats) {
        this.globalStats = globalStats;
    }
}
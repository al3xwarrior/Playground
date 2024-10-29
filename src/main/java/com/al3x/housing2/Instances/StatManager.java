package com.al3x.housing2.Instances;

import org.bukkit.entity.Player;

import java.util.*;

public class StatManager {

    private HashMap<UUID, List<Stat>> playerStats;
    private List<Stat> globalStats;
    private HousingWorld house;

    public StatManager(HousingWorld house) {
        this.house = house;
        this.playerStats = new HashMap<>();
        this.globalStats = new ArrayList<>();
    }

    public Stat getPlayerStatByName(Player player, String name) {
        List<Stat> playerStats = this.playerStats.getOrDefault(player.getUniqueId(), new ArrayList<>());

        for (Stat stat : playerStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        Stat defaultStat = new Stat(player.getUniqueId(), name, "0.0");
//        playerStats.add(defaultStat);
//
//        this.playerStats.put(player.getUniqueId(), playerStats);
        return defaultStat;
    }

    public Stat getGlobalStatByName(String name) {
        for (Stat stat : globalStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        Stat defaultStat = new Stat(UUID.randomUUID(), name, "0.0");
//        globalStats.add(defaultStat);
        return defaultStat;
    }

    public List<Stat> getPlayerStats(Player player) {
        return this.playerStats.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public void addPlayerStat(Player player, Stat stat) {
        List<Stat> playerStats = this.playerStats.getOrDefault(player.getUniqueId(), new ArrayList<>());
        playerStats.add(stat);
        this.playerStats.put(player.getUniqueId(), playerStats);
    }

    public boolean hasStat(Player player, String name) {
        List<Stat> playerStats = this.playerStats.getOrDefault(player.getUniqueId(), new ArrayList<>());

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

    public HashMap<UUID, List<Stat>> getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(HashMap<UUID, List<Stat>> playerStats) {
        this.playerStats = playerStats;
    }


    public List<Stat> getGlobalStats() {
        return globalStats;
    }

    public void setGlobalStats(List<Stat> globalStats) {
        this.globalStats = globalStats;
    }
}
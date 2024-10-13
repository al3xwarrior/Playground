package com.al3x.housing2.Instances;

import org.bukkit.entity.Player;

import java.util.*;

public class StatManager {

    private HashMap<UUID, List<Stat>> playerStats;
    private HousingWorld house;

    public StatManager(HousingWorld house) {
        this.house = house;
        this.playerStats = new HashMap<>();
    }

    public Stat getPlayerStatByName(Player player, String name) {
        List<Stat> playerStats = this.playerStats.getOrDefault(player.getUniqueId(), new ArrayList<>());

        for (Stat stat : playerStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        Stat defaultStat = new Stat(player.getUniqueId(), name, 0.0);
        playerStats.add(defaultStat);
        this.playerStats.put(player.getUniqueId(), playerStats);
        return defaultStat;
    }

    public List<Stat> getPlayerStats(Player player) {
        if (playerStats.containsKey(player.getUniqueId())) {
            return playerStats.get(player.getUniqueId());
        }
        return null;
    }

    public HashMap<UUID, List<Stat>> getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(HashMap<UUID, List<Stat>> playerStats) {
        this.playerStats = playerStats;
    }

}
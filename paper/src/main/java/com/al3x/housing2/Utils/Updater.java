package com.al3x.housing2.Utils;

import com.google.gson.*;
import org.bukkit.Bukkit;

import java.util.Map;

public class Updater {

    public static final Integer LATEST_VERSION = 1;

    public static JsonObject update(JsonObject data) {

        int version;
        try {
            version = data.get("version").getAsInt();
        } catch (Exception e) {
            version = 0;
        }
        
        switch (version) {
            /*
                Moved all player stats from "playerStats" hashmap into part of "playerData"
                Removed uuid field from player and global stats
             */
            case 0: {

                JsonObject playerStats = data.getAsJsonObject("playerStats");
                JsonObject playerData = data.getAsJsonObject("playerData");

                for (Map.Entry<String, JsonElement> player : playerStats.entrySet()) {

                    for (JsonElement stat : player.getValue().getAsJsonArray()) {

                        stat.getAsJsonObject().remove("id");

                    }

                    try {
                        playerData.getAsJsonObject(player.getKey()).add("stats", player.getValue());
                    } catch (Exception ignored) {}

                }

                data.remove("playerStats");

                JsonArray globalStats = data.getAsJsonArray("globalStats");

                for (JsonElement stat : globalStats) {

                    stat.getAsJsonObject().remove("id");

                }

            }
            case 1: {

            }

        }

        data.addProperty("version", LATEST_VERSION); // overwrites old version

        return data;

    }

}

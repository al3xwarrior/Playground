package com.al3x.housing2.Utils;

import com.al3x.housing2.Main;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class SkinCache implements Listener {

    public SkinCache() {}

    private static HashMap<String, String> skins;

    static {

        Gson gson = new Gson();
        try {

            File file = new File(Main.getInstance().getDataFolder() + "/skins.json");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                skins = gson.fromJson(reader, HashMap.class);
                reader.close();
            } else {
                file.createNewFile();
                skins = new HashMap<>();
            }
        } catch (IOException e) {
            skins = new HashMap<>();
            Main.getInstance().getLogger().warning("[Housing2] Failed to load skins.json");
        }

    }

    public static void save() {

        Main.getInstance().getLogger().info("[Housing2] Saving skins.json");

        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/skins.json")) {
            writer.write(gson.toJson(skins));
        } catch (IOException e) {
            Main.getInstance().getLogger().warning("[Housing2] Failed to save skins.json");
        }

    }

    public static HashMap<String, String> getSkins() {
        if (skins.isEmpty()) return null;
        return skins;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {

                    JsonObject playerJson = JsonParser.parseString(response.body()).getAsJsonObject();
                    String encodedTextures = playerJson.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();

                    JsonObject texturesJson = JsonParser.parseString(new String(Base64.getDecoder().decode(encodedTextures))).getAsJsonObject();
                    String texturesUrl = texturesJson.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();

                    skins.put(uuid.toString(), texturesUrl.substring(texturesUrl.lastIndexOf("/") + 1));

                } else throw new Exception();
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("[Housing2] Failed to load texture of player " + event.getPlayer().getName());
            }
        });

    }
}

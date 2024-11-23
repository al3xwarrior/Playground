package com.al3x.housing2.MineSkin;

import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;

public class MineskinHandler {
    private static Main main = Main.getInstance();
    private static Gson gson = new Gson();

    public static BiggerSkinData getSkinData(String uuid) {
        if (main.getMineSkinKey() == null) return null;
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .header("Accept", "application/json")
                            .header("User-Agent", "Housing2")
                            .header("Authorization", "Bearer " + Main.getInstance().getMineSkinKey())
                            .uri(new URI("https://api.mineskin.org/v2/skins/" + uuid))
                            .build(),
                    responseInfo -> HttpResponse.BodySubscribers.ofString(Charset.defaultCharset())
            );
            return gson.fromJson(response.body(), BiggerSkinResponse.class).getSkin();
        } catch (Exception e) {
            main.getLogger().warning("Failed to get skin data for " + uuid + ": " + e.getMessage());
        }
        return null;
    }

    public static void sendRequestForSkins(String after) {
        if (main.getMineSkinKey() == null) return;
        List<SkinData> previousSkins = HousingNPC.loadedSkins;
        HttpClient client = HttpClient.newBuilder().build();
        try {
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .header("Accept", "application/json")
                            .header("User-Agent", "Housing2")
                            .header("Authorization", "Bearer " + Main.getInstance().getMineSkinKey())
                            .uri(new URI("https://api.mineskin.org/v2/skins?size=21" + ((after != null && !after.isEmpty()) ? "&after=" + after : "")))
                            .build(),
                    responseInfo -> HttpResponse.BodySubscribers.ofString(Charset.defaultCharset())
            );
            try {
                SkinResponse skinResponse = gson.fromJson(response.body(), SkinResponse.class);
                if (skinResponse.getSuccess()) {
                    for (SkinData skin : skinResponse.getSkins()) {
                        if (skin == null) continue;
                        if (previousSkins.stream().noneMatch(s -> s.getUuid().equals(skin.getUuid()))) {
                            previousSkins.add(skin);
                        }
                    }
                }
            } catch (Exception e) {
                main.getLogger().info(response.body());
                main.getLogger().warning("Failed to get skins: " + e.getMessage());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.al3x.housing2.Controllers;

import com.al3x.housing2.Instances.HousingData.ResourcePackData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.PlaygroundWeb;
import io.jooby.FileUpload;
import io.jooby.StatusCode;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class ResourcePackController {
    private final Main main;
    private final Map<String, ResourcePackSession> packSessions = new HashMap<>();

    public ResourcePackController(Main main, PlaygroundWeb server) {
        this.main = main;

        server.get("/internal/upload-session", ctx -> {
            Map<String, String> queryMap = ctx.queryMap();
            ResourcePackSession session = packSessions.get(queryMap.get("key"));

            if (session == null) {
                ctx.setResponseCode(StatusCode.NOT_FOUND);
                return "";
            }

            if (Instant.now().isAfter(session.expires)) {
                ctx.setResponseCode(StatusCode.FORBIDDEN);
                return "";
            }

            ctx.setResponseCode(StatusCode.NO_CONTENT);
            return "";
        });

        server.post("/internal/upload-pack", ctx -> {
            Map<String, String> formMap = ctx.formMap();

            ResourcePackSession session = packSessions.get(formMap.get("key"));
            FileUpload archive = ctx.file("archive");

            if (session == null) {
                ctx.setResponseCode(StatusCode.BAD_REQUEST);
                return "";
            }

            if (Instant.now().isAfter(session.expires)) {
                ctx.setResponseCode(StatusCode.FORBIDDEN);
                return "";
            }

            String packUuid = UUID.randomUUID().toString();
            Files.copy(archive.stream(), new File(main.getDataFolder(), String.format("pack_objects/%s.zip", packUuid)).toPath(), StandardCopyOption.REPLACE_EXISTING);

            for (Player guest : session.house.getWorld().getPlayers()) {
                main.getResourcePackManager().removeResourcePack(guest, session.house);
            }

            ResourcePackData resourcePack = new ResourcePackData(
                    packUuid,
                    "This house uses a custom resource pack.",
                    true);
            session.house.setResourcePack(resourcePack);

            for (Player guest : session.house.getWorld().getPlayers()) {
                guest.sendMessage(colorize("&aA resource pack has been added to this house!"));
                main.getResourcePackManager().addResourcePack(guest, session.house);
            }

            session.expires = Instant.now();
            ctx.setResponseCode(StatusCode.NO_CONTENT);
            return "";
        });

        server.assets("/objects/*", new File(main.getDataFolder(), "pack_objects").toPath());
    }

    public String createSession(HousingWorld house) {
        String sessionToken = UUID.randomUUID().toString().split("-")[0];
        ResourcePackSession session = new ResourcePackSession(house);

        packSessions.put(sessionToken, session);
        return sessionToken;
    }
}

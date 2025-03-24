package com.al3x.housing2.Instances;

import com.al3x.housing2.Data.ResourcePackData;
import com.al3x.housing2.Main;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ResourcePackManager {
    public Main main;

    public ResourcePackManager(Main main) {
        this.main = main;
    }

    public void addResourcePack(Player player, HousingWorld house) {
        ResourcePackData resourcePack = house.getResourcePack();
        if (resourcePack == null) return;

        String packUrl = String.format("%s/objects/%s.zip", main.getConfig().getString("web_base"), resourcePack.getId());

        // i can't figure out how to calculate the hash so it's null for now -pixel
        // this just means the client will download the pack everytime they join but meh
        player.addResourcePack(UUID.fromString(resourcePack.getId()), packUrl, null, resourcePack.getPrompt(), resourcePack.isForce());
    }

    public void removeResourcePack(Player player, HousingWorld house) {
        ResourcePackData resourcePack = house.getResourcePack();
        if (resourcePack == null) return;

        player.removeResourcePack(UUID.fromString(resourcePack.getId()));
    }
}

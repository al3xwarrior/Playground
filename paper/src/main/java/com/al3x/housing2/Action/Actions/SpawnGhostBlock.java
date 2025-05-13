package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ItemStackProperty;
import com.al3x.housing2.Action.Properties.LocationProperty;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SpawnGhostBlock extends HTSLImpl {
    public SpawnGhostBlock() {
        super(ActionEnum.SPAWN_GHOST_BLOCK,
                "Spawn Ghost Block",
                "Spawns a ghost block for the player.",
                Material.CHAIN_COMMAND_BLOCK,
                List.of("spawnGhostBlock")
        );

        getProperties().addAll(List.of(
                new LocationProperty(
                        "location",
                        "Location",
                        "The location to spawn the ghost block at."
                ).setValue("INVOKERS_LOCATION"),
                new ItemStackProperty(
                        "item",
                        "Block",
                        "The block to spawn as a ghost block."
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Location loc = getProperty("location", LocationProperty.class).getLocation(player, house);
        ItemStack item = getValue("item", ItemStack.class);
        if (loc == null) {
            return OutputType.SUCCESS;
        }

        if (item == null) {
            return OutputType.SUCCESS;
        }

        Material material = item.getType();
        if (!material.isBlock()) {
            return OutputType.SUCCESS;
        }

        player.sendBlockChange(loc, material.createBlockData());

        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.LocationProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.function.BiFunction;

import static com.al3x.housing2.Enums.Locations.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class ExplosionAction extends HTSLImpl {
    public static HashMap<UUID, Integer> amountDone = new HashMap<>();

    public ExplosionAction() {
        super(
                ActionEnum.EXPLOSION,
                "Explosion",
                "Creates an explosion at a set location.",
                Material.TNT_MINECART,
                List.of("explosion")
        );

        getProperties().addAll(List.of(
                new LocationProperty(
                        "location",
                        "Location",
                        "The location to create the explosion at."
                ).setValue("INVOKERS_LOCATION"),
                new NumberProperty(
                        "power",
                        "Power",
                        "The power of the explosion.",
                        0, 20
                ).setValue("4")
        ));
    }
    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Double power = getProperty("power", NumberProperty.class).parsedValue(house, player);

        if (player != null) {
            if (amountDone.containsKey(player.getUniqueId())) {
                int amount = amountDone.get(player.getUniqueId());
                if (amount >= 5) {
                    if (amount == 5) {
                        player.sendMessage(colorize("&cYou have reached the limit of 5 explosions per tick!"));
                    }
                    amountDone.put(player.getUniqueId(), amount + 1);
                    return OutputType.SUCCESS;
                }
                amountDone.put(player.getUniqueId(), amount + 1);
            } else {
                amountDone.put(player.getUniqueId(), 1);
            }
        } else {
            if (amountDone.containsKey(house.getHouseUUID())) {
                int amount = amountDone.get(house.getHouseUUID());
                if (amount >= 5) {
                    return OutputType.SUCCESS;
                }
                amountDone.put(house.getHouseUUID(), amount + 1);
            } else {
                amountDone.put(house.getHouseUUID(), 1);
            }
        }

        Location loc = getProperty("location", LocationProperty.class).getLocation(player, house, player.getLocation(), player.getEyeLocation());
        if (loc == null) {
            return OutputType.ERROR;
        }
        player.getWorld().createExplosion(loc, power.floatValue(), false, false);
        return OutputType.ERROR;
    }

    @Override
    public int limit() {
        return 1;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

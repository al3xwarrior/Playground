package com.al3x.housing2.Instances;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.al3x.housing2.Utils.Color.colorize;

public class LobbyDisplays {

    private ArrayList<HouseDisplayEntity> displayEntities = new ArrayList<>();
    private HousesManager housesManager;

    private Location[] locations = new Location[] {
        new Location(Bukkit.getWorld("world"), 2.5, 70, 31.5),
        new Location(Bukkit.getWorld("world"), 0.5, 70, 33.5),
        new Location(Bukkit.getWorld("world"), -1.5, 70, 34.5),
        new Location(Bukkit.getWorld("world"), -4.5, 70, 34.5),
        new Location(Bukkit.getWorld("world"), -8.5, 70, 34.5),
        new Location(Bukkit.getWorld("world"), -11.5, 70, 34.5),
        new Location(Bukkit.getWorld("world"), -13.5, 70, 33.5),
        new Location(Bukkit.getWorld("world"), -15.5, 70, 31.5),
    };

    public LobbyDisplays(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    public void updateLobbyDisplays() {
        HousingWorld house = housesManager.getRandomHouse();
        if (house != null && displayEntities.size() < locations.length) {
            if (displayEntities.stream().anyMatch(display -> display.getHouse().equals(house))) return;
            displayEntities.add(new HouseDisplayEntity(house));
        }

        if (displayEntities.isEmpty()) return;

        displayEntities.forEach(display -> {
            // Find the current index in locations they are at and move to the next
            // delete if they are on the last one (like a boss)
            for (int i = 0; i < locations.length; i++) {
                Location displayLocation = display.getIconDisplay().getLocation();
                if (displayLocation.equals(locations[i])) {
                    if (i == locations.length - 1) {
                        displayEntities.remove(display);
                        display.getIconDisplay().remove();
                        display.getTextDisplay().remove();
                        break;
                    } else {
                        display.moveTo(locations[i + 1]);
                        break;
                    }
                }
            }
        });
    }

    public void removeDisplays() {
        displayEntities.forEach(display -> {
            display.getIconDisplay().remove();
            display.getTextDisplay().remove();
        });
        displayEntities.clear();
    }

    private static class HouseDisplayEntity {
        private final HousingWorld house;
        private final ItemDisplay icon;
        private final TextDisplay name;

        public HouseDisplayEntity(HousingWorld house) {
            this.house = house;

            // Create a new item display entity
            World lobby = Bukkit.getWorld("world");

            icon = lobby.spawn(new Location(lobby, 2.5, 70, 31.5), ItemDisplay.class);
            icon.setItemStack(new ItemStack(house.getIcon()));
            icon.setBillboard(Display.Billboard.CENTER);
            icon.setBrightness(new Display.Brightness(15, 15));

            name = lobby.spawn(new Location(lobby, 2.5, 70.6, 31.5), TextDisplay.class);
            name.setBillboard(Display.Billboard.CENTER);
            name.setText(colorize(house.getName()));
        }

        public void moveTo(Location location) {
            icon.setTeleportDuration(40);
            icon.teleport(location);
            name.setTeleportDuration(40);
            name.teleport(location.clone().add(0, 1, 0));
        }

        public HousingWorld getHouse() {
            return house;
        }

        public ItemDisplay getIconDisplay() {
            return icon;
        }

        public TextDisplay getTextDisplay() {
            return name;
        }
    }

}

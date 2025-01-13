package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PlibHologramLine;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Hologram {
    private boolean destroyed = false;
    private Main main;
    private ProtocolManager protocolManager;
    private List<String> text;
    private double spacing = .45;
    private HousingWorld house;
    private Location location;
    private ArrayList<PlibHologramLine> entitys;
    private final ArrayList<UUID> viewers = new ArrayList<>();

    public static ItemStack getHologramItem() {
        return ItemBuilder.create(Material.NAME_TAG).name("&aHologram").description("&7Place this in your house to place a Hologram!").build();
    }

    public Hologram(Main main, ArrayList<String> text, Player player, HousingWorld house, double x, double y, double z) {
        this.main = main;
        this.protocolManager = main.getProtocolManager();
        this.text = text;
        this.house = house;
        this.location = new Location(house.getWorld(), x, y, z);
        this.entitys = spawnHologramEntities();
    }

    public Hologram(Main main, Player player, HousingWorld house, double x, double y, double z) {
        this.main = main;
        this.protocolManager = main.getProtocolManager();
        this.text = new ArrayList<>();
        this.text.add("&6&lNew Hologram");
        this.text.add("&e&lRight Click to edit!");
        this.house = house;
        this.location = new Location(house.getWorld(), x, y, z);
        this.entitys = spawnHologramEntities();
    }

    public Hologram(Main main, Player player, HousingWorld house, Location location) {
        this.main = main;
        this.protocolManager = main.getProtocolManager();
        this.text = new ArrayList<>();
        this.text.add("&6&lNew Hologram");
        this.text.add("&e&lRight Click to edit!");
        this.house = house;
        this.location = location;


        this.entitys = spawnHologramEntities();
    }

    public Hologram(HousingWorld house, List<String> text, Location location, double spacing) {
        this.house = house;
        this.text = text;
        this.location = location;
        this.spacing = spacing;
        this.entitys = spawnHologramEntities();
    }

    //Call anytime you add or remove a line
    public ArrayList<PlibHologramLine> spawnHologramEntities() {
        if (entitys != null && !entitys.isEmpty()) {
            for (Player player : house.getWorld().getPlayers()) {
                for (PlibHologramLine entity : entitys) {
                    entity.hideFrom(player);
                }
                viewers.remove(player.getUniqueId());
            }
        }

        if (destroyed) return new ArrayList<>();

        ArrayList<PlibHologramLine> entitys = new ArrayList<>();
        List<String> text = new ArrayList<>(this.text);
        Collections.reverse(text);
        for (int i = text.size() - 1; i >= 0; i--) {
            String line = text.get(i);
            if (line == null) continue;
            Location startLocation = this.location.clone();
            startLocation = startLocation.add(0, spacing * (text.size() - 1 - i), 0);
            PlibHologramLine holo = new PlibHologramLine(startLocation);
            holo.setText(line);
            entitys.add(holo);
        }
        return entitys;
    }

    public void updateHologramEntity() {
        if (destroyed) return;

        // Create new entities for each line of text
        ArrayList<PlibHologramLine> entitys = new ArrayList<>(this.entitys);
        List<String> text = new ArrayList<>(this.text);
        Collections.reverse(text);

        for (Player player : house.getWorld().getPlayers()) {
            for (PlibHologramLine entity : entitys) {
                entity.hideFrom(player);
            }

            if (player.getLocation().distance(location) < 64) {
                for (int i = 0, entitysSize = entitys.size(); i < entitysSize; i++) {
                    PlibHologramLine entity = entitys.get(i);
                    entity.setText(player, HandlePlaceholders.parsePlaceholders(player, house, text.get(i)));
                    entity.showTo(player);
                }
            }
        }
    }

    public void addLine(String line) {
        this.text.add(line);
        this.entitys = spawnHologramEntities();
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
        this.entitys = spawnHologramEntities();
    }

    public void setLocation(Location location) {
        this.location = location;
        this.entitys = spawnHologramEntities();
    }

    public void setLine(int index, String line) {
        this.text.set(index, line);
        this.entitys = spawnHologramEntities();
    }

    public void removeLine(int index) {
        this.text.remove(index);
        this.entitys = spawnHologramEntities();
    }

    // Used when the house is deleted, might not actually be needed sense they are entities.
    public void remove() {
        destroyed = true;
        for (Player player : house.getWorld().getPlayers()) {
            for (PlibHologramLine entity : entitys) {
                entity.hideFrom(player);
            }
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public List<String> getText() {
        return text;
    }

    public double getSpacing() {
        return spacing;
    }

    public Location getLocation() {
        return location;
    }

    public List<Integer> getEntitys() {
        return entitys.stream().map(PlibHologramLine::getEntityId).toList();
    }
}

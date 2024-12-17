package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Hologram {
    private Main main;
    private ProtocolManager protocolManager;
    private ArrayList<String> text;
    private HousingWorld house;
    private Location location;
    private ArrayList<ArmorStand> entitys;

    public static ItemStack getHologramItem() {
        return ItemBuilder.create(Material.NAME_TAG).name("&aHologram").description("&7Place this in your house to place a Hologram!").build();
    }

    public Hologram(Main main, ArrayList<String> text, Player player, HousingWorld house, double x, double y, double z) {
        this.main = main;
        this.protocolManager = main.getProtocolManager();
        this.text = text;
        this.house = house;
        this.location = new Location(house.getWorld(), x, y, z);
        this.entitys = updateHologramEntity();
    }

    public Hologram(Main main, Player player, HousingWorld house, double x, double y, double z) {
        this.main = main;
        this.protocolManager = main.getProtocolManager();
        this.text = new ArrayList<>();
        this.text.add("&6&lNew Hologram");
        this.text.add("&e&lRight Click to edit!");
        this.house = house;
        this.location = new Location(house.getWorld(), x, y, z);
        this.entitys = updateHologramEntity();
    }

    public Hologram(Main main, Player player, HousingWorld house, Location location) {
        this.main = main;
        this.protocolManager = main.getProtocolManager();
        this.text = new ArrayList<>();
        this.text.add("&6&lNew Hologram");
        this.text.add("&e&lRight Click to edit!");
        this.house = house;
        this.location = location;
        this.entitys = updateHologramEntity();
    }

    public ArrayList<ArmorStand> updateHologramEntity() {
        // Delete Original Entitys
        if (this.entitys != null) {
            for (ArmorStand entity : this.entitys) {
                entity.remove();
            }
        }

        // Create new entities for each line of text
        ArrayList<ArmorStand> entitys = new ArrayList<>();
        Location startLocation = this.location.clone();
        for (int i = this.text.size() - 1; i >= 0; i--) {
            String line = this.text.get(i);
            if (line == null) continue;
            ArmorStand stand = this.location.getWorld().spawn(startLocation, ArmorStand.class);
            stand.setGravity(false);
            stand.setCustomNameVisible(true);
            stand.setCustomName("N/A");

            // Send the packet to all players in the house
            house.getWorld().getPlayers().forEach(player -> {
                PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
                packet.getIntegers().write(0, stand.getEntityId());

                WrappedDataWatcher watcher = new WrappedDataWatcher();

                // (a) Set the custom name (Index 2)
                WrappedDataWatcher.Serializer stringSerializer = WrappedDataWatcher.Registry.get(String.class);
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, stringSerializer), "{\"text\":\"" + line + "\"}");

                // (b) Make the custom name always visible (Index 3)
                WrappedDataWatcher.Serializer booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer), true);

                // Step 4: Write metadata to the packet
                packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

                // Step 5: Send the packet to the player
                protocolManager.sendServerPacket(player, packet);
            });

            stand.setInvisible(true);
            stand.setSmall(true);
            entitys.add(stand);
            startLocation.add(0, 0.45, 0);
        }

        return entitys;
    }

    public void addLine(String line) {
        this.text.add(line);
        this.entitys = updateHologramEntity();
    }

    public void setLine(int index, String line) {
        this.text.set(index, line);
        this.entitys = updateHologramEntity();
    }

    public void removeLine(int index) {
        this.text.remove(index);
        this.entitys = updateHologramEntity();
    }

    // Used when the house is deleted, might not actually be needed sense they are entities.
    public void remove() {
        for (ArmorStand entity : this.entitys) {
            entity.remove();
        }
    }

    public ArrayList<String> getText() {
        return text;
    }
    public Location getLocation() {
        return location;
    }
    public ArrayList<ArmorStand> getEntitys() {
        return entitys;
    }
}

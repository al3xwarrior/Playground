package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PlibHologramLine;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.comphenix.protocol.ProtocolManager;
import com.maximde.hologramlib.hologram.RenderMode;
import com.maximde.hologramlib.hologram.TextHologram;
import com.maximde.hologramlib.utils.Vector3F;
import me.catcoder.protocolsidebar.lib.folialib.FoliaLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.awt.font.TextHitInfo;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Hologram {
    private boolean destroyed = false;
    private Main main;
    private List<String> text;
    private double spacing = .45;
    private int backgroundColor = Color.fromARGB(63, 0, 0, 0).asARGB();
    private String scale = "1,1,1";
    private TextDisplay.TextAlignment alignment = TextDisplay.TextAlignment.CENTER;
    private TextDisplay.Billboard billboard = Display.Billboard.CENTER;
    private boolean shadow = false;
    private boolean seeThroughBlocks = true;
    private HousingWorld house;
    private Location location;
    private ConcurrentHashMap<Player, List<TextHologram>> entitys = new ConcurrentHashMap<>();


    public static List<Color> rainbow = com.al3x.housing2.Utils.Color.rainbow();
    public static int rainbowIndex = 0;

    public static ItemStack getHologramItem() {
        return ItemBuilder.create(Material.NAME_TAG).name("&aHologram").description("&7Place this in your house to place a Hologram!").build();
    }

    public Hologram(Main main, Player player, HousingWorld house, Location location) {
        this.main = main;
        this.text = new ArrayList<>();
        this.text.add("&6&lNew Hologram");
        this.text.add("&e&lRight Click to edit!");
        this.house = house;
        this.location = location;
    }

    public Hologram(HousingWorld house, List<String> text, Location location, double spacing, String scale, TextDisplay.TextAlignment alignment, TextDisplay.Billboard billboard, boolean shadow, boolean seeThroughBlocks, int backgroundColor) {
        this.house = house;
        this.text = text;
        this.location = location;
        this.spacing = spacing;
        this.main = Main.getInstance();
        this.scale = (scale == null || scale.isEmpty()) ? "1,1,1" : scale;
        this.alignment = (alignment == null) ? TextDisplay.TextAlignment.CENTER : alignment;
        this.billboard = (billboard == null) ? TextDisplay.Billboard.CENTER : billboard;
        this.shadow = shadow;
        this.seeThroughBlocks = seeThroughBlocks;
        this.backgroundColor = backgroundColor;
    }

    public void setHouse(HousingWorld house) {
        this.house = house;
    }

    private Component getComponent(Player player, int i) {
        Component component = Component.text("");
        String line = text.get(i);
        component = component.append(StringUtilsKt.housingStringFormatter(line, house, player));
        return component;
    }

    private Vector3F getScaleInternal() {
        String[] split = scale.split(",");
        if (split.length != 3) {
            return new Vector3F(1, 1, 1);
        }
        try {
            float x = Float.parseFloat(split[0]);
            float y = Float.parseFloat(split[1]);
            float z = Float.parseFloat(split[2]);
            return new Vector3F(x, y, z);
        } catch (NumberFormatException e) {
            return new Vector3F(1, 1, 1);
        }
    }

    //Call anytime you add or remove a line
    public void spawnHologramEntities(Player player) {
        if (destroyed) return;

        if (entitys.containsKey(player)) {
            entitys.get(player).forEach(hologram -> hologram.removeViewer(player));
        }


        new FoliaLib(Main.getInstance()).getScheduler().runAsync((t) -> {
            List<TextHologram> holograms = new ArrayList<>();

            for (int i = 0; i < text.size(); i++) {
                TextHologram hologram = new TextHologram(UUID.randomUUID().toString(), RenderMode.VIEWER_LIST)
                        .setAlignment(alignment)
                        .setShadow(shadow)
                        .setSeeThroughBlocks(seeThroughBlocks)
                        .setTextOpacity((byte) 255)
                        .setBillboard(billboard)
                        .setScale(getScaleInternal())
                        .setText(getComponent(player, i));
                main.getHologramManager().spawn(hologram, location.clone().add(0, spacing * (text.size() - 1 - i), 0));
                hologram.addViewer(player);
                holograms.add(hologram);
            }

            entitys.put(player, holograms);
        });
    }

    public void updateIndivualHologram(TextHologram hologram, int index, Player player) {
        if (destroyed) return;

        Color color;
        if (backgroundColor == -1) {
            color = rainbow.get(rainbowIndex);
        } else {
            color = Color.fromARGB(backgroundColor);
        }

        if (color != null) {
            hologram.setBackgroundColor(color.asARGB());
        }

        hologram.setText(getComponent(player, index));
        hologram.update();
    }

    public void updateHologramEntity() {
        if (destroyed) return;

        // Create new entities for each line of text
        for (Player player : house.getWorld().getPlayers()) {
            if (entitys.containsKey(player)) {
                if (player.getWorld() != house.getWorld()) {
                    entitys.get(player).forEach(hologram -> hologram.removeViewer(player));
                    entitys.remove(player);
                    return;
                }
                for (int i = 0; i < entitys.get(player).size(); i++) {
                    updateIndivualHologram(entitys.get(player).get(i), i, player);
                }
            } else {
                spawnHologramEntities(player);
            }
        }
    }

    public void addLine(String line) {
        this.text.add(line);
        entitys.forEach((player, hologram) -> {
            hologram.forEach(holo -> holo.removeViewer(player));
            spawnHologramEntities(player);
        });
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
        entitys.forEach((player, hologram) -> {
            for (int i = 0; i < hologram.size(); i++) {
                hologram.get(i).teleport(location.clone().add(0, spacing * (text.size() - 1 - i), 0));
            }
        });
    }

    public void setScale(String scale) {
        this.scale = scale;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale);
    }

    public void setLocation(Location location) {
        this.location = location;
        entitys.forEach((player, hologram) -> {
            for (int i = 0; i < hologram.size(); i++) {
                hologram.get(i).teleport(location.clone().add(0, spacing * (text.size() - 1 - i), 0));
            }
        });
    }

    public void setLine(int index, String line) {
        this.text.set(index, line);
        entitys.forEach((player, hologram) -> {
            hologram.forEach(holo -> {
                holo.setText(getComponent(player, index));
                holo.update();
            });
        });
    }

    public void removeLine(int index) {
        this.text.remove(index);
        entitys.forEach((player, hologram) -> {
            hologram.forEach(holo -> holo.removeViewer(player));
            spawnHologramEntities(player);
        });
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale);
    }

    public void setSeeThroughBlocks(boolean seeThroughBlocks) {
        this.seeThroughBlocks = seeThroughBlocks;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale);
    }

    public void setAlignment(TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale);
    }

    public void setBillboard(TextDisplay.Billboard billboard) {
        this.billboard = billboard;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale);
    }

    private void updateInternal(boolean shadow, boolean seeThroughBlocks, TextDisplay.TextAlignment alignment, TextDisplay.Billboard billboard, int backgroundColor, String scale) {
        this.shadow = shadow;
        this.seeThroughBlocks = seeThroughBlocks;
        this.alignment = alignment;
        this.billboard = billboard;
        this.backgroundColor = backgroundColor;
        this.scale = scale;
        new FoliaLib(main).getScheduler().runAsync((t) -> {
            entitys.forEach((player, hologram) -> {
                hologram.forEach(holo -> {
                    holo.setShadow(shadow);
                    holo.setSeeThroughBlocks(seeThroughBlocks);
                    holo.setAlignment(alignment);
                    holo.setBillboard(billboard);
                    holo.setBackgroundColor(backgroundColor);
                    holo.setScale(getScaleInternal());
                    holo.update();
                });
            });
        });
    }

    // Used when the house is deleted, might not actually be needed sense they are entities.
    public void remove() {
        destroyed = true;
        entitys.forEach((player, hologram) -> {
            hologram.forEach(holo -> holo.removeViewer(player));
        });
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

    public TextDisplay.Billboard getBillboard() {
        return billboard;
    }

    public TextDisplay.TextAlignment getAlignment() {
        return alignment;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public String getScale() {
        return scale;
    }

    public boolean isShadow() {
        return shadow;
    }

    public boolean isSeeThroughBlocks() {
        return seeThroughBlocks;
    }

    public List<TextHologram> getHolograms(Player player) {
        return entitys.get(player);
    }

    public HousingWorld getHouse() {
        return house;
    }

    public void setBackgroundColor(@NotNull int color) {
        this.backgroundColor = color;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, color, scale);
    }
}

package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PlibHologramLine;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.comphenix.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.maximde.hologramlib.__relocated__.com.tcoded.folialib.FoliaLib;
import com.maximde.hologramlib.hologram.RenderMode;
import com.maximde.hologramlib.hologram.TextHologram;
import com.maximde.hologramlib.utils.Vector3F;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import lombok.Setter;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import me.tofaa.entitylib.meta.other.InteractionMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
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
    @Getter
    @Setter
    private String startingY;
    private Location location;
    private Quaternion4f rotation;
    private ConcurrentHashMap<Player, List<TextHologram>> entitys = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Player, WrapperEntity> interaction = new ConcurrentHashMap<>();


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

    public Hologram(HousingWorld house, List<String> text, Location location, String startingY, double spacing, String scale, TextDisplay.TextAlignment alignment, TextDisplay.Billboard billboard, Quaternion4f rotation, boolean shadow, boolean seeThroughBlocks, int backgroundColor) {
        this.house = house;
        this.text = text;
        this.location = location;
        this.startingY = startingY;
        this.spacing = spacing;
        this.main = Main.getInstance();
        this.scale = (scale == null || scale.isEmpty()) ? "1,1,1" : scale;
        this.alignment = (alignment == null) ? TextDisplay.TextAlignment.CENTER : alignment;
        this.billboard = (billboard == null) ? TextDisplay.Billboard.CENTER : billboard;
        this.shadow = shadow;
        this.seeThroughBlocks = seeThroughBlocks;
        this.backgroundColor = backgroundColor;
        this.rotation = rotation;
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
        // Apparently this needs to be done on the main thread what?
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            if (destroyed) return;

            if (entitys.containsKey(player)) {
                entitys.get(player).forEach(hologram -> hologram.removeViewer(player));
            }

            if (interaction.containsKey(player)) {
                interaction.get(player).removeViewer(player.getUniqueId());
            }

            WrapperEntity entity = new WrapperEntity(EntityTypes.INTERACTION);
            entity.consumeEntityMeta(InteractionMeta.class, meta -> {
                meta.setHeight(0.5f);
                meta.setWidth(0.5f);
            });
            entity.addViewer(player.getUniqueId());
            String parsed = Placeholder.handlePlaceholders(startingY, house, player);
            if (parsed != null && !parsed.isEmpty()) {
                try {
                    double y = Double.parseDouble(parsed);
                    location.setY(y);
                } catch (NumberFormatException ignored) {
                }
            }
            entity.spawn(new com.github.retrooper.packetevents.protocol.world.Location(location.getX(), location.getY(), location.getZ(), 0, 0));
            interaction.put(player, entity);

            new FoliaLib(Main.getInstance()).getScheduler().runAsync((t) -> {
                List<TextHologram> holograms = new ArrayList<>();

                for (int i = 0; i < text.size(); i++) {
                    TextHologram hologram = new TextHologram(UUID.randomUUID().toString(), RenderMode.VIEWER_LIST)
                            .setAlignment(alignment)
                            .setShadow(shadow)
                            .setSeeThroughBlocks(seeThroughBlocks)
                            .setTextOpacity((byte) 255)
                            .setBillboard(billboard)
                            .setTeleportDuration(0)
                            .setScale(getScaleInternal())
                            .setText(getComponent(player, i));

                    if (rotation != null) {
                        hologram = hologram.setRightRotation(this.rotation.getX(), this.rotation.getY(), this.rotation.getZ(), this.rotation.getW());
                    }

                    hologram.addViewer(player);
                    main.getHologramManager().spawn(hologram, location.clone().add(0, spacing * (text.size() - 1 - i), 0));
                    holograms.add(hologram);
                }

                entitys.put(player, holograms);
            });
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

        Location location = this.location.clone();
        String parsed = Placeholder.handlePlaceholders(startingY, house, player);
        if (parsed != null && !parsed.isEmpty()) {
            try {
                double y = Double.parseDouble(parsed);
                location.setY(y);
            } catch (NumberFormatException ignored) {
            }
        }
        hologram.teleport(location.add(0, spacing * (index - 1), 0));
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
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale, rotation);
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
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale, rotation);
    }

    public void setSeeThroughBlocks(boolean seeThroughBlocks) {
        this.seeThroughBlocks = seeThroughBlocks;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale, rotation);
    }

    public void setAlignment(TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale, rotation);
    }

    public void setBillboard(TextDisplay.Billboard billboard) {
        this.billboard = billboard;
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, backgroundColor, scale, rotation);
    }

    public void setRotationRaw(Quaternion4f rotation) {
        this.rotation = rotation;
    }

    private void updateInternal(boolean shadow, boolean seeThroughBlocks, TextDisplay.TextAlignment alignment, TextDisplay.Billboard billboard, int backgroundColor, String scale, Quaternion4f rotation) {
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

                    if (rotation != null) {
                        holo.setRightRotation(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
                    } else {
                        holo.setRightRotation(0, 0, 0, 1);
                    }

                    holo.setBackgroundColor(backgroundColor);
                    holo.setScale(getScaleInternal());
                    holo.update();
                });
            });
        });
    }

    // Used when the house is deleted, might not actually be needed sense they are entities.
    public void remove() {
        entitys.forEach((player, hologram) -> {
            hologram.forEach(holo -> holo.removeViewer(player));
        });
        interaction.forEach((player, hologram) -> {
            hologram.removeViewer(player.getUniqueId());
        });
    }

    public void removeViewer(Player player) {
        entitys.get(player).forEach(holo -> holo.removeViewer(player));
        entitys.remove(player);
        interaction.get(player).removeViewer(player.getUniqueId());
        interaction.remove(player);
    }

    public void destroy() {
        destroyed = true;
        entitys.forEach((player, hologram) -> {
            hologram.forEach(holo -> holo.removeViewer(player));
        });
        interaction.forEach((player, hologram) -> {
            hologram.removeViewer(player.getUniqueId());
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

    public Quaternion4f getRotation() {
        return rotation;
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
        updateInternal(shadow, seeThroughBlocks, alignment, billboard, color, scale, rotation);
    }

    public WrapperEntity getInteractionEntity(Player player) {
        return interaction.get(player);
    }
}

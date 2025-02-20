package com.al3x.housing2.Action.Actions.Utils;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.Particles;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ParticleUtils {
    public static List<ActionEditor.ActionItem> customData(Particles particle, HashMap<String, Object> customData, HousingWorld house, Menu editMenu, Player player) {
        List<ActionEditor.ActionItem> items = new ArrayList<>();
        switch (particle.getData()) {
            case COLOR -> {
                items.addAll(color("color", particle, customData, house, editMenu, player));
            }
            case DUST -> {
                items.addAll(color("color", particle, customData, house, editMenu, player));
                items.addAll(floatData("size", particle, customData, house, editMenu, player));
            }
            case DUST_TRANSITION -> {
                items.addAll(color("color", particle, customData, house, editMenu, player));
                items.addAll(color("color2", particle, customData, house, editMenu, player));
                items.addAll(floatData("size", particle, customData, house, editMenu, player));
            }
        }
        return items;
    }

    public static Object output(Particles particle, HashMap<String, Object> customData) {
        if (customData == null) return null;
        if (particle.getData() == null) return null;
        switch (particle.getData()) {
            case COLOR -> {
                String[] split = ((String) customData.getOrDefault("color", "255,255,255")).split(",");
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                return Color.fromARGB(255, r, g, b);
            }
            case DUST -> {
                String[] split = ((String) customData.getOrDefault("color", "255,255,255")).split(",");
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                return new Particle.DustOptions(Color.fromRGB(r, g, b), Float.parseFloat(customData.getOrDefault("size", "1.0").toString()));
            }
            case DUST_TRANSITION -> {
                String[] split = ((String) customData.getOrDefault("color1", "255,255,255")).split(",");
                int r = Integer.parseInt(split[0]);
                int g = Integer.parseInt(split[1]);
                int b = Integer.parseInt(split[2]);
                String[] split2 = ((String) customData.getOrDefault("color2", "255,255,255")).split(",");
                int r2 = Integer.parseInt(split2[0]);
                int g2 = Integer.parseInt(split2[1]);
                int b2 = Integer.parseInt(split2[2]);
                return new Particle.DustTransition(Color.fromRGB(r, g, b), Color.fromRGB(r2, g2, b2), Float.parseFloat(customData.getOrDefault("size", "1.0").toString()));
            }
        }
        return null;
    }

    public static List<String> keys(Particles particle) {
        if (particle.getData() == null) return null;
        List<String> keys = new ArrayList<>();
        switch (particle.getData()) {
            case COLOR -> {
                keys.add("color");
            }
            case DUST -> {
                keys.add("color");
                keys.add("size");
            }
            case DUST_TRANSITION -> {
                keys.add("color");
                keys.add("color2");
                keys.add("size");
            }
        }
        return keys;
    }

    private static List<ActionEditor.ActionItem> color(String key, Particles particle, HashMap<String, Object> customData, HousingWorld house, Menu editMenu, Player player) {
        List<ActionEditor.ActionItem> items = new ArrayList<>();
        items.add(new ActionEditor.ActionItem(key,
                ItemBuilder.create(Material.RED_DYE)
                        .name("&eColor")
                        .info("&7Current Value", "")
                        .info(null, "&a" + customData.getOrDefault(key, "255,255,255"))
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                (event, notExistant) -> {
                    player.sendMessage(colorize("&ePlease enter the color in RGB format (0-255) separated by commas."));
                    editMenu.openChat(Main.getInstance(), "255,255,255", (s) -> {
                        String[] split = s.split(",");
                        if (split.length != 3) {
                            player.sendMessage(colorize("&cInvalid color format!"));
                            return;
                        }
                        try {
                            int r = Integer.parseInt(split[0]);
                            int g = Integer.parseInt(split[1]);
                            int b = Integer.parseInt(split[2]);
                            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                                player.sendMessage(colorize("&cInvalid color format!"));
                                return;
                            }
                            customData.put(key, r + "," + g + "," + b);
                            player.sendMessage(colorize("&aColor set to " + r + ", " + g + ", " + b + "."));
                            Bukkit.getScheduler().runTask(Main.getInstance(), editMenu::open);
                        } catch (NumberFormatException e) {
                            player.sendMessage(colorize("&cInvalid color format!"));
                        }
                    });
                    return true;
                }
        ));
        return items;
    }

    private static List<ActionEditor.ActionItem> floatData(String key, Particles particle, HashMap<String, Object> customData, HousingWorld house, Menu editMenu, Player player) {
        List<ActionEditor.ActionItem> items = new ArrayList<>();
        items.add(new ActionEditor.ActionItem(key,
                ItemBuilder.create(Material.BOOK)
                        .name("&e" + key)
                        .info("&7Current Value", "")
                        .info(null, "&a" + customData.getOrDefault(key, 0.0))
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                (event, notExistant) -> {
                    player.sendMessage(colorize("&ePlease enter the value for " + key + "."));
                    editMenu.openChat(Main.getInstance(), "0.0", (s) -> {
                        try {
                            double value = Double.parseDouble(s);
                            customData.put(key, value);
                            player.sendMessage(colorize("&aValue set to " + value + "."));
                            Bukkit.getScheduler().runTask(Main.getInstance(), editMenu::open);
                        } catch (NumberFormatException e) {
                            player.sendMessage(colorize("&cInvalid value format!"));
                        }
                    });
                    return true;
                }
        ));
        return items;
    }
}

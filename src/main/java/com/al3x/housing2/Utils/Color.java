package com.al3x.housing2.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;

import java.util.List;

public class Color {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> colorize (List<String> s) {
        s.replaceAll(Color::colorize);
        return s;
    }
    public static String colorizeLegacyText(String s) {
        return s.replaceAll("&", "§");
    }
    public static Material fromColor(BarColor s) {
        return switch (s) {
            case BLUE -> Material.BLUE_STAINED_GLASS_PANE;
            case GREEN -> Material.GREEN_STAINED_GLASS_PANE;
            case PINK -> Material.PINK_STAINED_GLASS_PANE;
            case PURPLE -> Material.PURPLE_STAINED_GLASS_PANE;
            case RED -> Material.RED_STAINED_GLASS_PANE;
            case WHITE -> Material.WHITE_STAINED_GLASS_PANE;
            case YELLOW -> Material.YELLOW_STAINED_GLASS_PANE;
            default -> Material.BLACK_STAINED_GLASS_PANE;
        };
    }

}

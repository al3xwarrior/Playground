package com.al3x.housing2.Utils;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Color.fromRGB;

public class Color {
    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean isJustColor(String s) {
        return s.matches("^[&§][0-9a-fA-F]$");
    }

    public static List<String> colorize(List<String> s) {
        s.replaceAll(Color::colorize);
        return s;
    }

    public static List<org.bukkit.Color> rainbow() {
        List<org.bukkit.Color> colors = new ArrayList<>();

        for (int r = 0; r < 100; r++) colors.add(fromRGB(r * 255 / 100, 255, 0));
        for (int g = 100; g > 0; g--) colors.add(fromRGB(255, g * 255 / 100, 0));
        for (int b = 0; b < 100; b++) colors.add(fromRGB(255, 0, b * 255 / 100));
        for (int r = 100; r > 0; r--) colors.add(fromRGB(r * 255 / 100, 0, 255));
        for (int g = 0; g < 100; g++) colors.add(fromRGB(0, g * 255 / 100, 255));
        for (int b = 100; b > 0; b--) colors.add(fromRGB(0, 255, b * 255 / 100));
        colors.add(fromRGB(0, 255, 0));
        return colors;
    }

    public static String colorizeLegacyText(String s) {
        return s.replaceAll("&", "§");
    }

    public static Material fromColor(BossBar.Color s) {
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

    public static String removeColor(String s) {
        return ChatColor.stripColor(s.replaceAll("&", "§"));
    }

}

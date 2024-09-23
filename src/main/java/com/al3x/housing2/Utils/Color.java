package com.al3x.housing2.Utils;

import org.bukkit.ChatColor;

public class Color {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}

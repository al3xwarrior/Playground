package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Distance extends Placeholder {
    @Override
    public String getPlaceholder() {
        return "%distance/[[x],[y],[z]] - [[x2],[y2],[z2]]%";
    }

    @Override
    public boolean hasArgs() {
        return true;
    }

    @Override
    public String handlePlaceholder(String input, HousingWorld house, Player player) {
        if (input.split("/").length < 2) {
            return "0";
        }
        String[] split = input.split("/");
        String afterOne = String.join("/", Arrays.copyOfRange(split, 1, split.length));
        if (!afterOne.contains(" - ")) {
            return "0";
        }
        String[] coords = afterOne.split(" - ");
        if (coords.length < 2) {
            return "0";
        }
        String[] coords1 = Placeholder.handlePlaceholders(coords[0], house, player, true).split(",");
        String[] coords2 = Placeholder.handlePlaceholders(coords[1], house, player, true).split(",");
        try {
            double x1 = Double.parseDouble(Placeholder.handlePlaceholders(coords1[0], house, player, true));
            double y1 = Double.parseDouble(Placeholder.handlePlaceholders(coords1[1], house, player, true));
            double z1 = Double.parseDouble(Placeholder.handlePlaceholders(coords1[2], house, player, true));
            double x2 = Double.parseDouble(Placeholder.handlePlaceholders(coords2[0], house, player, true));
            double y2 = Double.parseDouble(Placeholder.handlePlaceholders(coords2[1], house, player, true));
            double z2 = Double.parseDouble(Placeholder.handlePlaceholders(coords2[2], house, player, true));
            double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
            return String.valueOf(distance);
        } catch (Exception e) {
            return "null";
        }
    }
}

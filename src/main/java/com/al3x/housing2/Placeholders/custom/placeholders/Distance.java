package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

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
        String[] coords = Placeholder.handlePlaceholders(split[1], house, player).replace("%", "").split(" - ");
        String[] coords1 = coords[0].split(",");
        String[] coords2 = coords[1].split(",");
        double x1 = Double.parseDouble(Placeholder.handlePlaceholders(coords1[0], house, player));
        double y1 = Double.parseDouble(Placeholder.handlePlaceholders(coords1[1], house, player));
        double z1 = Double.parseDouble(Placeholder.handlePlaceholders(coords1[2], house, player));
        double x2 = Double.parseDouble(Placeholder.handlePlaceholders(coords2[0], house, player));
        double y2 = Double.parseDouble(Placeholder.handlePlaceholders(coords2[1], house, player));
        double z2 = Double.parseDouble(Placeholder.handlePlaceholders(coords2[2], house, player));
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
        return String.valueOf(distance);
    }
}

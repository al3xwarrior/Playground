package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerName extends Placeholder {
    @Override
    public String getPlaceholder() {
        return "%player.name%";
    }

    @Override
    public List<String> getAliases() {
        return List.of("%player%");
    }

    @Override
    public String handlePlaceholder(String input, HousingWorld house, Player player) {
        if (player == null) {
            return "null";
        }
        return player.getName();
    }
}

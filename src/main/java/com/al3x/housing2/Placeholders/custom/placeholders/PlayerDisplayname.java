package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

public class PlayerDisplayname extends Placeholder {
    @Override
    public String getPlaceholder() {
        return "%player.displayname%";
    }

    @Override
    public String handlePlaceholder(String input, HousingWorld house, Player player) {
        if (player == null) {
            return "null";
        }
        return player.getDisplayName();
    }
}

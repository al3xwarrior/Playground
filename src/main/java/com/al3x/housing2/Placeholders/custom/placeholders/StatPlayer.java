package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class StatPlayer extends Placeholder {
    @Override
    public String getPlaceholder() {
        return "%stat.player/[stat]%";
    }

    @Override
    public List<String> getAliases() {
        return List.of("%player.stat/[stat]%");
    }

    @Override
    public boolean hasArgs() {
        return true;
    }

    @Override
    public String handlePlaceholder(String input, HousingWorld house, Player player) {
        if (player == null) {
            return "0";
        }
        if (input.split("/").length < 2) {
            return "0";
        }
        String statName = input.split("/")[1].replace("%", "");

        if (statName.contains("[") && statName.contains("]")) {
            statName = statName.substring(statName.indexOf("[") + 1, statName.indexOf("]"));
            statName = Placeholder.handlePlaceholders("%" + statName + "%", house, player);
        }
        return house.getStatManager().getPlayerStatByName(player, statName).formatValue();
    }
}

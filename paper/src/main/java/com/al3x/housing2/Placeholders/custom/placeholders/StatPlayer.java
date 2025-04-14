package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.StringUtilsKt;
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
        return List.of("%player.stat/[stat]%", "%s.p/[stat]%");
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
        if (!input.contains("/")) {
            return "0";
        }
        String statName = StringUtilsKt.substringAfter(input, "/");
        statName = Placeholder.handlePlaceholders(statName, house, player, true);
        return house.getStatManager().getPlayerStatByName(player, statName).formatValue();
    }
}

package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

public class Region {
    public Region() {
        new Name();
    }

    private static class Name extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%region.name%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            return  house.getRegions().stream().filter(region -> region.getPlayersInRegion().contains(player.getUniqueId())).map(com.al3x.housing2.Instances.Region::getName).findFirst().orElse("none");
        }
    }
}

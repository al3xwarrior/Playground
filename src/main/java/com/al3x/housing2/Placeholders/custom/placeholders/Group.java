package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

public class Group {
    public Group() {
        new Name();
        new Prefix();
        new Suffix();
        new Priority();
        new Color();
        new DisplayName();
    }

    private static class Name extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%group.name%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return house.loadOrCreatePlayerData(player).getGroupInstance(house).getName();
        }
    }

    private static class Prefix extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%group.prefix%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return house.loadOrCreatePlayerData(player).getGroupInstance(house).getPrefix();
        }
    }

    private static class Suffix extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%group.suffix%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return house.loadOrCreatePlayerData(player).getGroupInstance(house).getSuffix();
        }
    }

    private static class Priority extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%group.priority%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(house.loadOrCreatePlayerData(player).getGroupInstance(house).getPriority());
        }
    }

    private static class Color extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%group.color%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(house.loadOrCreatePlayerData(player).getGroupInstance(house).getColor());
        }
    }

    private static class DisplayName extends Placeholder {

        @Override
        public String getPlaceholder() {
            return "%group.displayname%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return house.loadOrCreatePlayerData(player).getGroupInstance(house).getDisplayName();
        }
    }
}

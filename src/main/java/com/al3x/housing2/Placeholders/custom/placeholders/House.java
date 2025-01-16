package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;

public class House {
    public House() {
        new Name();
        new Cookies();
        new Guests();
        new CookiesPlayer();
        new StatGlobal();
    }

    private static class Name extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%house.name%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            return house.getName();
        }
    }

    private static class Cookies extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%house.cookies%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            return String.valueOf(house.getCookies());
        }
    }

    private static class Guests extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%house.guests%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            return String.valueOf(house.getWorld().getPlayerCount());
        }
    }

    private static class CookiesPlayer extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%house.cookies.player%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            return (!house.getCookieGivers().isEmpty()) ? String.valueOf(house.getCookieGivers().getLast()) : "null";
        }
    }

    private static class StatGlobal extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%stat.global/[stat]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%house.stat/[stat]%");
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
            String statName = input.split("/")[1].replace("%", "");

            if (statName.contains("[") && statName.contains("]")) {
                statName = statName.substring(statName.indexOf("[") + 1, statName.indexOf("]"));
                statName = Placeholder.handlePlaceholders("%" + statName + "%", house, player);
            }
            return house.getStatManager().getGlobalStatByName(statName).formatValue();
        }
    }
}

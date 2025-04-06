package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class House {
    public House() {
        new Name();
        new Cookies();
        new Guests();
        new CookiesPlayer();
        new Time();
        new StatGlobal();
        new BlockAt();
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
            if (!input.contains("/")) {
                return "0";
            }
            String statName = Placeholder.handlePlaceholders(input.substring(input.indexOf("/") + 1).replace("%", ""), house, player, true);
            return house.getStatManager().getGlobalStatByName(statName).formatValue();
        }
    }

    private static class Time extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%house.time%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            return String.valueOf(house.getWorld().getTime());
        }
    }

    private static class BlockAt extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%house.block/[x],[y],[z]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%world.block/[x],[y],[z]%");
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (house == null) {
                return "null";
            }
            if (!input.contains("/")) {
                return "null";
            }

            String[] split = input.split("/");
            String afterOne = String.join("/", Arrays.copyOfRange(split, 1, split.length));

            String[] coords = Placeholder.handlePlaceholders(afterOne, house, player, true).split(",");

            try {
                int x1 = Integer.parseInt(Placeholder.handlePlaceholders(coords[0], house, player, true));
                int y1 = Integer.parseInt(Placeholder.handlePlaceholders(coords[1], house, player, true));
                int z1 = Integer.parseInt(Placeholder.handlePlaceholders(coords[2], house, player, true));
                Block block = house.getWorld().getBlockAt(x1, y1, z1);
                return block.getType().name();
            } catch (Exception e) {
                return "null";
            }
        }
    }
}

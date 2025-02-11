package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class Touching {
    public Touching() {
        new Down();
        new Up();
        new North();
        new South();
        new East();
        new West();
    }

    private static class Down extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%touching.block/down%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%standing.block%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            Location loc = player.getLocation().clone().add(0, -1, 0);
            return loc.getBlock().getType().name();
        }
    }

    private static class Up extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%touching.block/up%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            Location loc = player.getLocation().clone().add(0, 1, 0);
            return loc.getBlock().getType().name();
        }
    }

    private static class North extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%touching.block/north%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%touching.block/front%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            Location loc = player.getLocation().clone().add(0, 0, -1);
            return loc.getBlock().getType().name();
        }
    }

    private static class South extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%touching.block/south%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%touching.block/back%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            Location loc = player.getLocation().clone().add(0, 0, 1);
            return loc.getBlock().getType().name();
        }
    }

    private static class East extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%touching.block/east%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%touching.block/right%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            Location loc = player.getLocation().clone().add(1, 0, 0);
            return loc.getBlock().getType().name();
        }
    }

    private static class West extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%touching.block/west%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%touching.block/left%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            Location loc = player.getLocation().clone().add(-1, 0, 0);
            return loc.getBlock().getType().name();
        }
    }
}

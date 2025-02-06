package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

public class PlayerLocation{
    public PlayerLocation() {
        new Coords();
        new X();
        new Y();
        new Z();
        new Pitch();
        new Yaw();
        new Speed();
    }

    private static class Coords extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.coords%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ();
        }
    }

    private static class X extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.x%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getLocation().getX());
        }
    }

    private static class Y extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.y%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getLocation().getY());
        }
    }

    private static class Z extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.z%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getLocation().getZ());
        }
    }

    private static class Pitch extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.pitch%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getLocation().getPitch());
        }
    }

    private static class Yaw extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.yaw%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getLocation().getYaw());
        }
    }

    private static class Speed extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.speed%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) return "null";
            return String.valueOf(player.getVelocity().length());
        }
    }
}

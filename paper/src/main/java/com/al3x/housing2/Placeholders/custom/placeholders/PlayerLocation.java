package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.PlayerSpeedManager;
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
        new Facing();
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
            return PlayerSpeedManager.playerSpeeds.getOrDefault(player.getUniqueId(), 0.0).toString();
        }
    }

    private static class Facing extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.location.direction%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) return "null";

            // Shoutout worldedit for this code
            double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
            if (rotation < 0.0D) {
                rotation += 360.0D;
            }
            if ((0.0D <= rotation) && (rotation < 22.5D)) {
                return "W";
            }
            if ((22.5D <= rotation) && (rotation < 67.5D)) {
                return "NW";
            }
            if ((67.5D <= rotation) && (rotation < 112.5D)) {
                return "N";
            }
            if ((112.5D <= rotation) && (rotation < 157.5D)) {
                return "NE";
            }
            if ((157.5D <= rotation) && (rotation < 202.5D)) {
                return "E";
            }
            if ((202.5D <= rotation) && (rotation < 247.5D)) {
                return "SE";
            }
            if ((247.5D <= rotation) && (rotation < 292.5D)) {
                return "S";
            }
            if ((292.5D <= rotation) && (rotation < 337.5D)) {
                return "SW";
            }
            if ((337.5D <= rotation) && (rotation < 360.0D)) {
                return "W";
            }

            return null;
        }
    }
}

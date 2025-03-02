package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.EatingListener;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerMisc {
    public PlayerMisc() {
        new Health();
        new MaxHealth();
        new Food();
        new Ping();
        new IsSprinting();
        new IsEating();
        new XP();
    }

    private static class Health extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.health%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getHealth());
        }
    }

    private static class MaxHealth extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.maxHealth%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%player.maxhealth%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getMaxHealth());
        }
    }

    private static class Food extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.food%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%player.hunger%");
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getFoodLevel());
        }
    }

    //ping
    private static class Ping extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.ping%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getPing());
        }
    }

    //isSprinting
    private static class IsSprinting extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.isSprinting%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.isSprinting());
        }
    }

    //isEating
    private static class IsEating extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.isEating%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(EatingListener.isPlayerEating(player));
        }
    }

    //xp
    private static class XP extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%player.xp%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            return String.valueOf(player.getExp());
        }
    }
}

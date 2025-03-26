package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.HouseEvents.PotionSplash;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;

public class SplashPotionEvent {
    public SplashPotionEvent() {
        new Name();
        new Shooter();
        new X();
        new Y();
        new Z();
        new Coords();
        new ExactCoords();
    }

    static class Name extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/name%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            ItemStack item = PotionSplash.potionSplashEvent.getPotion().getItem();
            return item.hasItemMeta() ? item.getItemMeta().getDisplayName() : "null";
        }
    }

    static class Shooter extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/shooter%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            if (!(PotionSplash.potionSplashEvent.getPotion().getShooter() instanceof Player shooter)) {
                return "null";
            }
            return shooter.getName();
        }
    }

    static class X extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/x%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            return String.valueOf(PotionSplash.potionSplashEvent.getEntity().getLocation().getX());
        }
    }

    static class Y extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/y%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            return String.valueOf(PotionSplash.potionSplashEvent.getEntity().getLocation().getY());
        }
    }

    static class Z extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/z%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            return String.valueOf(PotionSplash.potionSplashEvent.getEntity().getLocation().getZ());
        }
    }

    static class Coords extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/coords%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            return String.format("%s, %s, %s",
                    PotionSplash.potionSplashEvent.getEntity().getLocation().getBlockX(),
                    PotionSplash.potionSplashEvent.getEntity().getLocation().getBlockY(),
                    PotionSplash.potionSplashEvent.getEntity().getLocation().getBlockZ());
        }
    }

    static class ExactCoords extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.potion/exactCoords%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (PotionSplash.potionSplashEvent == null) {
                return "null";
            }
            return String.format("%s, %s, %s",
                    PotionSplash.potionSplashEvent.getEntity().getLocation().getX(),
                    PotionSplash.potionSplashEvent.getEntity().getLocation().getY(),
                    PotionSplash.potionSplashEvent.getEntity().getLocation().getZ());
        }
    }
}

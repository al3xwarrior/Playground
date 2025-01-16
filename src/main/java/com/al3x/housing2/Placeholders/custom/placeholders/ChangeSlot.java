package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.HouseEvents.ChangeHeldItem;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class ChangeSlot {
    public ChangeSlot() {

    }

    private static class PreviousSlot extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.change/previousSlot%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                return String.valueOf(event.getPreviousSlot());
            }
            return "null";
        }
    }

    private static class NewSlot extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.change/newSlot%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                return String.valueOf(event.getNewSlot());
            }
            return "null";
        }
    }

    private static class PreviousItem extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.change/previousItem%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
                return (previousItem != null && previousItem.hasItemMeta()) ? previousItem.getItemMeta().getDisplayName() : "null";
            }
            return "null";
        }
    }

    private static class NewItem extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.change/newItem.type%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
                return (previousItem == null) ? "null" : previousItem.getType().name();
            }
            return "null";
        }
    }

    private static class PreviousItemType extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.change/previousItem.type%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
                return (newItem == null) ? "null" : newItem.getType().name();
            }
            return "null";
        }
    }

    private static class NewItemType extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%event.change/newItem%";
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (player == null) {
                return "null";
            }
            if (ChangeHeldItem.playerItemHeldEvent.containsKey(player.getUniqueId())) {
                PlayerItemHeldEvent event = ChangeHeldItem.playerItemHeldEvent.get(player.getUniqueId());
                ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
                return (newItem != null && newItem.hasItemMeta()) ? newItem.getItemMeta().getDisplayName() : "null";
            }
            return "null";
        }
    }
}

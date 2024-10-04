package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.EventType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an action that can be executed by a player.
 */
public interface Action {
    String toString();
    ItemStack getDisplayItem();
    boolean execute(Player player);


    /**
     * Returns a list of events that are allowed for this action.
     * Null if all events are allowed.
     *
     * @return a list of strings representing the allowed events
     */
    default List<EventType> allowedEvents() {
        return null;
    }
}

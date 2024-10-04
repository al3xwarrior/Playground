package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an action that can be executed by a player.
 */
public interface Action {
    String toString();

    ItemStack getDisplayItem();

    boolean execute(Player player, HousingWorld house);


    /**
     * Returns a list of events that are allowed for this action.
     * Null if all events are allowed.
     *
     * @return a list of strings representing the allowed events
     */
    default List<EventType> allowedEvents() {
        return null;
    }

    /**
     * Returns a map of actions that are under this action.
     * <p>
     * Example:
     * Random Action will have a key of "Actions" and then a list of actions under that key.
     * </p>
     *
     * @return a map of strings representing the actions
     */
    default HashMap<String, List<Action>> getActions() {
        return new HashMap<>();
    }
}

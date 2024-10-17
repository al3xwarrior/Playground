package com.al3x.housing2.Action;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an action that can be executed by a player.
 */
public abstract class Action {
    protected String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String toString();

    /*
    New formats for item creation
     */
    public void createDisplayItem(ItemBuilder builder) {
        builder.name("&e" + name);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.changeOrderLore(true);
    }

    public void createAddDisplayItem(ItemBuilder builder) {
        builder.name("&a" + name);
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    public ActionEditor editorMenu(HousingWorld house) {
        return null;
    }

    public abstract boolean execute(Player player, HousingWorld house);

    public abstract HashMap<String, Object> data();

    public void fromData(HashMap<String, Object> data, Class< ? extends Action> actionClass) {
        for (String key : data.keySet()) {
            try {
                Field field = actionClass.getDeclaredField(key);
                field.setAccessible(true);
                if (field.getType().isEnum()) {
                    field.set(this, Enum.valueOf((Class<Enum>) field.getType(), (String) data.get(key)));
                    continue;
                }
                field.set(this, data.get(key));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Returns a list of events that are allowed for this action.
     * Null if all events are allowed.
     *
     * @return a list of strings representing the allowed events
     */
    public List<EventType> allowedEvents() {
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
    public HashMap<String, List<Action>> getActions() {
        return new HashMap<>();
    }
}

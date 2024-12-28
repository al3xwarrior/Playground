package com.al3x.housing2.Condition;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.Actions.CancelAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an action that can be executed by a player.
 */
public abstract class Condition {
    protected String name;

    public Condition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String toString();

    public abstract void createDisplayItem(ItemBuilder builder);

    public abstract void createAddDisplayItem(ItemBuilder builder);

    public ActionEditor editorMenu(HousingWorld house) {
        return null;
    }

    public ActionEditor editorMenu(HousingWorld house, Player player) {
        return null;
    }

    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        return null;
    }

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        return execute(player, house);
    }

    public abstract boolean execute(Player player, HousingWorld house);

    public abstract HashMap<String, Object> data();

    public abstract boolean requiresPlayer();

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
     * Returns a map of conditions that are under this action.
     * <p>
     * Example:
     * Random Action will have a key of "Actions" and then a list of actions under that key.
     * </p>
     *
     * @return a map of strings representing the actions
     */
    public HashMap<String, List<Condition>> getConditions() {
        return new HashMap<>();
    }

    public void fromData(HashMap<String, Object> data, Class< ? extends Condition> condtionClass) {
        for (String key : data.keySet()) {
            try {
                Field field = condtionClass.getDeclaredField(key);
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

}

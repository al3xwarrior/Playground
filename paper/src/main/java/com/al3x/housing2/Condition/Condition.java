package com.al3x.housing2.Condition;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.Actions.CancelAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents an action that can be executed by a player.
 */
public abstract class Condition {
    protected String name;
    public boolean inverted = false;

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
        return editorMenu(house);
    }

    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        return editorMenu(house);
    }

    public ActionEditor editorMenu(HousingWorld house, Player player, Menu backMenu) {
        return editorMenu(house, backMenu);
    }

    public boolean execute(Player player, HousingWorld house, CancellableEvent event) {
        return execute(player, house);
    }

    public boolean execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return execute(player, house, event);
    }

    public abstract boolean execute(Player player, HousingWorld house);

    public abstract LinkedHashMap<String, Object> data();

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
            if (key.equals("inverted")) {
                inverted = (Boolean) data.get(key);
                continue;
            }
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

    public Condition clone() {
        Condition condition;
        ConditionEnum conditionEnum = ConditionEnum.getConditionByName(getName());
        if (conditionEnum == null) {
            return null;
        }
        HashMap<String, Object> data = data();
        data.put("inverted", inverted);
        for (String key : data.keySet()) {
            if (data.get(key) instanceof Enum<?> e) {
                data.put(key, e.name());
            }
        }
        condition = conditionEnum.getConditionInstance(data);
        return condition;
    }
}

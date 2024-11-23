package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.CancelAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

/**
 * Represents an action that can be executed by a player.
 */
public abstract class Action {
    private static final Gson gson = new Gson();
    protected String name;

    public Action(String name) {
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

    public abstract boolean execute(Player player, HousingWorld house);

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        return execute(player, house);
    }

    public abstract HashMap<String, Object> data();

    public abstract boolean requiresPlayer();

    public Object getField(String name) throws NoSuchFieldException, IllegalAccessException, NumberFormatException {
        Field field = this.getClass().getDeclaredField(name.split(" ")[0]);
        field.setAccessible(true);
        if(field.getType() == List.class && name.contains(" ")) {
            return ((List<?>) field.get(this)).get(Integer.parseInt(name.split(" ")[1]));
        }
        return field.get(this);
    }

    public int limit() {
        return -1;
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

    public boolean mustBeSync() {
        return false;
    }

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

    protected <T> List<T> dataToList(JsonArray jsonArray, Class<T> clazz) {
        ArrayList<T> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            actions.add(gson.fromJson(jsonObject, clazz));
        }
        return actions;
    }

    protected boolean getDirection(InventoryClickEvent event, Object obj, HousingWorld house, Menu editMenu, BiConsumer<String, PushDirection> consumer) {
        if (obj instanceof PushDirection direction && direction == PushDirection.CUSTOM) {
            event.getWhoClicked().sendMessage(colorize("&ePlease enter the custom direction in the chat. (pitch,yaw)"));
            editMenu.openChat(Main.getInstance(), (message) -> {
                //pitch,yaw
                String[] split = message.split(",");
                if (split.length != 2) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <pitch>,<yaw>"));
                    return;
                }

                try {
                    //Check if the placeholders are valid
                    Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) event.getWhoClicked(), house, split[0]));
                    Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) event.getWhoClicked(), house, split[1]));
                    consumer.accept(message, PushDirection.CUSTOM);
                } catch (NumberFormatException e) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <pitch>,<yaw>"));
                }
            });
        }

        if ((obj instanceof PushDirection direction) && direction != PushDirection.CUSTOM) {
            consumer.accept(null, direction);
        }
        return false;
    }
}

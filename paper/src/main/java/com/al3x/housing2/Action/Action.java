package com.al3x.housing2.Action;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

import static com.al3x.housing2.Utils.Color.colorize;

/**
 * Represents an action that can be executed by a player.
 */
@Getter
@RequiredArgsConstructor
public abstract class Action {
    private static Gson gson = new GsonBuilder()
            .create();

    private final String id;
    private final String name;
    private final String description;
    private final Material icon;
    private final List<String> scriptingKeywords;

    @Setter
    private String comment;

    private final List<ActionProperty<?>> properties = new ArrayList<>();

    // --- Methods ---

    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return execute(player, house, event);
    }
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event) {
        return execute(player, house);
    }
    public abstract OutputType execute(Player player, HousingWorld house);

    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        return new ActionEditor(4, "§e" + name + " Settings", properties);
    }

    public ItemBuilder createDisplayItem() {
        ItemBuilder builder = new ItemBuilder()
                .material(icon)
                .name("<yellow>" + name)
                .info("&eSettings", "")

                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .rClick(ItemBuilder.ActionType.REMOVE_YELLOW)
                .shiftClick();
        properties.forEach(property -> {
            if (property.getVisible() != null && property.getVisible().apply() && property.displayValue() != null)
                builder.info(property.getName(), property.displayValue());
        });

        if (comment != null && !comment.isEmpty()) builder.extraLore(comment);

        return builder;
    }

    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(icon);
        builder.name("<yellow>" + name);
        builder.description(description);
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> propertiesData = new LinkedHashMap<>();
        properties.forEach(property -> {
            if (property instanceof ActionProperty.PropertySerializer<?,?>) {
                propertiesData.put(property.getId(), ((ActionProperty.PropertySerializer<?, ?>) property).serialize());
            } else {
                propertiesData.put(property.getId(), property.getValue());
            }
        });
        return propertiesData;
    }

    public <V> V getValue(String id, Class<V> clazz) {
        if (clazz.isInstance(ActionProperty.class)) {
            throw new IllegalArgumentException("Cannot use an ActionProperty as a class type");
        }
        for (ActionProperty<?> property : properties) {
            if (property.getId().equals(id)) {
                return (V) property.getValue();
            }
        }
        return null;
    }

    public <V extends ActionProperty<?>> V getProperty(String id, Class<V> clazz) {
        for (ActionProperty<?> property : properties) {
            if (property.getId().equals(id) && property.getClass() == clazz) {
                return (V) property;
            }
        }
        return null;
    }

    public abstract boolean requiresPlayer();

    public int limit() {
        return -1;
    }

    public int nestLimit() {
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

    public List<EventType> disallowedEvents() {
        return null;
    }

    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass, HousingWorld house) {
        house.runOnLoadOrNow((h) -> {
            for (String key : data.keySet()) {
                ActionProperty<?> property = properties.stream()
                        .filter(p     -> p.getId().equals(key))
                        .findFirst()
                        .orElse(null);
                if (property != null) {
                    if (property instanceof ActionProperty.PropertySerializer<?, ?> serializer) {
                        Object value = serializer.deserialize(gson.toJsonTree(data.get(key)), house);
                        if (value == null) {
                            value = serializer.deserialize(data.get(key), house);
                        }
                        property.setValue(value);
                    } else {
                        property.setValue(data.get(key));
                    }
                }
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        if (super.equals(o)) return true;
        return Objects.equals(getName(), action.getName()) && data().equals(action.data());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    public Action clone(HousingWorld house) {
        Action action;
        ActionEnum actionEnum = ActionEnum.getActionById(getId());
        if (actionEnum == null) {
            return null;
        }
        HashMap<String, Object> data = data();
        for (String key : data.keySet()) {
            if (data.get(key) != null && data.get(key).getClass().isEnum()) {
                data.put(key, ((Enum<?>) data.get(key)).name());
            }
        }
        action = actionEnum.getActionInstance(data, this.comment, house);
        return action;
    }
}


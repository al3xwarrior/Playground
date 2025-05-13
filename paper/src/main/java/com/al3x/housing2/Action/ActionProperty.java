package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Properties.*;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Returnable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.al3x.housing2.Action.Action.gson;
import static com.al3x.housing2.Utils.Color.colorize;

@Getter
@Setter
@ToString
public abstract class ActionProperty<V> {
    protected Main main = Main.getInstance();

    private final String id;
    private final String name;
    private final String description;
    private final Material icon;

    private ItemBuilder.ActionType rClick = null;
    private ItemBuilder.ActionType mClick = null;

    @Getter
    public V value;
    private Returnable<Boolean> visible = () -> true;
    private final ItemBuilder builder;

    public ActionProperty(String id, String name, String description, Material icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;

        this.builder = new ItemBuilder()
                .material(icon)
                .name("<yellow>" + name);

        if (description != null) {
            builder.description(description);
        }

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
    }

    public ActionProperty<V> setValue(Object value) {
        this.value = (V) value;
        return this;
    }

    public ActionProperty<V> setValue(V value, Player player) {
        this.value = value;
        player.sendMessage(colorize("&a" + name + " set to: " + value));
        return this;
    }

    protected String displayValue() {
        if (value == null) {
            return "&cNone";
        }
        return getValue().toString();
    }

    public abstract void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu);

    public ItemBuilder getDisplayItem() {
        ItemBuilder builder = getBuilder().clone();
        if (displayValue() != null) {
            builder.info("<yellow>Current value", "")
                    .info(null, "§a" + displayValue());
        }
        return builder;
    }

    public ActionProperty<V> showIf(Returnable<Boolean> condition) {
        this.visible = condition;
        return this;
    }

    public ActionProperty<V> mClick(ItemBuilder.ActionType action) {
        this.mClick = action;
        return this;
    }

    public ActionProperty<V> rClick(ItemBuilder.ActionType action) {
        this.rClick = action;
        return this;
    }

    protected <T> List<T> dataToList(JsonArray jsonArray, Class<T> clazz) {
        ArrayList<T> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            actions.add(gson.fromJson(jsonObject, clazz));
        }
        return actions;
    }

    public <T> T dataToObject(JsonElement jsonElement, Class<T> clazz) {
        return gson.fromJson(jsonElement, clazz);
    }

    public interface PropertySerializer<T, S> {
        S serialize();

        default T deserialize(Object value, HousingWorld house) {
            return null;
        }

        default T deserialize(JsonElement value, HousingWorld house) {
            return null;
        }
    }

    public interface PropertyUpdater<V> {
        V update(HashMap<String, Object> properties, HousingWorld house);
    }

    public static class Constant {

    }
}

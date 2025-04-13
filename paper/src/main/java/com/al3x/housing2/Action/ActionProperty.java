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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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
    private V value;
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
    }

    public ActionProperty<V> setValue(V value) {
        this.value = value;
        return this;
    }

    public ActionProperty<V> setValue(V value, Player player) {
        this.value = value;
        player.sendMessage(colorize("&a" + name + " set to: " + value));
        return this;
    }

    protected String displayValue() {
        return getValue().toString();
    }

    public abstract void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu);
    public ItemBuilder getDisplayItem() {
        ItemBuilder builder = getBuilder().clone();
        if (displayValue() != null) {
            builder.info("<yellow>Current value", "")
                    .info(null, displayValue());
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

    public interface PropertySerializer<T, S> {
        S serialize();

        T deserialize(S value, HousingWorld house);
    }

    public static class Constant {

    }
}

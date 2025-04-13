package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PaginationProperty<T> extends ActionProperty<T> implements ActionProperty.PropertySerializer<T, String> {

    public PaginationProperty(String id, String name, String description, Material icon) {
        super(id, name, description, icon);
    }

    protected abstract List<T> getItems(HousingWorld house);

    protected abstract ItemBuilder buildItem(T item);

    protected abstract void onSelect(T item, Player player);

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        List<Duple<T, ItemBuilder>> items = getItems(house).stream()
            .map(item -> new Duple<>(item, buildItem(item)))
            .collect(Collectors.toList());

        PaginationMenu<T> paginationMenu = new PaginationMenu<>(
            main,
            "Select " + getName(),
            items,
            player,
            house,
            menu,
            (item) -> {
                onSelect(item, player);
                setValue(item, player);
            }
        );
        paginationMenu.open();
    }

    @Override
    protected String displayValue() {
        return getValue() != null ? getValue().toString() : "&cNone";
    }

    @Override
    public String serialize() {
        return getValue().toString(); // Override if needed
    }

    @Override
    public T deserialize(String value, HousingWorld house) {
        return getItems(house).stream()
            .filter(item -> item.toString().equals(value))
            .findFirst()
            .orElse(null);
    }
}
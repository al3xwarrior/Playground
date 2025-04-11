package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Actions.ActionEnumMenu;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EnumProperty<E extends Enum<E>> extends ActionProperty<E> implements ActionProperty.PropertySerializer<E, String> {
    private final Class<E> enumClass;

    public EnumProperty(String id, String name, String description, Class<E> enumClass) {
        super(id, name, description, Material.BOOK);
        this.enumClass = enumClass;

        getBuilder().lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new EnumMenu<>(main, "Select a " + getName(),
                enumClass.getEnumConstants(),
                Material.BARRIER, player, house, menu,
                (enumValue) -> {
                    setValue(enumValue);
                    menu.open();
                }
        ).open();
    }

    @Override
    public String serialize() {
        return getValue().name();
    }

    @Override
    public E deserialize(String value, HousingWorld house) {
        return Enum.valueOf(enumClass, value);
    }
}

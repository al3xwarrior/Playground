package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EnumProperty<E extends Enum<E>> extends ActionProperty<E> implements ActionProperty.PropertySerializer<E, String> {
    private final Class<E> enumClass;

    public EnumProperty(String id, String name, String description, Class<E> enumClass, Material material) {
        super(id, name, description, material);
        this.enumClass = enumClass;

        getBuilder().lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
    }

    @Override
    protected String displayValue() {
        if (getValue() == null) {
            return "&cNo value";
        }
        return "&a" + StringUtilsKt.formatCapitalize(getValue().name());
    }

    public EnumProperty(String id, String name, String description, Class<E> enumClass) {
        this(id, name, description, enumClass, Material.COMPASS);
    }

    @Override
    public ItemBuilder getDisplayItem() {
        ItemBuilder builder = super.getDisplayItem();
        if (getValue() instanceof EnumMaterial material) {
            builder.material(material.getMaterial());
        }
        return builder;
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new EnumMenu<>(main, "Select a " + getName(),
                enumClass.getEnumConstants(),
                Material.BARRIER, player, house, menu,
                (enumValue) -> {
                    setValue(enumValue, player);
                    menu.open();
                }
        ).open();
    }

    @Override
    public String serialize() {
        return getValue().name();
    }

    @Override
    public E deserialize(Object value, HousingWorld housingWorld) {
        return Enum.valueOf(enumClass, value.toString());
    }
}

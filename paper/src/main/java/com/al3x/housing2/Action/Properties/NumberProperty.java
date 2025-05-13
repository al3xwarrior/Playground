package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class NumberProperty extends ActionProperty<String> implements ActionProperty.PropertySerializer<String, String> {
    private final double min;
    private final double max;

    public NumberProperty(String id, String name, String description) {
        this(id, name, description, 0, Double.MAX_VALUE);
    }

    public NumberProperty(String id, String name, String description, double min, double max) {
        super(id, name, description, Material.REPEATER);
        this.min = min;
        this.max = max;
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        player.sendMessage("§eEnter a value:");
        menu.openChat(main, getValue(), message -> {
            if (NumberUtilsKt.isDouble(message)) {
                double value = Double.parseDouble(message);

                if (value < getMin()) value = getMin();
                if (value > getMax()) value = getMax();

                setValue(String.valueOf(value), player);
                return;
            }
            setValue(message, player);
        });
    }

    public Double parsedValue(HousingWorld house, Player player) {
        if (getValue() == null) return 0D;
        String value = Placeholder.handlePlaceholders(getValue(), house, player);
        try {
            double parsedValue = Double.parseDouble(value);
            if (parsedValue < getMin()) parsedValue = getMin();
            if (parsedValue > getMax()) parsedValue = getMax();
            return parsedValue;
        } catch (NumberFormatException e) {
            return 0D;
        }
    }

    @Override
    public String serialize() {
        return getValue();
    }

    @Override
    public String deserialize(Object value, HousingWorld house) {
        if (value instanceof Double) {
            return String.valueOf(value);
        } else if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }
}

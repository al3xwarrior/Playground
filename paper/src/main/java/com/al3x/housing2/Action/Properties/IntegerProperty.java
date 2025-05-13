package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class IntegerProperty extends ActionProperty<Integer> implements ActionProperty.PropertySerializer<Integer, Object> {
    private final int min;
    private final int max;

    public IntegerProperty(String id, String name, String description) {
        this(id, name, description, 0, Integer.MAX_VALUE);
    }

    public IntegerProperty(String id, String name, String description, int min, int max) {
        super(id, name, description, Material.IRON_INGOT);
        this.min = min;
        this.max = max;
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        player.sendMessage("§eEnter a integer between " + getMin() + " and " + getMax() + ":");
        menu.openChat(main, getValue().toString(), (message) -> {
            try {
                int value = Integer.parseInt(message);
                if (value < getMin() || value > getMax()) {
                    player.sendMessage("§cValue must be between " + getMin() + " and " + getMax());
                    return;
                }
                setValue(value, player);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid number format.");
            }
        });
    }

    @Override
    public Object serialize() {
        return getValue();
    }

    @Override
    public Integer deserialize(Object value, HousingWorld house) {
        if (value instanceof Double) {
            return ((Double) value).intValue();
        } else if (value instanceof Integer) {
            return (Integer) value;
        }

        return Integer.parseInt(value.toString());
    }
}

package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class DoubleProperty extends ActionProperty<Double> {
    private final double min;
    private final double max;

    public DoubleProperty(String id, String name, String description) {
        this(id, name, description, 0, Double.MAX_VALUE);
    }

    public DoubleProperty(String id, String name, String description, double min, double max) {
        super(id, name, description, Material.REPEATER);
        this.min = min;
        this.max = max;
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        player.sendMessage("§eEnter a double between " + getMin() + " and " + getMax() + ":");
        menu.openChat(main, getValue().toString(), (message) -> {
            try {
                double value = Double.parseDouble(message);
                if (value < getMin() || value > getMax()) {
                    player.sendMessage("§cValue must be between " + getMin() + " and " + getMax());
                    return;
                }
                setValue(value, player);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid number format.");
                return;
            }
        });
    }
}

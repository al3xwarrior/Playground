package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BooleanProperty extends ActionProperty<Boolean> {
    public BooleanProperty(String id, String name, String description) {
        super(id, name, description, Material.TURTLE_SCUTE);

        getBuilder().lClick(ItemBuilder.ActionType.TOGGLE_YELLOW);
    }

    @Override
    protected String displayValue() {
        return getValue() ? "§aEnabled" : "§cDisabled";
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        setValue(!getValue());
    }
}

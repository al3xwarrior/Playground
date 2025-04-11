package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StringProperty extends ActionProperty<String> {
    public StringProperty(String id, String name, String description) {
        super(id, name, description, Material.STRING);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        menu.openChat(main, getValue(), (message) -> setValue(message, player));
    }

    public String parsedValue(HousingWorld house, Player player) {
        return Placeholder.handlePlaceholders(getValue(), house, player);
    }
}

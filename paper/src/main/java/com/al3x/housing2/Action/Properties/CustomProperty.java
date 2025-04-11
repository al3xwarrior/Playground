package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class CustomProperty<V> extends ActionProperty<V> {
    public CustomProperty(String id, String name, String description, Material icon) {
        super(id, name, description, icon);
    }
}

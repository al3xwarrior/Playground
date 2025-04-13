package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AddStatInstanceProperty extends CustomSlotProperty<ActionProperty.Constant> {
    public AddStatInstanceProperty(int customSlot) {
        super(
             "addStatInstance",
                "Add Stat Instance",
                "Add a new stat expression instance.\n\nBasically adds a new mode and value to the expression.",
                Material.PAPER
                , customSlot);
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        Action action = menu.getAction();
        StatInstanceProperty property = action.getProperty("statInstances", StatInstanceProperty.class);
        if (property != null) {
            if (property.getValue().size() >= 6) {
                player.sendMessage("§cYou can only have 6 stat instances.");
                return;
            }
            property.getValue().add(new StatInstance());
            menu.open();
        }
    }
}

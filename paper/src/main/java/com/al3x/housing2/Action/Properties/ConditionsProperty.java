package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.ConditionalData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ConditionsProperty extends ActionProperty<List<Condition>> implements ActionProperty.PropertySerializer<List<Condition>, List<ConditionalData>> {
    public ConditionsProperty(String id, String name, String description) {
        super(id, name, description, Material.REDSTONE);
    }

    @Override
    protected String displayValue() {
        return getValue().size() + " conditions";
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new ActionsMenu(main, player, house, getValue(), menu, true).open();
    }

    @Override
    public List<ConditionalData> serialize() {
        return ConditionalData.fromList(getValue());
    }

    @Override
    public List<Condition> deserialize(List<ConditionalData> value, HousingWorld house) {
        return ConditionalData.toList(value);
    }
}

package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class ActionsProperty extends ActionProperty<List<Action>> implements ActionProperty.PropertySerializer<List<Action>, List<ActionData>> {
    public ActionsProperty(String id, String name, String description) {
        super(id, name, description, Material.PISTON);

        setValue(new ArrayList<>());
    }

    @Override
    protected String displayValue() {
        if (getValue() == null || getValue().isEmpty()) {
            return "&cNo actions";
        }
        return "&a" + getValue().size() + " actions";
    }
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new ActionsMenu(main, player, house, getValue(), menu, getId()).open();
    }

    @Override
    public List<ActionData> serialize() {
        return ActionData.fromList(getValue());
    }

    @Override
    public List<Action> deserialize(JsonElement value, HousingWorld housingWorld) {
        List<ActionData> data = dataToList(value.getAsJsonArray(), ActionData.class);
        return ActionData.toList(data, housingWorld);
    }
}

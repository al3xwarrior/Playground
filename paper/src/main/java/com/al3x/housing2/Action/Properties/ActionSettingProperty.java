package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ActionSettingProperty extends ActionProperty<Action> implements ActionProperty.PropertySerializer<Action, ActionData> {
    public ActionSettingProperty(String id, String name, String description) {
        super(id, name, description, Material.COMPARATOR);
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        new ActionEditMenu(getValue(), main, player, house, menu).open();
    }

    @Override
    public ActionData serialize() {
        return ActionData.toData(getValue());
    }

    @Override
    public Action deserialize(JsonElement value, HousingWorld housingWorld) {
        ActionData data = dataToObject(value, ActionData.class);
        return ActionData.fromData(data, housingWorld);
    }
}



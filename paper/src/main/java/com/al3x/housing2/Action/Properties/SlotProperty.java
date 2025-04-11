package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.SlotSelectMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SlotProperty extends ActionProperty<Integer> {
    public SlotProperty(String id, String name, String description) {
        super(id, name, description, Material.ARMOR_STAND);
    }

    @Override
    protected String displayValue() {
        return slotIndexToName(getValue());
    }

    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu backMenu) {
        new SlotSelectMenu(player, main, backMenu, this::setValue).open();
    }

    private String slotIndexToName(int index) {
        if (index == -1) {
            return "First Available Slot";
        }
        if (index == -2) {
            return "Hand Slot";
        }
        if (index == -3 || index == -106) {
            return "Offhand Slot";
        }
        if (index < 9 && index >= 0) {
            return "Inventory Slot " + (index + 1);
        }
        if (index < 36 && index >= 9) {
            return "Inventory Slot " + (index - 8);
        }
        if (index == 103) {
            return "Helmet";
        }
        if (index == 102) {
            return "Chestplate";
        }
        if (index == 101) {
            return "Leggings";
        }
        if (index == 100) {
            return "Boots";
        }
        if (index >= 80 && index <= 83) {
            return "Crafting Slot " + (index - 79);
        }
        return "Unknown Slot";
    }
}

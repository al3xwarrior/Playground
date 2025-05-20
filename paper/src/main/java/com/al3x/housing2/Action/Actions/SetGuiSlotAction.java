package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ActionsProperty;
import com.al3x.housing2.Action.Properties.GenericPagination.MenuProperty;
import com.al3x.housing2.Action.Properties.ItemStackProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Action.Properties.SlotProperty;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetGuiSlotAction extends HTSLImpl {
    public SetGuiSlotAction() {
        super(
                ActionEnum.SET_GUI_SLOT,
                "Set Gui Slot",
                "Set the gui slot of a custom menu.",
                Material.CHEST_MINECART,
                List.of("guiSlot")
        );
        getProperties().addAll(List.of(
                new MenuProperty(
                        "menu",
                        "Menu",
                        "The menu to set the slot in."
                ),
                new NumberProperty(
                        "slot",
                        "Slot",
                        "The slot to set the item in."
                ).setValue("1"),
                new ItemStackProperty(
                        "item",
                        "Item",
                        "The item to set in the slot."
                ),
                new ActionsProperty(
                        "actions",
                        "Actions",
                        "The actions to execute when the item is clicked."
                )
        ));
    }

    public SetGuiSlotAction(CustomMenu menu) {
        this();
        getProperty("menu", MenuProperty.class).setValue(menu);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        CustomMenu menu = getValue("menu", CustomMenu.class);
        int slot = getProperty("slot", NumberProperty.class).parsedValue(house, player).intValue();
        ItemStack item = getValue("item", ItemStack.class);
        List<Action> actions = getProperty("actions", ActionsProperty.class).getValue();
        if (menu == null) {
            return OutputType.ERROR;
        }
        if (slot < 1 || slot > menu.getRows() * 9) {
            return OutputType.ERROR;
        }
        if (item == null) {
            return OutputType.ERROR;
        }

        menu.getItems().set(slot - 1, new Duple<>(item, actions));
        return OutputType.SUCCESS;
    }

    @Override
    public List<EventType> allowedEvents() {
        return List.of(EventType.INVENTORY_CLICK);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

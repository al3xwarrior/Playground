package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.ItemStackProperty;
import com.al3x.housing2.Action.Properties.SlotProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.SlotSelectMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@ToString
public class RemoveItemAction extends Action {
    public RemoveItemAction() {
        super("remove_item_action",
                "Remove Item",
                "Removes an item from the player's inventory.",
                Material.CAULDRON,
                List.of("remove_item")
        );

        getProperties().addAll(List.of(
                new ItemStackProperty(
                        "item",
                        "Item",
                        "The item to remove."
                ),
                new SlotProperty(
                        "slot",
                        "Slot",
                        "The slot to remove the item from."
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        ItemStack item = getValue("item", ItemStackProperty.class).getValue();
        int slot = getValue("slot", SlotProperty.class).getValue();

        if (slot == -1) {
            player.getInventory().removeItemAnySlot(item);
        } else if (slot == -2) {
            player.getInventory().setItemInMainHand(null);
        } else if (slot == -3) {
            player.getInventory().setItemInOffHand(null);
        } else {
            player.getInventory().setItem((int) slot, null);
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

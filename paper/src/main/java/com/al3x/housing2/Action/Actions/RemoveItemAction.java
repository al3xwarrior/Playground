package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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
    ItemStack item = null;
    double slot = -1;
    public RemoveItemAction() {
        super("remove_item_action",
                "Remove Item",
                "Removes an item from the player's inventory.",
                Material.CAULDRON,
                List.of("remove_item")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "item",
                        "Item",
                        "The item to remove.",
                        ActionProperty.PropertyType.ITEM
                ),
                new ActionProperty(
                        "slot",
                        "Slot",
                        "The slot to remove the item from.",
                        ActionProperty.PropertyType.SLOT
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (item == null) return OutputType.ERROR;

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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("item", Serialization.itemStackToBase64(item));
        data.put("slot", slot);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        try {
            item = Serialization.itemStackFromBase64((String) data.get("item"));
        } catch (IOException e) {
            e.printStackTrace();
            Main.getInstance().getLogger().warning("Failed to load item from base64 string");
        }
        slot = (double) data.get("slot");
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.SlotSelectMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RemoveItemAction extends Action {
    ItemStack item;
    double slot = -1;
    public RemoveItemAction() {
        super("Remove Item Action");
    }

    @Override
    public String toString() {
        return "RemoveItemAction{" +
                "item=" + item +
                ", slot=" + slot +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CAULDRON);
        builder.name("&eRemove Item");
        builder.info("&eSettings", "");
        builder.info("Item", (item == null ? "&cNone" : "&6" + item.getType()));
        builder.info("Slot", slotIndexToName((int) slot));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CAULDRON);
        builder.name("&aRemove Item");
        builder.description("Remove an item from the player's inventory.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("item", ItemBuilder.create((item == null ? Material.BOOK : item.getType()))
                        .name("&aItem")
                        .description("Select a item to give")
                        .info("&7Current Value", "")
                        .info(null, (item == null ? "&cNone" : "&6" + StackUtils.getDisplayName(item))),
                        ActionEditor.ActionItem.ActionType.ITEM
                ),
                new ActionEditor.ActionItem("slot", ItemBuilder.create(Material.CHEST)
                        .name("&aSlot")
                        .description("Select a slot to give the item in")
                        .info("&7Current Value", "")
                        .info(null, slotIndexToName((int) slot)),
                        (e, o) -> {
                            new SlotSelectMenu((Player) e.getWhoClicked(), Main.getInstance(), backMenu, true, (slot) -> {
                                this.slot = slot;
                            }).open();
                            return true;
                        }
                )
        );
        return new ActionEditor(4, "&eFunction Action Settings", items);
    }

    private String slotIndexToName(int index) {
        if (index == -1) {
            return "Any Slot";
        }
        if (index == -2) {
            return "Hand Slot";
        }
        if (index == -3 || index == -106) {
            return "Offhand Slot";
        }
        if (index < 9 && index >= 0) {
            return "Hotbar Slot " + (index + 1);
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

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (item == null) return true;

        if (slot == -1) {
            player.getInventory().removeItemAnySlot(item);
        } else if (slot == -2) {
            player.getInventory().setItemInMainHand(null);
        } else if (slot == -3) {
            player.getInventory().setItemInOffHand(null);
        } else {
            player.getInventory().setItem((int) slot, null);
        }
        return true;
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

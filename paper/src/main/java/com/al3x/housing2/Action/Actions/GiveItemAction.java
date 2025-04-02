package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.OutputType;
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
public class GiveItemAction extends Action {
    ItemStack item;
    boolean allowMultiple = false;
    double slot = -1;
    boolean replaceExistingSlot = false;

    public GiveItemAction() {
        super(
                "give_item_action",
                "Give Item",
                "Gives an item to the player.",
                Material.CHEST,
                List.of("giveItem")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "item",
                        "Item",
                        "The item to give",
                        ActionProperty.PropertyType.ITEM
                ),
                new ActionProperty(
                        "allowMultiple",
                        "Allow Multiple",
                        "If true, the player can have multiple copies of the item.",
                        ActionProperty.PropertyType.BOOLEAN
                ),
                new ActionProperty(
                        "slot",
                        "Slot",
                        "The slot to give the item to. -1 for first available slot, -2 for hand slot, -3 for offhand slot.",
                        ActionProperty.PropertyType.NUMBER // FIXME
                ),
                new ActionProperty(
                        "replaceExistingSlot",
                        "Replace Existing Slot",
                        "If true, the item will replace any existing item in the slot.",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));
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
    public OutputType execute(Player player, HousingWorld house) {
        if (item == null) return OutputType.ERROR;
        int slot = (int) this.slot;

        if (player.getInventory().contains(item) && !allowMultiple) {
            return OutputType.ERROR;
        }

        if (slot == -2) {
            if (!player.getInventory().getItemInMainHand().isEmpty() && !replaceExistingSlot) {
                return OutputType.ERROR;
            }
            player.getInventory().setItemInMainHand(item);
            return OutputType.SUCCESS;
        }
        if (slot == -106 || slot == -3) {
            if (!player.getInventory().getItemInOffHand().isEmpty() && !replaceExistingSlot) {
                return OutputType.ERROR;
            }
            player.getInventory().setItemInOffHand(item);
            return OutputType.SUCCESS;
        }
        if (slot == -1) {
            player.getInventory().addItem(item);
            return OutputType.SUCCESS;
        }
        if (slot >= 0 && slot < 36) {
            if (player.getInventory().getItem(slot) == null || replaceExistingSlot) {
                player.getInventory().setItem(slot, item);
                return OutputType.SUCCESS;
            }
            return OutputType.SUCCESS;
        }
        if (slot == 100) {
            if (player.getInventory().getBoots() == null || replaceExistingSlot) {
                player.getInventory().setBoots(item);
                return OutputType.SUCCESS;
            }
            return OutputType.SUCCESS;
        }
        if (slot == 101) {
            if (player.getInventory().getLeggings() == null || replaceExistingSlot) {
                player.getInventory().setLeggings(item);
                return OutputType.SUCCESS;
            }
            return OutputType.SUCCESS;
        }
        if (slot == 102) {
            if (player.getInventory().getChestplate() == null || replaceExistingSlot) {
                player.getInventory().setChestplate(item);
                return OutputType.SUCCESS;
            }
            return OutputType.SUCCESS;
        }
        if (slot == 103) {
            if (player.getInventory().getHelmet() == null || replaceExistingSlot) {
                player.getInventory().setHelmet(item);
                return OutputType.SUCCESS;
            }
            return OutputType.SUCCESS;
        }
        if (slot >= 80 && slot <= 83) {
            if (player.getInventory().getItem(slot) == null || replaceExistingSlot) {
                player.getInventory().setItem(slot, item);
                return OutputType.SUCCESS;
            }
            return OutputType.SUCCESS;
        }
        return OutputType.SUCCESS;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        try {
            item = Serialization.itemStackFromBase64((String) data.get("item"));
        } catch (IOException e) {
            e.printStackTrace();
            Main.getInstance().getLogger().warning("Failed to load item from base64 string");
        }
        allowMultiple = (boolean) data.get("allowMultiple");
        slot = (double) data.get("slot");
        replaceExistingSlot = (boolean) data.get("replaceExistingSlot");
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

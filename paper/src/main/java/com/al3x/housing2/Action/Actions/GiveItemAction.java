package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.BooleanProperty;
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
public class GiveItemAction extends Action {
    public GiveItemAction() {
        super(
                "give_item_action",
                "Give Item",
                "Gives an item to the player.",
                Material.CHEST,
                List.of("giveItem")
        );

        getProperties().addAll(List.of(
                new ItemStackProperty(
                        "item",
                        "Item",
                        "The item to give"
                ),
                new BooleanProperty(
                        "allowMultiple",
                        "Allow Multiple",
                        "If true, the player can have multiple copies of the item."
                ).setValue(true),
                new SlotProperty(
                        "slot",
                        "Slot",
                        "The slot to give the item to. -1 for first available slot, -2 for hand slot, -3 for offhand slot."
                ).setValue(-1),
                new BooleanProperty(
                        "replaceExistingSlot",
                        "Replace Existing Slot",
                        "If true, the item will replace any existing item in the slot."
                ).setValue(false)
        ));
    }


    @Override
    public OutputType execute(Player player, HousingWorld house) {
        ItemStack item = getProperty("item", ItemStackProperty.class).getValue();
        boolean allowMultiple = getProperty("allowMultiple", BooleanProperty.class).getValue();
        int slot = getProperty("slot", SlotProperty.class).getValue();
        boolean replaceExistingSlot = getProperty("replaceExistingSlot", BooleanProperty.class).getValue();

        if (item == null) return OutputType.ERROR;

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
    public boolean requiresPlayer() {
        return true;
    }
}

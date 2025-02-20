package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.FunctionData;
import com.al3x.housing2.Instances.HousingData.HouseData;
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
import java.util.*;

public class GiveItemAction extends Action {
    ItemStack item;
    boolean allowMultiple;
    double slot = -1;
    boolean replaceExistingSlot;
    public GiveItemAction() {
        super("Give Item Action");
    }

    @Override
    public String toString() {
        return "GiveItemAction{" +
                "item=" + item +
                ", allowMultiple=" + allowMultiple +
                ", slot=" + slot +
                ", replaceExistingSlot=" + replaceExistingSlot +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&eGive Item");
        builder.info("&eSettings", "");
        builder.info("Item", (item == null ? "&cNone" : "&6" + item.getType()));
        builder.info("Allow Multiple", allowMultiple ? "&aYes" : "&cNo");
        builder.info("Slot", slotIndexToName((int) slot));
        builder.info("Replace Existing Slot", replaceExistingSlot ? "&aEnabled" : "&cDisabled");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&aGive Item");
        builder.description("Gives an item to the player");
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
                new ActionEditor.ActionItem("allowMultiple", ItemBuilder.create((allowMultiple ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aAllow Multiple")
                        .info("&7Current Value", "")
                        .info(null, allowMultiple ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("slot", ItemBuilder.create(Material.CHEST)
                        .name("&aSlot")
                        .description("Select a slot to give the item to")
                        .info("&7Current Value", "")
                        .info(null, slotIndexToName((int) slot)),
                        (e, slot) -> {
                            SlotSelectMenu menu = new SlotSelectMenu((Player) e.getWhoClicked(), Main.getInstance(), backMenu, (selectedSlot) -> {
                                this.slot = selectedSlot;
                            });
                            menu.open();
                            return true;
                        }
                ),
                new ActionEditor.ActionItem("replaceExistingSlot", ItemBuilder.create((replaceExistingSlot ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aReplace Existing Slot")
                        .description("When enabled, if an item currently occupies the inventory slot it will be replaced with the new item.")
                        .info("&7Current Value", "")
                        .info(null, replaceExistingSlot ? "&aEnabled" : "&cDisabled"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&eGive Item Action Settings", items);
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
    public boolean execute(Player player, HousingWorld house) {
        if (item == null) return true;
        int slot = (int) this.slot;

        if (player.getInventory().contains(item) && !allowMultiple) {
            return true;
        }

        if (slot == -2) {
            if (!player.getInventory().getItemInMainHand().isEmpty() && !replaceExistingSlot) {
                return true;
            }
            player.getInventory().setItemInMainHand(item);
            return true;
        }
        if (slot == -106) {
            if (!player.getInventory().getItemInOffHand().isEmpty() && !replaceExistingSlot) {
                return true;
            }
            player.getInventory().setItemInOffHand(item);
            return true;
        }
        if (slot == -1) {
            player.getInventory().addItem(item);
            return true;
        }
        if (slot >= 0 && slot < 36) {
            if (player.getInventory().getItem(slot) == null || replaceExistingSlot) {
                player.getInventory().setItem(slot, item);
                return true;
            }
            return true;
        }
        if (slot == 100) {
            if (player.getInventory().getBoots() == null || replaceExistingSlot) {
                player.getInventory().setBoots(item);
                return true;
            }
            return true;
        }
        if (slot == 101) {
            if (player.getInventory().getLeggings() == null || replaceExistingSlot) {
                player.getInventory().setLeggings(item);
                return true;
            }
            return true;
        }
        if (slot == 102) {
            if (player.getInventory().getChestplate() == null || replaceExistingSlot) {
                player.getInventory().setChestplate(item);
                return true;
            }
            return true;
        }
        if (slot == 103) {
            if (player.getInventory().getHelmet() == null || replaceExistingSlot) {
                player.getInventory().setHelmet(item);
                return true;
            }
            return true;
        }
        if (slot >= 80 && slot <= 83) {
            if (player.getInventory().getItem(slot) == null || replaceExistingSlot) {
                player.getInventory().setItem(slot, item);
                return true;
            }
            return true;
        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("item", Serialization.itemStackToBase64(item));
        data.put("allowMultiple", allowMultiple);
        data.put("slot", slot);
        data.put("replaceExistingSlot", replaceExistingSlot);
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
        allowMultiple = (boolean) data.get("allowMultiple");
        slot = (double) data.get("slot");
        replaceExistingSlot = (boolean) data.get("replaceExistingSlot");
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.SlotSelectMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HasItemCondition extends Condition {
    private ItemStack item = null;
    private WhatToCheck whatToCheck = WhatToCheck.METADATA;
    private WhereToCheck whereToCheck = WhereToCheck.ANYWHERE;
    private int customSlot = -1;
    private Amount amount = Amount.ANY_AMOUNT;


    public HasItemCondition() {
        super("Has Item");
    }

    @Override
    public String toString() {
        return "idk";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&aHas Item");
        builder.description("Requires the user to have the specified item.");
        builder.info("&eSettings", "");
        builder.info("Item", (item == null ? "&cNone" : "&6" + item.getType().name()));
        builder.info("What to check", "&6" + StringUtilsKt.formatCapitalize(whatToCheck.name()));
        builder.info("Where to check", "&6" + StringUtilsKt.formatCapitalize(whereToCheck.name()));
        builder.info("Amount", "&6" + StringUtilsKt.formatCapitalize(amount.name()));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&eHas Item");
        builder.description("Requires the user to have the specified item.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("item",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eItem")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (item == null ? "&aNot Set" : StackUtils.getDisplayName(item)))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ITEM
                ),
                new ActionEditor.ActionItem("whatToCheck",
                        ItemBuilder.create(whatToCheck.getMaterial())
                                .name("&eWhat to check")
                                .info("&7Current Value", "")
                                .info(null, "&a" + StringUtilsKt.formatCapitalize(whatToCheck.name()))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, WhatToCheck.values(), null
                ),
                new ActionEditor.ActionItem("whereToCheck",
                        ItemBuilder.create(whereToCheck.getMaterial())
                                .name("&eWhere to check")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (whereToCheck == WhereToCheck.CUSTOM ? slotIndexToName(customSlot) : StringUtilsKt.formatCapitalize(whereToCheck.name())))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, WhereToCheck.values(), null, (e, o) -> {
                    whereToCheck = (WhereToCheck) o;
                    e.getWhoClicked().sendMessage(Component.text("Set Where to check to " + StringUtilsKt.formatCapitalize(whereToCheck.name())).color(NamedTextColor.YELLOW));
                    if (o == WhereToCheck.CUSTOM) {
                        new SlotSelectMenu((Player) e.getWhoClicked(), Main.getInstance(), editMenu, false, (slot) -> {
                            customSlot = slot;
                            whereToCheck = WhereToCheck.CUSTOM;
                            editMenu.open();
                        }).open();
                    }
                    return false;
                }
                ),
                new ActionEditor.ActionItem("amount",
                        ItemBuilder.create(amount.getMaterial())
                                .name("&eRequired Amount")
                                .info("&7Current Value", "")
                                .info(null, "&a" + StringUtilsKt.formatCapitalize(amount.name()))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Amount.values(), null
                )
        );
        return new ActionEditor(4, "Settings", items);
    }

    private String slotIndexToName(int index) {
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
        if (item == null) return false;
        switch (whereToCheck) {
            case CUSTOM: {
                if (customSlot == -1) return false;
                if (checkItem(player.getInventory().getItem(customSlot))) {
                    return checkAmount(player.getInventory().getItem(customSlot), item.getAmount());
                }
                break;
            }
            case HAND: {
                if (checkItem(player.getInventory().getItemInMainHand())) {
                    return checkAmount(player.getInventory().getItemInMainHand(), item.getAmount());
                }
                break;
            }
            case OFFHAND: {
                if (checkItem(player.getInventory().getItemInOffHand())) {
                    return checkAmount(player.getInventory().getItemInOffHand(), item.getAmount());
                }
                break;
            }
            case ARMOR: {
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (checkItem(armor)) {
                        return checkAmount(armor, item.getAmount());
                    }
                }
                break;
            }
            case HOTBAR: {
                for (int i = 0; i < 9; i++) {
                    if (checkItem(player.getInventory().getItem(i))) {
                        return checkAmount(player.getInventory().getItem(i), item.getAmount());
                    }
                }
                break;
            }
            case INVENTORY: {
                for (ItemStack inventory : player.getInventory().getContents()) {
                    if (checkItem(inventory)) {
                        return checkAmount(inventory, item.getAmount());
                    }
                }
                break;
            }
            case ANYWHERE: {
                for (ItemStack inventory : player.getInventory().getContents()) {
                    if (checkItem(inventory)) {
                        return checkAmount(inventory, item.getAmount());
                    }
                }
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (checkItem(armor)) {
                        return checkAmount(armor, item.getAmount());
                    }
                }
                for (int i = 0; i < 9; i++) {
                    if (checkItem(player.getInventory().getItem(i))) {
                        return checkAmount(player.getInventory().getItem(i), item.getAmount());
                    }
                }
                if (checkItem(player.getInventory().getItemInMainHand())) {
                    return checkAmount(player.getInventory().getItemInMainHand(), item.getAmount());
                }
                if (checkItem(player.getInventory().getItemInOffHand())) {
                    return checkAmount(player.getInventory().getItemInOffHand(), item.getAmount());
                }
                break;
            }
        }
        return false;
    }

    private boolean checkItem(ItemStack item) {
        if (item == null) return false;
        if (whatToCheck == WhatToCheck.ITEM_TYPE) {
            return item.getType() == this.item.getType();
        } else if (whatToCheck == WhatToCheck.METADATA) {
            return item.isSimilar(this.item);
        }
        return false;
    }

    private boolean checkAmount(ItemStack item, int amount) {
        switch (this.amount) {
            case ANY_AMOUNT: {
                return true;
            }
            case LESS: {
                return item.getAmount() < amount;
            }
            case GREATER: {
                return item.getAmount() > amount;
            }
            case EQUAL: {
                return item.getAmount() == amount;
            }
            case GREATER_OR_EQUAL: {
                return item.getAmount() >= amount;
            }
            case LESS_OR_EQUAL: {
                return item.getAmount() <= amount;
            }
        }
        return false;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("item", Serialization.itemStackToBase64(item));
        data.put("whatToCheck", whatToCheck);
        data.put("whereToCheck", whereToCheck);
        data.put("customSlot", customSlot);
        data.put("amount", amount);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Condition> condtionClass) {
        try {
            item = Serialization.itemStackFromBase64((String) data.get("item"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        whatToCheck = WhatToCheck.valueOf((String) data.get("whatToCheck"));
        whereToCheck = WhereToCheck.valueOf((String) data.get("whereToCheck"));
        customSlot = ((Double) data.get("customSlot")).intValue();
        amount = Amount.valueOf((String) data.get("amount"));
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    private enum WhatToCheck implements EnumMaterial {
        ITEM_TYPE(Material.MAP),
        METADATA(Material.FILLED_MAP)
        ;

        private Material material;
        WhatToCheck(Material material) {
            this.material = material;
        }

        @Override
        public Material getMaterial() {
            return material;
        }
    }

    private enum WhereToCheck implements EnumMaterial {
        HAND(Material.STONE_SWORD),
        OFFHAND(Material.SHIELD),
        ARMOR(Material.LEATHER_CHESTPLATE),
        HOTBAR(Material.PAPER),
        INVENTORY(Material.CHEST),
        ANYWHERE(Material.NETHER_STAR),
        CUSTOM(Material.BOOK)
        ;

        private Material material;
        WhereToCheck(Material material) {
            this.material = material;
        }

        @Override
        public Material getMaterial() {
            return material;
        }
    }

    private enum Amount implements EnumMaterial {
        ANY_AMOUNT(Material.WHITE_STAINED_GLASS),
        LESS(Material.RED_STAINED_GLASS),
        GREATER(Material.LIME_STAINED_GLASS),
        EQUAL(Material.YELLOW_STAINED_GLASS),
        GREATER_OR_EQUAL(Material.LIME_STAINED_GLASS),
        LESS_OR_EQUAL(Material.RED_STAINED_GLASS)
        ;

        private Material material;
        Amount(Material material) {
            this.material = material;
        }

        @Override
        public Material getMaterial() {
            return material;
        }
    }
}

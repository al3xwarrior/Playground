package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.IntegerProperty;
import com.al3x.housing2.Action.Properties.ItemStackProperty;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Events.CancellableEvent;
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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class HasItemCondition extends Condition {
    public HasItemCondition() {
        super(ConditionEnum.HAS_ITEM,
                "Has Item",
                "Requires the user to have the specified item.",
                Material.CHEST,
                List.of()); // it doesn't have a ptsl prefix?

        getProperties().addAll(List.of(
                new ItemStackProperty(
                        "item",
                        "Item",
                        "The item to check for."
                ),
                new EnumProperty<>(
                        "whatToCheck",
                        "What to check",
                        "What should be checked on the item.",
                        WhatToCheck.class
                ).setValue(WhatToCheck.METADATA),
                new EnumProperty<>(
                        "whereToCheck",
                        "Where to check",
                        "Where to check for the item.",
                        WhereToCheck.class
                ).setValue(WhereToCheck.ANYWHERE),
                new EnumProperty<>(
                        "amountCondition",
                        "Amount Condition",
                        "The comparator to use for the item amount.",
                        Amount.class
                ).setValue(Amount.ANY_AMOUNT),
                new IntegerProperty(
                        "amount",
                        "Amount",
                        "The amount of the item."
                ).setValue(1)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        int amount = getValue("amount", IntegerProperty.class).getValue();
        if (getValue("item", ItemStackProperty.class).getValue() == null) return OutputType.ERROR;
        switch (getValue("whereToCheck", WhereToCheck.class)) {
            /* case CUSTOM: {
                if (customSlot == -1) return OutputType.ERROR;
                if (checkItem(player.getInventory().getItem(customSlot))) {
                    return checkAmount(player.getInventory().getItem(customSlot), amount);
                }
                break;
            } */
            case HAND: {
                if (checkItem(player.getInventory().getItemInMainHand())) {
                    return checkAmount(player.getInventory().getItemInMainHand(), amount) ? OutputType.SUCCESS : OutputType.ERROR;
                }
                break;
            }
            case OFFHAND: {
                if (checkItem(player.getInventory().getItemInOffHand())) {
                    return checkAmount(player.getInventory().getItemInOffHand(), amount) ? OutputType.SUCCESS : OutputType.ERROR;
                }
                break;
            }
            case ARMOR: {
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (checkItem(armor)) {
                        return checkAmount(armor, amount) ? OutputType.SUCCESS : OutputType.ERROR;
                    }
                }
                break;
            }
            case HOTBAR: {
                for (int i = 0; i < 9; i++) {
                    if (checkItem(player.getInventory().getItem(i))) {
                        return checkAmount(player.getInventory().getItem(i), amount) ? OutputType.SUCCESS : OutputType.ERROR;
                    }
                }
                break;
            }
            case INVENTORY: {
                for (ItemStack inventory : player.getInventory().getContents()) {
                    if (checkItem(inventory)) {
                        return checkAmount(inventory, amount) ? OutputType.SUCCESS : OutputType.ERROR;
                    }
                }
                break;
            }
            case ANYWHERE: {
                for (ItemStack inventory : player.getInventory().getContents()) {
                    if (checkItem(inventory)) {
                        return checkAmount(inventory, amount) ? OutputType.SUCCESS : OutputType.ERROR;
                    }
                }
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (checkItem(armor)) {
                        return checkAmount(armor, amount) ? OutputType.SUCCESS : OutputType.ERROR;
                    }
                }
                for (int i = 0; i < 9; i++) {
                    if (checkItem(player.getInventory().getItem(i))) {
                        return checkAmount(player.getInventory().getItem(i), amount) ? OutputType.SUCCESS : OutputType.ERROR;
                    }
                }
                if (checkItem(player.getInventory().getItemInMainHand())) {
                    return checkAmount(player.getInventory().getItemInMainHand(), amount) ? OutputType.SUCCESS : OutputType.ERROR;
                }
                if (checkItem(player.getInventory().getItemInOffHand())) {
                    return checkAmount(player.getInventory().getItemInOffHand(), amount) ? OutputType.SUCCESS : OutputType.ERROR;
                }
                break;
            }
        }
        return OutputType.ERROR;
    }

    private boolean checkItem(ItemStack item) {
        if (item == null) return false;
        ItemStack thisItem = getValue("item", ItemStackProperty.class).getValue();
        WhatToCheck whatToCheck = getValue("whatToCheck", WhatToCheck.class);
        if (whatToCheck == WhatToCheck.ITEM_TYPE) {
            return item.getType() == thisItem.getType();
        } else if (whatToCheck == WhatToCheck.METADATA) {
            return item.isSimilar(thisItem);
        }
        return false;
    }

    private boolean checkAmount(ItemStack item, int amount) {
        switch (getValue("amountCondition", Amount.class)) {
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
        /* CUSTOM(Material.BOOK) */
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

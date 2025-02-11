package com.al3x.housing2.Utils;

import org.bukkit.inventory.ItemStack;

public class StackUtils {
    public static String getDisplayName(ItemStack stack) {
        if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
            return stack.getItemMeta().getDisplayName();
        }
        return stack.getI18NDisplayName();
    }
}

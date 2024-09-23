package com.al3x.housing2.Actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Action {
    String toString();
    ItemStack getDisplayItem();
    void execute(Player player);
}

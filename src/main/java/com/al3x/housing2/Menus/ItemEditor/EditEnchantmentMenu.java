package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.*;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.ItemBuilder.ActionType.ADD_YELLOW;

public class EditEnchantmentMenu extends Menu {
    int page = 1;
    HousingWorld house;

    public EditEnchantmentMenu(Player player) {
        super(player, "&7Edit Enchantments", 9 * 6);
        this.house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getEnchantments().isEmpty()) {
            addItem(22, ItemBuilder.create(Material.BEDROCK)
                    .name("&cNo Enchants!")
                    .build());
        } else {
            PaginationList<Enchantment> paginationList = new PaginationList<>(item.getEnchantments().keySet(), slots.length);
            List<Enchantment> enchantments = paginationList.getPage(page);
            for (int i = 0; i < enchantments.size(); i++) {
                Enchantment enchantment = enchantments.get(i);
                int level = item.getEnchantments().get(enchantment);
                ItemBuilder itemBuilder = ItemBuilder.create(Material.ENCHANTED_BOOK);
                String levelRoman = NumberUtilsKt.romanThatBitch(level);
                itemBuilder.name("&a" + StringUtilsKt.formatCapitalize(enchantment.getKey().getKey()) + " " + levelRoman);
                itemBuilder.lClick(ItemBuilder.ActionType.REMOVE_YELLOW);
                addItem(slots[i], itemBuilder.build(), () -> {
                    item.removeEnchantment(enchantment);
                    player.getInventory().setItemInMainHand(item);
                    setupItems();
                });
            }

            if (page > 1) {
                addItem(45, ItemBuilder.create(Material.ARROW)
                        .name("&7Previous Page")
                        .build(), () -> {
                    page--;
                    setupItems();
                    open();
                });
            }

            if (page < paginationList.getPageCount()) {
                addItem(53, ItemBuilder.create(Material.ARROW)
                        .name("&7Next Page")
                        .build(), () -> {
                    page++;
                    setupItems();
                    open();
                });
            }
        }

        addItem(50, ItemBuilder.create(Material.ENCHANTING_TABLE)
                .name("&aAdd Enchantment")
                .lClick(ADD_YELLOW)
                .build(), () -> {
            List<Duple<Enchantment, ItemBuilder>> enchantments = new ArrayList<>();
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.canEnchantItem(item)) {
                    ItemBuilder itemBuilder = ItemBuilder.create(Material.ENCHANTED_BOOK);
                    itemBuilder.name("&a" + StringUtilsKt.formatCapitalize(enchantment.getKey().getKey()));
                    itemBuilder.lClick(ItemBuilder.ActionType.SELECT_LEVEL);
                    enchantments.add(new Duple<>(enchantment, itemBuilder));
                }
            }
            //Personally I hate that I coded this lol, but I made all of these fancy things, so I might aswell use them :)
            new PaginationMenu<Enchantment>(
                    Main.getInstance(), "&7Add Enchantment", enchantments, player, house, this, (enchantment) -> {
                List<Duple<Integer, ItemBuilder>> levels = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    ItemBuilder itemBuilder = ItemBuilder.create(Material.ENCHANTED_BOOK);
                    itemBuilder.name("&a" + StringUtilsKt.formatCapitalize(enchantment.getKey().getKey()) + " " + NumberUtilsKt.romanThatBitch(i));
                    itemBuilder.lClick(ADD_YELLOW);
                    levels.add(new Duple<>(i, itemBuilder));
                }
                new PaginationMenu<Integer>(
                        Main.getInstance(), "&7Select Level", levels, player, house, this, (level) -> {
                    item.addUnsafeEnchantment(enchantment, level);
                    player.getInventory().setItemInMainHand(item);
                    setupItems();
                    new EditEnchantmentMenu(player).open();
                }).open();
            }).open();
        });

        addItem(49, ItemBuilder.create(Material.ARROW)
                .name("&aGo Back")
                .build(), () -> {
            new EditItemMainMenu(player).open();
        });
    }
}

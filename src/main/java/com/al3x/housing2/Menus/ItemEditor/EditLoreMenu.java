package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.ADD_YELLOW;

public class EditLoreMenu extends Menu {
    int page = 1;
    HousingWorld house;

    public EditLoreMenu(Player player) {
        super(player, "&7Edit Lore", 9 * 6);
        this.house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta().getLore() == null || item.getItemMeta().getLore().isEmpty()) {
            addItem(22, ItemBuilder.create(Material.BEDROCK)
                    .name("&cNo Lore!")
                    .build());
        } else {
            PaginationList<String> paginationList = new PaginationList<>(item.getItemMeta().getLore(), slots.length);
            List<String> lore = paginationList.getPage(page);
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                ItemBuilder itemBuilder = ItemBuilder.create(Material.PAPER);
                itemBuilder.name("&aLine #" + (i + 1));
                itemBuilder.info("&eCurrent Line", "");
                itemBuilder.info(null, line);
                itemBuilder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
                itemBuilder.rClick(ItemBuilder.ActionType.DELETE_YELLOW);
                itemBuilder.shiftClick();
                addItem(slots[i], itemBuilder.build(), e -> {
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> newLore = new ArrayList<>(meta.getLore());
                    if (e.isShiftClick()) {
                        shiftLine(newLore, line, e.isRightClick());
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                        setupItems();
                        return;
                    }

                    if (e.isRightClick()) {
                        newLore.remove(line);
                        meta.setLore(newLore);
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                        setupItems();
                    } else {
                        player.sendMessage(colorize("&eEnter the new line for the lore."));
                        openChat(Main.getInstance(), line, (newLine) -> {
                            newLore.set(newLore.indexOf(line), newLine);
                            meta.setLore(newLore);
                            item.setItemMeta(meta);
                            player.getInventory().setItemInMainHand(item);
                            setupItems();
                        });
                    }
                });
            }


            if (page > 1) {
                addItem(45, ItemBuilder.create(Material.ARROW)
                        .name("&7Previous Page")
                        .build(), e -> {
                    page--;
                    setupItems();
                });
            }

            if (page < paginationList.getPageCount()) {
                addItem(53, ItemBuilder.create(Material.ARROW)
                        .name("&7Next Page")
                        .build(), e -> {
                    page++;
                    setupItems();
                });
            }
        }

        addItem(50, ItemBuilder.create(Material.PAPER)
                .name("&aAdd Line")
                .build(), e -> {
            player.sendMessage(colorize("&eEnter the new line for the lore."));
            openChat(Main.getInstance(), "", (newLine) -> {
                ItemMeta meta = item.getItemMeta();
                List<String> newLore = new ArrayList<>(meta.getLore() == null ? new ArrayList<>() : meta.getLore());
                newLore.add(colorize(newLine));
                meta.setLore(newLore);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                setupItems();
            });
        });

        addItem(49, ItemBuilder.create(Material.ARROW)
                .name("&aGo Back")
                .build(), e -> {
            new EditItemMainMenu(player).open();
        });
    }

    public void shiftLine(ArrayList<String> lore, String action, boolean forward) {
        int index = lore.indexOf(action);

        if (lore.size() < 2) return;

        lore.remove(index);

        if (forward) {
            lore.add((index == lore.size()) ? 0 : index + 1, action);
        } else {
            lore.add((index == 0) ? lore.size() : index - 1, action);
        }
        setupItems();
    }
}

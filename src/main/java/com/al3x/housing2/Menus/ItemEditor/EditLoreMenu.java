package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

        if (item.getItemMeta().lore() == null || item.getItemMeta().lore().isEmpty()) {
            addItem(22, ItemBuilder.create(Material.BEDROCK)
                    .name("&cNo Lore!")
                    .build());
        } else {
            PaginationList<Component> paginationList = new PaginationList<>(item.lore(), slots.length);
            List<Component> lore = paginationList.getPage(page);
            for (int i = 0; i < lore.size(); i++) {
                Component component = lore.get(i);
                ItemBuilder itemBuilder = ItemBuilder.create(Material.PAPER);
                itemBuilder.name("&aLine #" + (i + 1));
                itemBuilder.info("&eCurrent Line", "");
                itemBuilder.info(null, LegacyComponentSerializer.legacyAmpersand().serialize(component));
                itemBuilder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
                itemBuilder.rClick(ItemBuilder.ActionType.DELETE_YELLOW);
                itemBuilder.shiftClick();
                int finalI = i;
                addItem(slots[i], itemBuilder.build(), e -> {
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<Component> newLore = new ArrayList<>(meta.lore() == null ? new ArrayList<>() : meta.lore());
                    if (e.isShiftClick()) {
                        shiftLine(newLore, finalI, component, e.isRightClick());
                        meta.lore(newLore);
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                        setupItems();
                        return;
                    }

                    if (e.isRightClick()) {
                        newLore.remove(component);
                        meta.lore(newLore);
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                        setupItems();
                    } else {
                        player.sendMessage(colorize("&eEnter the new line for the lore."));
                        openChat(Main.getInstance(), LegacyComponentSerializer.legacyAmpersand().serialize(component), (newLine) -> {
                            newLore.set(newLore.indexOf(component), StringUtilsKt.housingStringFormatter(newLine));
                            meta.lore(newLore);
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
                List<Component> newLore = new ArrayList<>(meta.lore() == null ? new ArrayList<>() : meta.lore());
                newLore.add(StringUtilsKt.housingStringFormatter(newLine));
                meta.lore(newLore);
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

    public void shiftLine(ArrayList<Component> lore, int index, Component action, boolean forward) {
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

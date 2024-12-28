package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.*;

public class EditItemMainMenu extends Menu {
    public EditItemMainMenu(Player player) {
        super(player, "&7Edit Item", 9 * 6);
    }

    @Override
    public void setupItems() {
        clearItems();

        Item customItem = Item.fromItemStack(player.getInventory().getItemInMainHand());
        ItemStack item = customItem.getBase();

        addItem(13, item);

        //Rename Item
        addItem(28, ItemBuilder.create(Material.NAME_TAG)
                .name("&aRename Item")
                .lClick(EDIT_YELLOW)
                .build(), e -> {
            player.sendMessage(colorize("&eEnter the new name for the item."));
            openChat(Main.getInstance(), item.getItemMeta().getDisplayName(), (newName) -> {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(colorize(newName));
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                setupItems();
            });
        });

        //Edit Lore
        addItem(30, ItemBuilder.create(Material.BOOK)
                .name("&aEdit Lore")
                .lClick(EDIT_YELLOW)
                .build(), e -> {
            new EditLoreMenu(player).open();
        });

        //Edit Enchantments
        addItem(32, ItemBuilder.create(Material.ENCHANTED_BOOK)
                .name("&aEdit Enchantments")
                .lClick(EDIT_YELLOW)
                .build(), e -> {
            new EditEnchantmentMenu(player).open();
        });

        addItem(34, ItemBuilder.create(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .name("&aEdit Actions")
                .description("Edit the actions which will be performed when the player uses this item.")
                .lClick(EDIT_YELLOW)
                .build(), e -> {
//            if (e.isLeftClick()) {
//                List<Action> actions = new ArrayList<>(customItem.getActions().getOrDefault(ClickType.LEFT, new ArrayList<>()));
//                HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
//                ActionsMenu menu = new ActionsMenu(Main.getInstance(), player, house, actions, this, null);
//                menu.setUpdate(() -> {
//                    customItem.getActions().put(ClickType.LEFT, actions);
//                    player.getInventory().setItemInMainHand(customItem.build());
//                    setupItems();
//                });
//                menu.open();
//            } else if (e.isRightClick()) {
//                List<Action> actions = new ArrayList<>(customItem.getActions().getOrDefault(ClickType.RIGHT, new ArrayList<>()));
//                HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
//                ActionsMenu menu = new ActionsMenu(Main.getInstance(), player, house, actions, this, null);
//                menu.setUpdate(() -> {
//                    customItem.getActions().put(ClickType.RIGHT, actions);
//                    player.getInventory().setItemInMainHand(customItem.build());
//                    setupItems();
//                });
//                menu.open();
//            }

            new EditActionTypesMenu(player, customItem).open();
        });

        addItem(40, ItemBuilder.create(Material.BLACK_BANNER)
                .name("&aEdit Item Flags")
                .description("Toggle which flags are shown on the tooltip of the item.")
                .lClick(EDIT_YELLOW)
                .build(), e -> {
            new EditFlagMenu(player).open();
        });


    }
}

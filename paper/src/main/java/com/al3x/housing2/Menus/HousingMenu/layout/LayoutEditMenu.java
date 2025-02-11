package com.al3x.housing2.Menus.HousingMenu.layout;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.ConfirmMenu;
import com.al3x.housing2.Menus.ItemSelectMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class LayoutEditMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Layout layout;

    public LayoutEditMenu(Main main, Player player, HousingWorld house, Layout layout) {
        super(player, colorize("&7Edit Layout: " + layout.getName()), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.layout = layout;
    }

    @Override
    protected boolean isCancelled() {
        return false;
    }

    //I dont know where to put Rename and Description so uhh yeah :) -Sin_ender

    @Override
    public void setupItems() {
        clearItems();

        List<ItemStack> inventory = layout.getInventory();
        for (int i = 0; i < 27 && i < inventory.size(); i++) {
            if (inventory.get(i) != null) {
                addItem(i, inventory.get(i));
            }
        }

        ItemBuilder item = ItemBuilder.create(Material.GRAY_STAINED_GLASS_PANE)
                .name("&8↑ &7Inventory")
                .punctuation(false)
                .description("&8↓ &7Hotbar");
        for (int i = 27; i < 36; i++) {
            addItem(i, item.build());
        }

        List<ItemStack> hotbar = layout.getHotbar();
        for (int i = 36; i < 45 && i - 36 < hotbar.size(); i++) {
            if (hotbar.get(i - 36) != null) {
                addItem(i, hotbar.get(i - 36));
            }

            if (i == 44) {
                ItemStack menu = new ItemStack(Material.DARK_OAK_DOOR);
                ItemMeta menuMeta = menu.getItemMeta();
                menuMeta.setDisplayName(colorize("&aHousing Menu &7(Right-Click"));
                menu.setItemMeta(menuMeta);
                addItem(i, menu, (e) -> {
                    e.setCancelled(true);
                });
            }
        }

        //Change Offhand
        ItemStack offhand = layout.getOffhand();
        addItem(46, offhand == null ? ItemBuilder.create(Material.SHIELD)
                .name(colorize("&aChange Offhand"))
                .description("Click to change the offhand item, currently empty.")
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build() : offhand, (e) -> {
            e.setCancelled(true);
            new ItemSelectMenu(player, this, (choosen) -> {
                layout.setOffhand(choosen);
                setupItems();
            }).open();
        });

        //Change Armor
        addItem(47, ItemBuilder.create(Material.CHAINMAIL_CHESTPLATE)
                .name(colorize("&aChange Armor"))
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), (e) -> {
            e.setCancelled(true);
            new ChangeArmorMenu(main, player, house, layout).open();
        });

        //Delete Region
        addItem(48, ItemBuilder.create(Material.TNT)
                .name(colorize("&aDelete Layout"))
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), (e) -> {
            e.setCancelled(true);
            new ConfirmMenu(player, new LayoutsMenu(main, player, house), () -> {
                house.getLayouts().remove(layout);
            }).open();
        });

        //Back
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Layouts Menu")
                .build(), (e) -> {
            e.setCancelled(true);
            new LayoutsMenu(main, player, house).open();
        });

        //Save
        addItem(50, ItemBuilder.create(Material.LIME_DYE)
                .name(colorize("&aSave Layout"))
                .build(), (e) -> {
            e.setCancelled(true);
            layout.save(
                    Arrays.asList(this.inventory.getContents()).subList(0, 27),
                    Arrays.asList(this.inventory.getContents()).subList(36, 45),
                    layout.getArmor(),
                    layout.getOffhand()
            );
            player.sendMessage(colorize("&aLayout saved!"));
        });

        //Apply Layout
        addItem(52, ItemBuilder.create(Material.ENDER_CHEST)
                .name(colorize("&aApply Layout"))
                .description("Apply this layout to yourself.")
                .build(), (e) -> {
            e.setCancelled(true);
            layout.apply(player);
        });

        //Import Layout
        addItem(53, ItemBuilder.create(Material.BREWING_STAND)
                .name(colorize("&aImport Layout"))
                .description("Import a layout from a player inventory into the layout.")
                .build(), (e) -> {
            e.setCancelled(true);
            layout.save(
                    Arrays.asList(player.getInventory().getContents()).subList(9, 35),
                    Arrays.asList(player.getInventory().getContents()).subList(0, 9),
                    Arrays.asList(player.getInventory().getArmorContents()),
                    player.getInventory().getItemInOffHand().getType() == Material.AIR ? null : player.getInventory().getItemInOffHand()
            );
            setupItems();
        });
    }
}

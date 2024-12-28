package com.al3x.housing2.Menus.HousingMenu.CustomMenus;

import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ConfirmMenu;
import com.al3x.housing2.Menus.CustomMenuViewer;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CustomMenuEditMenu extends Menu {
    CustomMenu customMenu;
    public CustomMenuEditMenu(Player player, CustomMenu customMenu) {
        super(player, "Edit Menu: " + customMenu.getTitle(), 9 * 4);
        this.customMenu = customMenu;
    }

    @Override
    public void setupItems() {
        Main main = Main.getInstance();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        addItem(11, ItemBuilder.create(Material.ANVIL)
                .name("&aChange Title")
                .description("Change the title of this menu.")
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), (e) -> {
            player.sendMessage("§eEnter the new title for this menu: ");
            openChat(main, customMenu.getTitle(), (message) -> {
                customMenu.setTitle(message);
            });
        });

        addItem(13, ItemBuilder.create(Material.BEACON)
                .name("&aChange Menu Size")
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .build(), (e) -> {
            new ChangeMenuSizeMenu(player, customMenu).open();
        });

        addItem(15, ItemBuilder.create(Material.ENDER_CHEST)
                .name("&aEdit Menu Elements")
                .description("Edit the specific item elements in this menu.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            new EditMenuElementsMenu(player, customMenu).open();
        });

        addItem(30, ItemBuilder.create(Material.TNT)
                .name("&aDelete Menu")
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), (e) -> {
            new ConfirmMenu(player, new CustomMenusMenu(main, player, house), () -> {
                house.getCustomMenus().remove(customMenu);
            }).open();
        });
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name("&aGo Back")
                .build(), (e) -> {
            new CustomMenusMenu(main, player, house).open();
        });
        addItem(32, ItemBuilder.create(Material.CHEST)
                .name("&aView Menu")
                .lClick(ItemBuilder.ActionType.VIEW_YELLOW)
                .build(), (e) -> {
            new CustomMenuViewer(player, customMenu).open();
        });
    }
}

package com.al3x.housing2.Menus.HousingMenu.CustomMenus;

import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.SystemsMenu;
import com.al3x.housing2.Menus.HousingMenu.regions.RegionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_REGION;

public class CustomMenusMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private int currentPage = 1;

    public CustomMenusMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Custom Menus"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<CustomMenu> paginationList = new PaginationList<>(house.getCustomMenus(), slots.length);
        List<CustomMenu> customMenus = paginationList.getPage(currentPage);
        if (customMenus != null) {
            for (int i = 0; i < customMenus.size(); i++) {
                CustomMenu customMenu = customMenus.get(i);
                ItemBuilder item = ItemBuilder.create(Material.CHEST);
                item.name(colorize("&a" + customMenu.getTitle()));
                addItem(slots[i], item.build(), () -> {
                    new CustomMenuEditMenu(player, customMenu).open();
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build());
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(Material.ARROW).name(colorize("&7Previous Page")).build(), () -> {
                currentPage--;
                setupItems();
                open();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, new ItemBuilder().material(Material.ARROW).name(colorize("&7Next Page")).build(), () -> {
                currentPage++;
                setupItems();
                open();
            });
        }

        addItem(49, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), () -> {
            new SystemsMenu(main, player, house).open();
        });

        addItem(50, new ItemBuilder().material(Material.PAPER).name(colorize("&aCreate Menu")).build(), () -> {
            player.sendMessage(colorize("&ePlease enter the title of the new menu."));
            openChat(main, (message) -> {
                CustomMenu customMenu = new CustomMenu(message, 6);
                house.addCustomMenu(customMenu);
                setupItems();
            });
        });
    }
}

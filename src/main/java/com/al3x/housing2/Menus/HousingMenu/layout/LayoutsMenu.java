package com.al3x.housing2.Menus.HousingMenu.layout;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.SystemsMenu;
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
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_YELLOW;

public class LayoutsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private int currentPage = 1;

    public LayoutsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Inventory Layouts"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Layout> paginationList = new PaginationList<>(house.getLayouts(), slots.length);
        List<Layout> layouts = paginationList.getPage(currentPage);
        if (layouts != null) {
            for (int i = 0; i < layouts.size(); i++) {
                Layout layout = layouts.get(i);
                ItemBuilder item = ItemBuilder.create(layout.getIcon());
                item.name(colorize("&a" + layout.getName()));
                item.description(layout.getDescription());
                item.lClick(EDIT_YELLOW);
                addItem(slots[i], item.build(), () -> {
                    new LayoutEditMenu(main, player, house, layout).open();
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build());
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(Material.ARROW).name(colorize("&7Previous Page")).build(), (e) -> {
                currentPage--;
                setupItems();
                open();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, new ItemBuilder().material(Material.ARROW).name(colorize("&7Next Page")).build(), (e) -> {
                currentPage++;
                setupItems();
                open();
            });
        }

        addItem(49, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), (e) -> {
            new SystemsMenu(main, player, house).open();
        });

        addItem(50, new ItemBuilder().material(Material.PAPER).name(colorize("&aCreate Layout")).build(), (e) -> {
            player.sendMessage("§eEnter the name for the new layout: ");
            openChat(main, "", (message) -> {
                if (house.createLayout(message) != null) {
                    player.sendMessage("§aLayout created!");
                    setupItems();
                    Bukkit.getScheduler().runTask(main, this::open);
                } else {
                    player.sendMessage("§cLayout already exists!");
                }
            });
        });
    }
}

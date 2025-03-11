package com.al3x.housing2.Menus.HousingMenu.regions;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Region;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.HousingMenu.SystemsMenu;
import com.al3x.housing2.Menus.HousingMenu.commands.CommandEditMenu;
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
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_ACTIONS;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_REGION;

public class RegionsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private int currentPage = 1;

    public RegionsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Regions"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Region> paginationList = new PaginationList<>(house.getRegions(), slots.length);
        List<Region> regions = paginationList.getPage(currentPage);
        if (regions != null) {
            for (int i = 0; i < regions.size(); i++) {
                Region region = regions.get(i);
                ItemBuilder item = ItemBuilder.create(Material.GRASS_BLOCK);
                item.name(colorize("&a" + region.getName()));
                item.info("&7From", "&7" + region.getFirst().getX() + ", " + region.getFirst().getY() + ", " + region.getFirst().getZ());
                item.info("&7To", "&7" + region.getSecond().getX() + ", " + region.getSecond().getY() + ", " + region.getSecond().getZ());
                item.lClick(EDIT_REGION);
                addItem(slots[i], item.build(), () -> {
                    new RegionEditMenu(main, player, house, region).open();
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

        addItem(50, new ItemBuilder().material(Material.WOODEN_AXE).name(colorize("&aCreate Region")).build(), () -> {
            if (Main.getInstance().getProtoolsManager().getSelection(player) == null) {
                player.sendMessage(colorize("&cYou must make a selection first!"));
                return;
            }
            Duple<Location, Location> selection = Main.getInstance().getProtoolsManager().getSelection(player);
            player.sendMessage(colorize("&eEnter the name of the region:"));
            openChat(main, (s) -> {
                Bukkit.getScheduler().runTask(main, () -> {
                    // Create region
                    if (house.createRegion(s, selection.getFirst(), selection.getSecond()) != null) {
                        player.sendMessage(colorize("&aRegion created!"));
                        setupItems();
                        open();
                    } else {
                        player.sendMessage(colorize("&cRegion already exists!"));
                    }
                });
            });
        });
    }
}

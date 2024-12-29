package com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions;

import com.al3x.housing2.Instances.Group;
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
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_YELLOW;

public class GroupsMenu extends Menu {
    Main main;
    HousingWorld house;

    private int currentPage = 1;

    public GroupsMenu(Player player, Main main, HousingWorld house) {
        super(player, "&7Groups Menu", 9 * 6);
        this.main = main;
        this.house = house;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Group> paginationList = new PaginationList<>(house.getGroups(), slots.length);
        List<Group> groups = paginationList.getPage(currentPage);
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                Group group = groups.get(i);
                ItemBuilder item = ItemBuilder.create(Material.PLAYER_HEAD);
                item.skullTexture("86f125004a8ffa6e4a4ec7b178606d0670c28a75b9cde59e011e66e91a66cf14");
                item.name(colorize(group.getDisplayName()));
                item.description("Edit the group " + group.getDisplayName() + " &7permissions, name, tag and more.");
                item.lClick(EDIT_YELLOW);
                addItem(slots[i], item.build(), () -> {
                    new GroupEditMenu(player, main, house, group).open();
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

        addItem(50, new ItemBuilder().material(Material.OAK_SIGN).name(colorize("&aCreate Group")).build(), (e) -> {
            player.sendMessage(colorize("&eEnter the name of the group:"));
            openChat(main, (s) -> {
                // Create region
                if (house.createGroup(s) != null) {
                    player.sendMessage(colorize("&aGroup created with name: " + s));
                    setupItems();
                } else {
                    player.sendMessage(colorize("&cThat name is already in use by another group!"));
                }
            });
        });
    }
}

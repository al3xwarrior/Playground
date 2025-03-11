package com.al3x.housing2.Menus.HousingMenu.teams;

import com.al3x.housing2.Enums.Colors;
import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Team;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.HousingMenu.SystemsMenu;
import com.al3x.housing2.Menus.HousingMenu.groupsAndPermissions.GroupEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.EDIT_YELLOW;

public class TeamsMenu extends Menu {
    Main main;
    HousingWorld house;

    private int currentPage = 1;

    public TeamsMenu(Player player, Main main, HousingWorld house) {
        super(player, "&7Teams Menu", 9 * 6);
        this.main = main;
        this.house = house;
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Team> paginationList = new PaginationList<>(house.getTeams(), slots.length);
        List<Team> teams = paginationList.getPage(currentPage);
        if (teams != null) {
            for (int i = 0; i < teams.size(); i++) {
                Team team = teams.get(i);
                ItemBuilder item = ItemBuilder.create(Colors.fromColorcode(team.getColor()).getMaterial());
                item.name(colorize(team.getName()));
                item.description("Edit the team " + team.getName() + " &7permissions, name, tag and more.");
                item.lClick(EDIT_YELLOW);
                addItem(slots[i], item.build(), () -> {
                    new TeamEditMenu(player, main, house, team).open();
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

        addItem(50, new ItemBuilder().material(Material.PAPER).name(colorize("&aCreate Team")).build(), () -> {
            player.sendMessage(colorize("&eEnter the name of the team:"));
            openChat(main, (s) -> {
                if (house.createTeam(s) != null) {
                    player.sendMessage(colorize("&aTeam created with name: " + s));
                    setupItems();
                } else {
                    player.sendMessage(colorize("&cThat name is already in use by another team!"));
                }
            });
        });
    }
}

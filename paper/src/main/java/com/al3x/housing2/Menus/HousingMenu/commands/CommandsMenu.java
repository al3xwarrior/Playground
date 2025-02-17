package com.al3x.housing2.Menus.HousingMenu.commands;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.HousingMenu.SystemsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class CommandsMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private int currentPage = 1;

    public CommandsMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Commands"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Command> paginationList = new PaginationList<>(house.getCommands(), slots.length);
        List<Command> commands = paginationList.getPage(currentPage);
        if (commands != null) {
            for (int i = 0; i < commands.size(); i++) {
                Command command = commands.get(i);
                ItemBuilder item = ItemBuilder.create(Material.COMMAND_BLOCK_MINECART);
                item.name(colorize("&a" + command.getName()));
                item.info("Priority", "&a" + command.getPriorityRequired());
                item.info("Actions", "&a" + command.getActions().size());
                item.info("Arguments", "&a" + command.getArgs().size());
                item.lClick(ItemBuilder.ActionType.EDIT_ACTIONS);
                item.rClick(ItemBuilder.ActionType.EDIT_COMMAND);
                addItem(slots[i], item.build(), () -> {
                    new ActionsMenu(main, player, house, command.getActions(), this, "CommandsActions").open();
                },  () -> {
                    new CommandEditMenu(main, player, house, command).open();
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build(), () -> {
            });
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

        addItem(50, new ItemBuilder().material(Material.PAPER).name(colorize("&aCreate Command")).build(), () -> {
            player.sendMessage(colorize("&eEnter the name of the command:"));
            openChat(main, (s) -> {
                Bukkit.getScheduler().runTask(main, () -> {
                    Command command = house.createCommand(s);
                    if (command == null) {
                        player.sendMessage(colorize("&cA command with that name already exists or is invalid!"));
                        return;
                    }
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        new ActionsMenu(main, player, house, command.getActions(), this, "CommandsActions").open();
                    }, 1L);
                });
            });
        });
    }
}

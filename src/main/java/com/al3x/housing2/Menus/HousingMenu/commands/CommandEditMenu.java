package com.al3x.housing2.Menus.HousingMenu.commands;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.FunctionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class CommandEditMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Command command;

    public CommandEditMenu(Main main, Player player, HousingWorld house, Command command) {
        super(player, colorize("&7Edit: " + command.getName()), 9 * 4);
        this.main = main;
        this.player = player;
        this.house = house;
        this.command = command;
    }

    @Override
    public void setupItems() {
        //Rename Command
        addItem(11, ItemBuilder.create(Material.ANVIL)
                .name(colorize("&aRename Command"))
                .description("Change the name of this command.")
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), (e) -> {
            player.sendMessage("§eEnter the new name for this command: ");
            openChat(main, command.getName(), (message) -> {
                command.setName(message);
            });
        });

        //Edit Arguments
        addItem(13, ItemBuilder.create(Material.WRITABLE_BOOK)
                .name(colorize("&aEdit Arguments"))
                .description("Edit the arguments of this command.\n\n&fCurrent: &a" + command.getArgs().size() + " arguments")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            new CommandArgumentsEditMenu(main, player, house, command).open();
        });

        //Edit Group Priority
        addItem(15, ItemBuilder.create(Material.FILLED_MAP)
                .name(colorize("&aEdit Group Priority"))
                .description("Edit the group priority of this command.\n\n&fCurrent: &a" + command.getPriorityRequired() + "\n\n&cNOT IMPLEMENTED YET.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), (e) -> {
            player.sendMessage("§eEnter the new group priority for this command: ");
            openChat(main, command.getPriorityRequired() + "", (message) -> {
                try {
                    int priority = Integer.parseInt(message);
                    command.setPriorityRequired(priority);
                } catch (NumberFormatException ex) {
                    player.sendMessage(colorize("&cInvalid number!"));
                }
            });
        });


        //Delete Command
        addItem(30, ItemBuilder.create(Material.TNT)
                .name(colorize("&aDelete Command"))
                .lClick(ItemBuilder.ActionType.DELETE_YELLOW)
                .build(), (e) -> {
            command.setLoaded(false, house);
            house.getCommands().remove(command);
            new CommandsMenu(main, player, house).open();
        });

        //Back
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Commands")
                .build(), (e) -> {
            new CommandsMenu(main, player, house).open();
        });
    }
}

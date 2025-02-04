package com.al3x.housing2.Menus.HousingMenu.commands;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Menus.HousingMenu.FunctionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class CommandArugmentEditMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Command command;
    private Command.CommandArg arg;

    public CommandArugmentEditMenu(Main main, Player player, HousingWorld house, Command command, Command.CommandArg arg) {
        super(player, colorize("&7Edit: " + command.getName() + " -> Edit Argument: " + arg.getName()), 9 * 5);
        this.main = main;
        this.player = player;
        this.house = house;
        this.command = command;
        this.arg = arg;
    }

    @Override
    public void setupItems() {
        //Rename Argument
        addItem(11, ItemBuilder.create(Material.ANVIL)
                .name(colorize("&aRename Argument"))
                .description("Change the name of this argument.")
                .lClick(ItemBuilder.ActionType.RENAME_YELLOW)
                .build(), () -> {
            player.sendMessage("§eEnter the new name for this argument: ");
            openChat(main, arg.getName(), (message) -> {
                arg.setName(message);
            });
        });

        //Edit Type
        addItem(13, ItemBuilder.create(Material.WRITABLE_BOOK)
                .name(colorize("&aEdit Type"))
                .description("Change the type of this argument.")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            EnumMenu<Command.ArgType> enumMenu = new EnumMenu<>(main, "Select Type", Command.ArgType.values(), arg.getType().getMaterial(), player, house, this, (type) -> {
                arg.setType(type);
                open();
            });
            enumMenu.open();
        });

        //Edit Required
        addItem(15, ItemBuilder.create((arg.isRequired() ? Material.LIME_DYE : Material.RED_DYE))
                .name(colorize("&aToggle Required"))
                .description("Change if this argument is required.")
                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                .build(), () -> {
            arg.setRequired(!arg.isRequired());
            open();
        });

        if (arg.getType() == Command.ArgType.STRING) {
            //Edit Greedy
            addItem(22, ItemBuilder.create((arg.isGreedy() ? Material.LIME_DYE : Material.RED_DYE))
                    .name(colorize("&aToggle Greedy"))
                    .description("Change if this argument is greedy.")
                    .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW)
                    .build(), () -> {
                arg.setGreedy(!arg.isGreedy());
                open();
            });
        }

        //Back
        addItem(40, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Arugments")
                .build(), () -> {
            new CommandArgumentsEditMenu(main, player, house, command).open();
        });
    }
}

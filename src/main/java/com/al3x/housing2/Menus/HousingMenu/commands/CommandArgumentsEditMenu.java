package com.al3x.housing2.Menus.HousingMenu.commands;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.EnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class CommandArgumentsEditMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private Command command;

    private int currentPage = 1;

    public CommandArgumentsEditMenu(Main main, Player player, HousingWorld house, Command command) {
        super(player, colorize("&7Edit: " + command.getName() + " -> Arguments"), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.command = command;
    }

    @Override
    public void setupItems() {
        clearItems();

        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Command.CommandArg> paginationList = new PaginationList<>(command.getArgs(), 21);
        List<Command.CommandArg> args = paginationList.getPage(currentPage);

        if (args != null) {
            for (int i = 0, j = 0; i < args.size(); i++, j++) {
                Command.CommandArg arg = args.get(i);
                ItemBuilder item = ItemBuilder.create(arg.getType().getMaterial());
                item.name(colorize("&a" + arg.getName()));
                item.description("Type: " + arg.getType().name() + "\n" + "Required: " + arg.isRequired());
                item.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
                item.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
                item.shiftClick();
                addItem(slots[j], item.build(), (e) -> {
                    if (e.isShiftClick()) {
                        shiftArg(arg, e.isLeftClick());
                        return;
                    }

                    if (e.isLeftClick()) {
                        new CommandArugmentEditMenu(main, player, house, command, arg).open();
                    } else {
                        command.getArgs().remove(arg);
                        setupItems();
                    }
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build(), (e) -> {});
        }

        //Add Argument Super simple because I am lazy :)
        addItem(50, ItemBuilder.create(Material.BOOK)
                .name(colorize("&aAdd Argument"))
                .description("Add a new argument to this command.")
                .lClick(ItemBuilder.ActionType.ADD_YELLOW)
                .build(), (e) -> {
            new EnumMenu<>(main, "&7Select Argument type", Command.ArgType.values(), null, player, house, this, (type) -> {
                command.getArgs().add(new Command.CommandArg(type, "arg" + command.getArgs().size(), false, false));
                open();
            }).open();
        });

        //Back
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&aGo Back"))
                .description("To Edit Command")
                .build(), (e) -> {
            new CommandEditMenu(main, player, house, command).open();
        });
    }

    public void shiftArg(Command.CommandArg arg, boolean forward) {
        List<Command.CommandArg> args = command.getArgs();
        if (args == null) return;
        if (args.size() < 2) return;

        int index = args.indexOf(arg);
        if (forward) {
            if (index == args.size() - 1) {
                //Move to the first position
                args.remove(index);
                args.add(0, arg);
                return;
            }
            args.remove(index);
            args.add(index + 1, arg);
        } else {
            if (index == 0) {
                //Move to the last position
                args.remove(index);
                args.add(args.size(), arg);
                return;
            }
            args.remove(index);
            args.add(index - 1, arg);
        }
        setupItems();
    }
}

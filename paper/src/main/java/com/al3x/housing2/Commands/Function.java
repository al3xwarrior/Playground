package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.HousingMenu.FunctionsMenu;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class Function extends AbstractHousingCommand {
    public Function(Commands registrar, HousesManager housesManager) {
        super(registrar, housesManager);

        registrar.register(Commands.literal("function")
                .requires(ctx -> hasPermission(ctx, Permissions.EDIT_FUNCTIONS))
                .then(Commands.argument("action", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            builder.suggest("create");
                            builder.suggest("edit");
                            builder.suggest("remove");
                            builder.suggest("run");
                            return builder.buildFuture();
                        })
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> function(context, context.getSource().getSender()))))
                .build());
    }

    private int function(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;
        HousingWorld house = housesManager.getHouse(player.getWorld());

        String action = StringArgumentType.getString(context, "action");
        String name = StringArgumentType.getString(context, "name");

        switch (action) {
            case "create":
                com.al3x.housing2.Instances.Function createFunction = house.createFunction(name);
                if (createFunction == null) {
                    player.sendMessage(colorize("&cA function with that name already exists!"));
                    return Command.SINGLE_SUCCESS;
                }
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    new ActionsMenu(main, player, house, createFunction, new FunctionsMenu(main, player, house)).open();
                }, 1L);
                player.sendMessage(colorize(String.format("&aCreated function %s!", name)));
                return Command.SINGLE_SUCCESS;

            case "edit":
                com.al3x.housing2.Instances.Function editFunction = house.getFunction(name);
                if (editFunction == null) {
                    player.sendMessage(colorize("&cNo function with that name exists!"));
                    return Command.SINGLE_SUCCESS;
                }
                new ActionsMenu(main, player, house, editFunction, new FunctionsMenu(main, player, house)).open();
                return Command.SINGLE_SUCCESS;

            case "remove":
                com.al3x.housing2.Instances.Function deleteFunction = house.getFunction(name);
                if (deleteFunction == null) {
                    player.sendMessage(colorize("&cNo function with that name exists!"));
                    return Command.SINGLE_SUCCESS;
                }
                house.getFunctions().remove(deleteFunction);
                player.sendMessage(colorize("&cFunction deleted!"));
                return Command.SINGLE_SUCCESS;

            case "run":
                com.al3x.housing2.Instances.Function runFunction = house.getFunction(name);
                if (runFunction == null) {
                    player.sendMessage(colorize("&cNo function with that name exists!"));
                    return Command.SINGLE_SUCCESS;
                }
                player.sendMessage(colorize(String.format("&aRunning function %s...", name)));
                runFunction.execute(main, player, player, house, false, false, null);
                return Command.SINGLE_SUCCESS;
        }

        player.sendMessage(colorize("&cUsage: /function <create/edit/remove/run> <name>"));
        return Command.SINGLE_SUCCESS;
    }
}

package com.al3x.housing2.Commands;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class Visit extends AbstractCommand {
    public Visit(Commands commandRegistrar) {
        super(commandRegistrar);
        commandRegistrar.register(Commands.literal("visit")
                .requires(context -> context.getSender() instanceof Player)
                .then(Commands.argument("player", StringArgumentType.string())
                        .executes(context -> command(context, context.getSource().getSender(),
                                StringArgumentType.getString(context, "player")
                        ))
                )
                .build(), "Visit a player's house"
        );
    }

    @Override
    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        Player player = (Player) sender;
        String target = (String) args[0];

        if (!Main.getInstance().getHousesManager().playerHasHouse(target)) {
            player.sendMessage(colorize("&cThat player doesn't have a house!"));
            return Command.SINGLE_SUCCESS;
        }

        MyHousesMenu menu = new MyHousesMenu(Main.getInstance(), player, Bukkit.getOfflinePlayer(target));
        menu.open();
        return Command.SINGLE_SUCCESS;
    }
}

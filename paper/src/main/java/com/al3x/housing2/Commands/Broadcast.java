package com.al3x.housing2.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static com.al3x.housing2.Utils.Color.colorize;

public class Broadcast extends AbstractCommand {
    public Broadcast(Commands commandRegistrar) {
        super(commandRegistrar);
        commandRegistrar.register(Commands.literal("broadcast")
                .requires(context -> context.getSender().hasPermission("housing2.admin"))
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(context -> command(context, context.getSource().getSender(), context.getArgument("message", String.class)))
                        )
                .build()
        );
    }

    @Override
    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        String message = (String) args[0];
        Bukkit.getServer().broadcastMessage(colorize("&8[&4&lBroadcast&8] &7" + message));
        return 1;
    }
}

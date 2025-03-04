package com.al3x.housing2.Commands.newcommands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {
    public AbstractCommand(Commands commandRegistrar) {

    }

    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        return 0;
    }
}

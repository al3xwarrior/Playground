package com.al3x.housing2.Commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand {
    public AbstractCommand(Commands commandRegistrar) {

    }

    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        return 0;
    }

    protected boolean isPlayer(CommandSourceStack commandSourceStack) {
        return commandSourceStack.getSender() instanceof Player;
    }

    protected boolean isAdmin(CommandSourceStack commandSourceStack) {
        return commandSourceStack.getSender().hasPermission("housing2.admin");
    }
}

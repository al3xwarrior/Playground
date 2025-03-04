package com.al3x.housing2.Commands.newcommands;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

public abstract class AbstractHousingCommand {
    HousesManager housesManager;
    Main main;
    public AbstractHousingCommand(Commands commandRegistrar, HousesManager housesManager) {
        this.housesManager = housesManager;
        this.main = Main.getInstance();
    }

    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        return 0;
    }
}

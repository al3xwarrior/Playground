package com.al3x.housing2.Commands;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home extends AbstractCommand {
    public Home(Commands registrar) {
        super(registrar);
        registrar.register(Commands.literal("home")
                .requires(context -> context.getSender() instanceof Player)
                .executes(context -> command(context, context.getSource().getSender()))
                .build(), "Go to your home");
    }

    @Override
    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        Player player = (Player) sender;
        new MyHousesMenu(Main.getInstance(), player, player).open();
        return 1;
    }
}

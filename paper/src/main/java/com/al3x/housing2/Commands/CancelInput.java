package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.MenuManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static com.al3x.housing2.Utils.Color.colorize;

public class CancelInput extends AbstractCommand {

    public CancelInput(Commands commandRegistrar) {
        super(commandRegistrar);
        commandRegistrar.register(Commands.literal("cancelinput")
                .requires(context -> context.getSender() instanceof Player)
                .executes(this::execute)
                .build()
        );
    }

    private int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = (Player) context.getSource().getSender();
        if (MenuManager.getListener(player) != null) {
            AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
        }
        MenuManager.getPlayerMenu(player).open();

        return 1;
    }
}
package com.al3x.housing2.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class Message extends AbstractCommand {
    public static HashMap<UUID, UUID> lastMessage = new HashMap<>();

    public Message(Commands commandRegistrar) {
        super(commandRegistrar);

        commandRegistrar.register(Commands.literal("message")
                .requires(this::isPlayer)
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(context.getSource()).getFirst();

                                    return message(context, context.getSource().getSender(), StringArgumentType.getString(context, "message"), target);
                                })
                        )
                ).build(), List.of("msg", "w", "tell")
        );
    }

    private int message(CommandContext<CommandSourceStack> context, CommandSender sender, String message, Player target) {
        Player p = (Player) sender;

        String fromPrefix = PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%");
        lastMessage.put(p.getUniqueId(), target.getUniqueId());
        lastMessage.put(target.getUniqueId(), p.getUniqueId());

        String toPrefix = PlaceholderAPI.setPlaceholders(target, "%luckperms_prefix%");

        String fromMessage = "§dFrom " + fromPrefix + sender.getName() + "§f: " + message;
        String toMessage = "§dTo " + toPrefix + target.getName() + "§f: " + message;

        target.sendMessage(colorize(fromMessage));
        sender.sendMessage(colorize(toMessage));

        return 1;
    }
}

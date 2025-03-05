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
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class Reply extends AbstractCommand {
    public static HashMap<UUID, UUID> lastMessage = new HashMap<>();

    public Reply(Commands commandRegistrar) {
        super(commandRegistrar);

        commandRegistrar.register(Commands.literal("message")
                .requires(this::isPlayer)
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            return message(context, context.getSource().getSender(), StringArgumentType.getString(context, "message"));
                        })
                ).build()
        );
    }

    private int message(CommandContext<CommandSourceStack> context, CommandSender sender, String message) {
        Player p = (Player) sender;

        UUID lastMessage = Message.lastMessage.get(p.getUniqueId());

        if (lastMessage == null) {
            sender.sendMessage("You have no one to reply to.");
            return 1;
        }

        Player target = sender.getServer().getPlayer(lastMessage);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return 1;
        }

        String prefix = PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%");
        Message.lastMessage.put(p.getUniqueId(), target.getUniqueId());

        String toPrefix = PlaceholderAPI.setPlaceholders(target, "%luckperms_prefix%");


        String fromMessage = "§dFrom " + prefix + sender.getName() + "§f: " + message;
        String toMessage = "§dTo " + toPrefix + target.getName() + "§f: " + message;

        target.sendMessage(colorize(fromMessage));
        sender.sendMessage(colorize(toMessage));
        return 1;
    }
}

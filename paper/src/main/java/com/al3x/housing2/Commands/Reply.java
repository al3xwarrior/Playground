package com.al3x.housing2.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class Reply implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("Usage: /reply <message>");
            return true;
        }

        UUID lastMessage = null;
        if (commandSender instanceof Player p) {
            lastMessage = Message.lastMessage.get(p.getUniqueId());
        } else {
            lastMessage = Message.lastMessage.get(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }

        if (lastMessage == null) {
            commandSender.sendMessage("You have no one to reply to.");
            return true;
        }

        Player target = commandSender.getServer().getPlayer(lastMessage);
        if (target == null) {
            commandSender.sendMessage("Player not found.");
            return true;
        }

        String message = String.join(" ", strings);
        String prefix;
        String toPrefix = PlaceholderAPI.setPlaceholders(target, "%luckperms_prefix%");
        if (commandSender instanceof Player p) {
            prefix = PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%");
            Message.lastMessage.put(p.getUniqueId(), target.getUniqueId());
        } else {
            prefix = "§f";
            Message.lastMessage.put(UUID.fromString("00000000-0000-0000-0000-000000000000"), target.getUniqueId());
        }

        String fromMessage = "§dFrom " + prefix + commandSender.getName() + "§f: " + message;
        String toMessage = "§dTo " + toPrefix + target.getName() + "§f: " + message;

        target.sendMessage(colorize(fromMessage));
        commandSender.sendMessage(colorize(toMessage));
        return true;
    }
}

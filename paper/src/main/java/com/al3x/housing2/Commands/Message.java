package com.al3x.housing2.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class Message implements CommandExecutor {
    public static HashMap<UUID, UUID> lastMessage = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 0 || strings.length == 1) {
            commandSender.sendMessage("Usage: /message <player> <message>");
            return true;
        }

        String targetString = strings[0];
        Player target = commandSender.getServer().getPlayer(targetString);
        if (target == null) {
            commandSender.sendMessage("Player not found.");
            return true;
        }

        String message = String.join(" ", strings).substring(targetString.length() + 1);
        String fromPrefix;
        if (commandSender instanceof Player p) {
            fromPrefix = PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%");
            lastMessage.put(p.getUniqueId(), target.getUniqueId());
            lastMessage.put(target.getUniqueId(), p.getUniqueId());
        } else {
            fromPrefix = "§f";
            lastMessage.put(UUID.fromString("00000000-0000-0000-0000-000000000000"), target.getUniqueId());
            lastMessage.put(target.getUniqueId(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }

        String toPrefix = PlaceholderAPI.setPlaceholders(target, "%luckperms_prefix%");

        String fromMessage = "§dFrom " + fromPrefix + commandSender.getName() + "§f: " + message;
        String toMessage = "§dTo " + toPrefix + target.getName() + "§f: " + message;

        target.sendMessage(colorize(fromMessage));
        commandSender.sendMessage(colorize(toMessage));

        return true;
    }
}

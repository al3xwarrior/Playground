package com.al3x.housing2.Commands;

import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class Broadcast implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender.hasPermission("housing2.admin")) {
            if (strings.length == 0) {
                commandSender.sendMessage(colorize("Usage: /broadcast <message>"));
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (String string : strings) {
                message.append(string).append(" ");
            }

            Bukkit.getServer().broadcastMessage(colorize("&8[&4&lBroadcast&8] &7" + message.toString()));
            return true;
        }

        return false;
    }
}

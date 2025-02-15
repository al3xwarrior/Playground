package com.al3x.housing2.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class StaffAlerts implements CommandExecutor {

    private static final HashMap<UUID, Boolean> staffAlerts = new HashMap<>();

    public static boolean isStaffAlerts(Player player) {
        return staffAlerts.containsKey(player.getUniqueId());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players can use this command");
            return false;
        }

        if (!player.hasPermission("housing2.admin")) {
            player.sendMessage(colorize("&cYou do not have permission to use this command"));
            return false;
        }

        if (staffAlerts.containsKey(player.getUniqueId())) {
            staffAlerts.remove(player.getUniqueId());
            player.sendMessage(colorize("&cStaff alerts disabled"));
        } else {
            staffAlerts.put(player.getUniqueId(), true);
            player.sendMessage(colorize("&aStaff alerts enabled"));
        }

        return true;
    }

}

package com.al3x.housing2.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Hub implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Player player = (Player) commandSender;

        World hub = Bukkit.getWorld("world");
        if (hub == null) return true;
        player.teleport(hub.getSpawnLocation());
        return true;
    }
}

package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.ProtoolsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveSelection implements CommandExecutor {
    private final ProtoolsManager protoolsManager;

    public RemoveSelection(ProtoolsManager protoolsManager) {
        this.protoolsManager = protoolsManager;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (protoolsManager.canUseProtools(player, false)) {
            protoolsManager.clearSelection(player);
            return true;
        }

        return false;
    }
}
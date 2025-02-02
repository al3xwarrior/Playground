package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Undo implements CommandExecutor {
    private final ProtoolsManager protoolsManager;

    public Undo(ProtoolsManager protoolsManager) {
        this.protoolsManager = protoolsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (protoolsManager.canUseProtools(player, true)) {
            protoolsManager.undo(player);
            return true;
        }

        return false;
    }
}

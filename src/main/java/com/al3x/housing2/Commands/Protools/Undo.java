package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Undo implements CommandExecutor {

    private HousesManager houseManager;
    private ProtoolsManager protoolsManager;

    public Undo(HousesManager housesManager, ProtoolsManager protoolsManager) {
        this.houseManager = housesManager;
        this.protoolsManager = protoolsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player)commandSender;

        if (protoolsManager.canUseProtools(player, true)) {
            protoolsManager.undo(player);
            player.sendMessage(Color.colorize("&aUndo successful!"));
            return true;
        }

        return false;
    }
}

package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Wand implements CommandExecutor {
    private HousesManager houseManager;

    public Wand(Main main) {
        this.houseManager = main.getHousesManager();
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player)commandSender;
        if (this.houseManager.playerIsInOwnHouse(player)) {
            player.getInventory().addItem(ProtoolsManager.getWand());
            return true;
        }
        return false;
    }
}
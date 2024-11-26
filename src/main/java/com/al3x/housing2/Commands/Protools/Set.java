package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Set implements CommandExecutor {
    private ProtoolsManager protoolsManager;

    public Set(ProtoolsManager protoolsManager) {
        this.protoolsManager = protoolsManager;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player)commandSender;

        if (strings.length != 1) {
            player.sendMessage(Color.colorize("&cUsage: //set <blocks>"));
            return true;
        }

        if (protoolsManager.canUseProtools(player, false)) {
            BlockList blockList = BlockList.fromString(player, strings[0]);
            protoolsManager.setRegionTo(player, blockList);
            player.sendMessage(Color.colorize("&aSetting region set..."));
            return true;
        }

        return false;
    }
}
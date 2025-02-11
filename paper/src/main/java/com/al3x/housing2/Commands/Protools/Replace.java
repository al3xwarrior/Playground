package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Replace implements CommandExecutor {
    private final ProtoolsManager protoolsManager;

    public Replace(ProtoolsManager protoolsManager) {
        this.protoolsManager = protoolsManager;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (strings.length != 2) {
            player.sendMessage(Color.colorize("&cUsage: //replace <from> <to>"));
            return true;
        }

        if (protoolsManager.canUseProtools(player, false)) {
            BlockList from = BlockList.fromString(player, strings[0]);
            BlockList to = BlockList.fromString(player, strings[1]);
            if (from == null || to == null) {
                player.sendMessage(Color.colorize("&cUsage: //replace <from> <to>"));
                return true;
            }
            protoolsManager.setRegionTo(player, from, to);
            player.sendMessage(Color.colorize("&aReplacing region set..."));
            return true;
        }

        return false;
    }
}
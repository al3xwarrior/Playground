package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Instances.ProtoolsManager;
import com.al3x.housing2.Utils.BlockList;
import com.al3x.housing2.Utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class Sphere implements CommandExecutor {
    private final ProtoolsManager protoolsManager;

    public Sphere(ProtoolsManager protoolsManager) {
        this.protoolsManager = protoolsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (strings.length != 2) {
            player.sendMessage(Color.colorize("&cUsage: //sphere <radius> <block[s]>"));
            return true;
        }

        if (protoolsManager.canUseProtools(player, true)) {
            BlockList blockList = BlockList.fromString(player, strings[1]);
            protoolsManager.createSphere(player, Integer.parseInt(strings[0]), blockList);
            player.sendMessage(Color.colorize("&aGenerating sphere..."));
        }

        return false;
    }
}

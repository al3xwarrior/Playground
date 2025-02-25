package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.Item;
import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class CheckItemSize implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        if (!player.hasPermission("housing2.admin")) return false;

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            player.sendMessage(colorize("&7Item Size: &e" + Item.getItemNBTSize(player.getInventory().getItemInMainHand()) + " bytes"));
        } else {
            player.sendMessage(colorize("&cYou must be holding an item to check its size."));
        }

        return false;
    }
}
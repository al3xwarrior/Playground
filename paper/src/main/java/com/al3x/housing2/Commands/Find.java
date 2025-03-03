package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class Find implements TabExecutor {

    private HousesManager housesManager;

    public Find(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!commandSender.hasPermission("housing2.admin")) {
            commandSender.sendMessage(colorize("&cYou do not have permission to use this command."));
            return false;
        }

        if (strings.length != 1) {
            commandSender.sendMessage(colorize("&cUsage: /find <player>"));
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(strings[0]);
        if (target == null) {
            commandSender.sendMessage(colorize("&cPlayer not found."));
            return true;
        }

        HousingWorld house = housesManager.getHouse(target.getWorld());
        if (house == null) {
            commandSender.sendMessage(colorize("&cPlayer is not in a house."));
            return true;
        }

        commandSender.sendMessage(colorize("&e" + target.getName() + " &7is in the house &e" + house.getName() + "&7 by &e" + house.getOwner().getName() + "&7."));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        ArrayList<String> players = new ArrayList<>();

        if (strings.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
        }

        return players;
    }
}

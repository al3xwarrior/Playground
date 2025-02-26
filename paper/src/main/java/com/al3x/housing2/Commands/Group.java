package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class Group implements CommandExecutor {
    public HousesManager housesManager;

    public Group(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    // i'm allergic to variables in this code -pixel
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }
        if (args.length < 2) {
            player.sendMessage(colorize("&cUsage: /group <player> <group>"));
            return false;
        }
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (!house.hasPermission(player, Permissions.CHANGE_PLAYER_GROUP)) {
            player.sendMessage(colorize("&cYou don't have permission to change groups in this house!"));
            return false;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage(colorize("&cThat player is not online!"));
            return true;
        }
        boolean online = house.getWorld().getPlayers().contains(Bukkit.getPlayer(args[0]));
        if (!online) {
            player.sendMessage(colorize("&cThat player is not in the same house as you!"));
            return true;
        }
        if (!house.getGroups().stream().map(com.al3x.housing2.Instances.Group::getName).toList().contains(args[1])) {
            player.sendMessage(colorize("&cThat group does not exist!"));
            return false;
        }
        if (house.getGroup(args[1]).getPriority() > house.getGroup(house.getPlayersData().get(player.getUniqueId().toString()).getGroup()).getPriority()) {
            player.sendMessage(colorize("&cThis group has a higher priority than yours!"));
            return false;
        }
        if (house.getGroup(args[1]).getPriority() == 2147483647) {
            player.sendMessage(colorize("&cYou can't make another player the owner of this house!"));
            return false;
        }
        if (house.getGroup(house.getPlayersData().get(player.getUniqueId().toString()).getGroup()).getPriority() < house.getGroup(house.getPlayersData().get(Bukkit.getPlayer(args[0]).getUniqueId().toString()).getGroup()).getPriority()) {
            player.sendMessage(colorize("&cThis player has a higher priority group than you!"));
            return false;
        }
        house.getPlayersData().get(Bukkit.getPlayer(args[0]).getUniqueId().toString()).setGroup(args[1]);
        player.sendMessage(String.format(colorize("&aChanged %s's group to %s&a!"), Bukkit.getPlayer(args[0]).getName(), colorize(house.getGroup(args[1]).getDisplayName())));
        return false;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        public HousesManager housesManager;

        public TabCompleter(HousesManager housesManager) {
            this.housesManager = housesManager;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            Player player = Bukkit.getPlayer(sender.getName());

            if (player == null)
                return List.of();
            if (player.getWorld().getName().equals("world"))
                return List.of();

            HousingWorld house = housesManager.getHouse(player.getWorld());

            if (args.length == 1) {
                return player.getWorld().getPlayers().stream().map(Player::getName).toList();
            }

            if (args.length == 2) {
                return house.getGroups().stream().map(com.al3x.housing2.Instances.Group::getName).toList();
            }

            return List.of();
        }
    }
}

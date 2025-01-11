package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ItemEditor.EditItemMainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.AIR;

public class Gamemode implements CommandExecutor {

    private Main main;

    public Gamemode(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length == 0) {
            player.sendMessage(colorize("&cUsage: /gamemode <gamemode>"));
            return true;
        }

        if (main.getHousesManager().getHouse(player.getWorld()) == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return true;
        }

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        if (!house.hasPermission(player, Permissions.COMMAND_GAMEMODE)) {
            player.sendMessage(colorize("&cYou do not have permission to use this command in this house!"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("creative") || strings[0].equalsIgnoreCase("c") || strings[0].equalsIgnoreCase("1")) {
            player.setGameMode(org.bukkit.GameMode.CREATIVE);
            player.sendMessage(colorize("&aYou have set your gamemode to Creative!"));
        } else if (strings[0].equalsIgnoreCase("survival") || strings[0].equalsIgnoreCase("s") || strings[0].equalsIgnoreCase("0")) {
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.sendMessage(colorize("&aYou have set your gamemode to Survival!"));
        } else if (strings[0].equalsIgnoreCase("adventure") || strings[0].equalsIgnoreCase("a") || strings[0].equalsIgnoreCase("2")) {
            player.setGameMode(org.bukkit.GameMode.ADVENTURE);
            player.sendMessage(colorize("&aYou have set your gamemode to Adventure!"));
        } else if (strings[0].equalsIgnoreCase("spectator") || strings[0].equalsIgnoreCase("sp") || strings[0].equalsIgnoreCase("3")) {
            player.setGameMode(org.bukkit.GameMode.SPECTATOR);
            player.sendMessage(colorize("&aYou have set your gamemode to Spectator!"));
        } else {
            player.sendMessage(colorize("&cInvalid gamemode!"));
        }
        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 1) {
                ArrayList<String> list = new ArrayList<>();
                list.add("creative");
                list.add("survival");
                list.add("adventure");
                list.add("spectator");
                return list.stream().filter(s -> s.startsWith(args[0])).toList();
            }
            return Collections.emptyList();
        }
    }
}

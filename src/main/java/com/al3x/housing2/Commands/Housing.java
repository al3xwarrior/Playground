package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class Housing implements CommandExecutor {

    private HousesManager housesManager;

    public Housing(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be done by players.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("create")) {
                if (housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou already have a house!"));
                    return true;
                }

                player.sendMessage(colorize("&eCreating your house..."));
                HousingWorld house = housesManager.createHouse(player, HouseSize.LARGE);
                player.sendMessage(colorize("&aYour house has been created!"));
                house.sendPlayerToHouse(player);
                return true;
            }

            if (strings[0].equalsIgnoreCase("delete")) {
                if (!housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou don't have a house!"));
                    return true;
                }

                player.sendMessage(colorize("&cDeleting..."));
                housesManager.deleteHouse(player);
                player.sendMessage(colorize("&cHouse Deleted!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("name")) {
                if (!housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou don't have a house!"));
                    return true;
                }

                if (strings.length < 1) {
                    player.sendMessage(colorize("&cYou need to supply a name!"));
                    return true;
                }

                String fullName = "";
                for (int i = 1; i < strings.length - 2; i++) {
                    fullName += strings[i];
                    if (i + 1 != strings.length) fullName += " ";
                }

                HousingWorld house = housesManager.getHouse(player);
                house.setName(fullName);
                player.sendMessage(colorize("&aThe name of your house was set to " + fullName + "&a!"));
                
                return true;
            }

            if (strings[0].equalsIgnoreCase("goto")) {
                if (housesManager.playerHasHouse(player)) {
                    HousingWorld house = housesManager.getHouse(player);
                    house.sendPlayerToHouse(player);
                    return true;
                }
                player.sendMessage(colorize("&cYou don't have a house!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("visit")) {
                if (strings.length == 2) {
                    Player target = Bukkit.getPlayer(strings[1]);
                    if (target == null) {
                        player.sendMessage(colorize("&cThere is no player with that username online!"));
                        return true;
                    }

                    if (!housesManager.playerHasHouse(target)) {
                        player.sendMessage(colorize("&cThat player doesn't have a house!"));
                        return true;
                    }

                    player.sendMessage(colorize("&aSending you to that players house..."));
                    HousingWorld house = housesManager.getHouse(target);
                    house.sendPlayerToHouse(player);
                    return true;
                }

                player.sendMessage(colorize("&cUsage: /housing visit <player>"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("hub")) {
                World hub = Bukkit.getWorld("world");
                if (hub == null) return false;
                player.teleport(hub.getSpawnLocation());
                return true;
            }
        }

        player.sendMessage(colorize("&7&m----------------------------------------------"));
        player.sendMessage(colorize("&2&lHousing Commands:"));
        player.sendMessage(colorize("&7- &f/housing create &7&o- start the creation process"));
        player.sendMessage(colorize("&7- &f/housing delete &7&o- delete your housing"));
        player.sendMessage(colorize("&7- &f/housing name <name> &7&o- rename your housing"));
        player.sendMessage(colorize("&7- &f/housing goto &7&o- teleport to your housing"));
        player.sendMessage(colorize("&7- &f/housing visit <player> &7&o- visit another users housing"));
        player.sendMessage(colorize("&7- &f/housing hub &7&o- go back to the lobby"));
        player.sendMessage(colorize("&7&m----------------------------------------------"));

        return true;
    }
}

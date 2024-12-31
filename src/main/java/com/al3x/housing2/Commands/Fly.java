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

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.AIR;

public class Fly implements CommandExecutor {

    private Main main;

    public Fly(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) commandSender;

        if (main.getHousesManager().getHouse(player.getWorld()) == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return true;
        }

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        if (!house.hasPermission(player, Permissions.FLY)) {
            player.sendMessage(colorize("&cYou do not have permission to use this command in this house!"));
            return true;
        }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(colorize("&cYou have disabled fly mode!"));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(colorize("&aYou have enabled fly mode!"));
        }
        return true;
    }
}

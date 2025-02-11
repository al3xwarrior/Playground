package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class WTFMap implements CommandExecutor {

    private HousesManager housesManager;

    public WTFMap(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Player player = (Player) commandSender;

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return true;
        }

        player.sendMessage(colorize("&aHouse: &7" + house.getName()));
        player.sendMessage(colorize("&aOwner: &7" + house.getOwnerName()));
        player.sendMessage(colorize("&aUUID: &7" + house.getHouseUUID()));

        return true;
    }
}

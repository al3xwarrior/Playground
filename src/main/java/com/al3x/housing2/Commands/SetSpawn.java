package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawn implements CommandExecutor {

    private final Main main;
    private HousesManager housesManager;

    public SetSpawn(HousesManager housesManager, Main main) {
        this.housesManager = housesManager;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage("§cYou are not in a house world.");
            return true;
        }

        if (!house.hasPermission(player, Permissions.HOUSE_SETTINGS)) {
            player.sendMessage("§cYou are not the owner of this house.");
            return true;
        }

        house.setSpawn(player.getLocation());
        player.sendMessage("§aHouses spawn set to your current location.");
        return true;
    }
}

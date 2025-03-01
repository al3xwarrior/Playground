package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class LockHouse implements CommandExecutor {

    private HousesManager housesManager;

    public LockHouse(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command!");
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("housing2.admin")) {
            player.sendMessage(colorize("&cYou do not have permission to use this command!"));
            return false;
        }

        if (strings.length < 1) {
            player.sendMessage(colorize("&cUsage: /lockhouse <reason>"));
            return false;
        }

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou are not in a house! Stupid."));
            return false;
        }

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < strings.length; i++) {
            reason.append(strings[i]).append(" ");
        }

        house.setPrivacy(HousePrivacy.LOCKED);
        house.setLockedReason(reason.toString());

        player.sendMessage(colorize("&aHouse locked for reason: &e" + reason));

        for (Player playerInHouse : player.getWorld().getPlayers()) {
            if (playerInHouse.getUniqueId().equals(house.getOwnerUUID())) {
                playerInHouse.sendMessage(colorize("&6&m                                 "));
                playerInHouse.sendMessage(colorize("&cThis house has been &4&lLOCKED&c!"));
                playerInHouse.sendMessage(colorize("&r"));
                playerInHouse.sendMessage(colorize("&cReason: &e" + reason));
                playerInHouse.sendMessage(colorize("&r"));
                playerInHouse.sendMessage(colorize("&7&oYou may make the house public again after changes have been made."));
                playerInHouse.sendMessage(colorize("&6&m                                 "));
                continue;
            }

            house.kickPlayerFromHouse(playerInHouse);
        }

        return true;
    }
}

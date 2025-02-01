package com.al3x.housing2.Commands;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.MyHousesMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class Visit implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof OfflinePlayer)) return true;

        Player player = (Player) commandSender;

        if (strings.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);

            if (!Main.getInstance().getHousesManager().playerHasHouse(target)) {
                player.sendMessage(colorize("&cThat player doesn't have a house!"));
                return true;
            }

            MyHousesMenu menu = new MyHousesMenu(Main.getInstance(), player, target);
            menu.open();
            return true;
        } else {
            player.sendMessage(colorize("&cUsage: /visit <player>"));
        }

        return false;
    }
}

package com.al3x.housing2.Commands;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.MyHousesMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Home implements CommandExecutor {

    private Main main;

    public Home(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) commandSender;

        new MyHousesMenu(main, player, player).open();

        return false;
    }
}

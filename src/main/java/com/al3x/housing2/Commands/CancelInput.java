package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import static com.al3x.housing2.Utils.Color.colorize;

public class CancelInput implements CommandExecutor {

    private final Main main;

    public CancelInput(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be done by players.");
            return true;
        }

        Player player = (Player) commandSender;

        if (MenuManager.getListener(player) != null) AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
        MenuManager.getPlayerMenu(player).open();

        return true;
    }
}

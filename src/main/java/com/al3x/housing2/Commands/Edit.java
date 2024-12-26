package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ItemEditor.EditItemMainMenu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.AIR;

public class Edit implements CommandExecutor {

    private Main main;

    public Edit(Main main) {
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

        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == AIR || player.getInventory().getItemInMainHand().getItemMeta() == null) {
            player.sendMessage(colorize("&cYou must be holding an item to edit it!"));
            return true;
        }

        new EditItemMainMenu(player).open();
        return true;
    }
}

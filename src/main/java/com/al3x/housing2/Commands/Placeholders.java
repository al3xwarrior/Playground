package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.HandlePlaceholders;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;

public class Placeholders implements CommandExecutor {

    private Main main;

    public Placeholders(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) commandSender;

        String filter = "";
        if (strings.length > 0) {
            filter = strings[0];
            if (filter.length() < 2) {
                player.sendMessage(colorize("&cPlease enter a filter with at least 2 characters"));
                return true;
            }
        }

        if (main.getHousesManager().getHouse(player.getWorld()) == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return true;
        }

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        player.sendMessage(colorize("&eHousing Placeholders:"));
        player.sendMessage(colorize("&7Use &e/placeholders <filter> &7to filter the list."));

        List<HandlePlaceholders.Placeholder> placeholders = HandlePlaceholders.getPlaceholders();
        // Filter placeholders
        if (!filter.isEmpty()) {
            String search = filter.toLowerCase();
            placeholders = placeholders.stream().filter(i -> Color.removeColor(i.getDisplayName().toLowerCase()).contains(search)).toList();
        }

        //Go through all placeholders and send them to the player
        for (HandlePlaceholders.Placeholder placeholder : placeholders) {
            TextComponent comp = new TextComponent(colorize("&7- &6" + placeholder +
                    (placeholder.getDisplayName().equals(placeholder.getPlaceholder()) ?
                            " &7(" + HandlePlaceholders.parsePlaceholders(player, house, placeholder.getPlaceholder()) + "&7)" :
                            "") //Basically with this we are just checking if the placeholder has a unique display name, if it does we show the parsed value
            ));
            comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Color.removeColor(placeholder.getDisplayName())));
            comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(colorize("&eInsert " + Color.removeColor(placeholder.getDisplayName()) + "&e into chat"))));
            player.spigot().sendMessage(comp);
        }

        return true;
    }
}

package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
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

public class TestPlaceholder implements CommandExecutor {

    private Main main;

    public TestPlaceholder(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length == 0) {
            player.sendMessage("Usage: /testplaceholder <string>");
            return true;
        }

        if (main.getHousesManager().getHouse(player.getWorld()) == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return true;
        }

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        String message = String.join(" ", strings);
        player.sendMessage(colorize("&e" + HandlePlaceholders.parsePlaceholders(player, house, message)));

        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Override
        public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
            if (args.length == 0) {
                return HandlePlaceholders.getPlaceholders().stream().map((p) -> Color.removeColor(p.getDisplayName())).toList();
            }

            if (args.length == 1) {
                String arg = args[0];
                List<String> placeholders = new ArrayList<>(HandlePlaceholders.getPlaceholders().stream().map((p) -> Color.removeColor(p.getDisplayName())).toList());
                placeholders.removeIf((p) -> !p.toLowerCase().startsWith(args[0].toLowerCase()));

                Player player = (Player) commandSender;
                HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());

                if (house == null) return placeholders;

                ArrayList<String> newCompletions = new ArrayList<>();
                //I will work on this later lol
//                for (String placeholder : placeholders) {
//                    Regex regex = new Regex("[.+]");
//                    MatchResult match = regex.find(placeholder, 0);
//                    if (match != null && match.getRange().getStart() >= arg.length() - 1 && match.getRange().getEndInclusive() < arg.length()) {
//                        String thing = placeholder.substring(match.getRange().getStart() + 1, match.getRange().getEndInclusive());
//                        for (String more : getMore(thing, player, house)) {
//                            newCompletions.add(placeholder.substring(0, match.getRange().getStart() + 1) + more + placeholder.substring(match.getRange().getEndInclusive()));
//                        }
//                    }
//                }

                if (!newCompletions.isEmpty()) {
                    return newCompletions;
                }
                return placeholders;
            }

            return List.of();
        }

        private List<String> getMore(String thing, Player player, HousingWorld house) {
            return switch (thing) {
                case "player_stat" ->
                        house.getStatManager().getPlayerStats(player).stream().map(Stat::getStatName).toList();
                case "global_stat" ->
                        house.getStatManager().getGlobalStats().stream().map(Stat::getStatName).toList();
                case "placeholder" ->
                        HandlePlaceholders.getPlaceholders().stream().map(HandlePlaceholders.Placeholder::getPlaceholder).toList();
                default -> List.of();
            };
        }
    }
}

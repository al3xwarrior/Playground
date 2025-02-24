package com.al3x.housing2.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class GlobalChat implements CommandExecutor, TabCompleter {
    public static HashMap<UUID, Boolean> globalChat = new HashMap<>();
    public static HashMap<UUID, Boolean> isToggled = new HashMap<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length == 0) {
            if (globalChat.containsKey(player.getUniqueId())) {
                globalChat.put(player.getUniqueId(), !globalChat.get(player.getUniqueId()));
            } else {
                globalChat.put(player.getUniqueId(), true);
            }
            player.sendMessage(colorize("&7Global Chat is now " + (globalChat.get(player.getUniqueId()) ? "&aenabled" : "&cdisabled") + "&7."));
            return true;
        }

        String m = String.join(" ", strings);

        if (m.equals("toggle")) {
            isToggled.put(player.getUniqueId(), !isToggled.getOrDefault(player.getUniqueId(), false));
            player.sendMessage(colorize("&7Can See Global Chat is now " + (isToggled.get(player.getUniqueId()) ? "&cdisabled" : "&aenabled") + "&7."));
            return true;
        }

        String message = PlaceholderAPI.setPlaceholders(player, "&6[Global] %luckperms_prefix%" + player.getName() + "&7: &f");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isToggled.getOrDefault(p.getUniqueId(), false)) continue;
            p.sendMessage(colorize(message) + m);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 1) {
            return List.of("toggle");
        }
        return null;
    }
}

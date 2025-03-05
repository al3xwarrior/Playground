package com.al3x.housing2.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class GlobalChat extends AbstractCommand {
    public static HashMap<UUID, Boolean> globalChat = new HashMap<>();
    public static HashMap<UUID, Boolean> isToggled = new HashMap<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    public GlobalChat(Commands commandRegistrar) {
        super(commandRegistrar);
        commandRegistrar.register(Commands.literal("globalchat")
                .requires(context -> context.getSender() instanceof Player)
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();
                            String message = StringArgumentType.getString(context, "message");
                            return execute(player, message);
                        })
                )
                .then(Commands.literal("toggle")
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();
                            return execute(player, "toggle");
                        })
                )
                .executes(context -> {
                    Player player = (Player) context.getSource().getSender();
                    return execute(player, null);
                })
                .build(), List.of("gc")
        );
    }

    private int execute(Player player, String m) {
        if (m == null) {
            if (globalChat.containsKey(player.getUniqueId())) {
                globalChat.put(player.getUniqueId(), !globalChat.get(player.getUniqueId()));
            } else {
                globalChat.put(player.getUniqueId(), true);
            }
            player.sendMessage(colorize("&7Global Chat is now " + (globalChat.get(player.getUniqueId()) ? "&aenabled" : "&cdisabled") + "&7."));
            return 1;
        }


        if (m.equals("toggle")) {
            isToggled.put(player.getUniqueId(), !isToggled.getOrDefault(player.getUniqueId(), false));
            player.sendMessage(colorize("&7Can See Global Chat is now " + (isToggled.get(player.getUniqueId()) ? "&cdisabled" : "&aenabled") + "&7."));
            return 1;
        }

        String message = PlaceholderAPI.setPlaceholders(player, "&6[Global] %luckperms_prefix%" + player.getName() + "&7: &f");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isToggled.getOrDefault(p.getUniqueId(), false)) continue;
            p.sendMessage(colorize(message) + m);
        }

        return 1;
    }
}

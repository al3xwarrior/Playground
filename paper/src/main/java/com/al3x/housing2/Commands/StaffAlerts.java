package com.al3x.housing2.Commands;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class StaffAlerts extends AbstractCommand {
    private static final HashMap<UUID, Boolean> staffAlerts = new HashMap<>();

    public static boolean isStaffAlerts(Player player) {
        return staffAlerts.containsKey(player.getUniqueId());
    }


    public StaffAlerts(Commands commandRegistrar) {
        super(commandRegistrar);

        commandRegistrar.register(Commands.literal("staffalerts")
                .requires(this::isPlayer)
                .requires(this::isAdmin)
                .executes(context -> {
                    return staffAlerts(context, context.getSource().getSender());
                })
                .build()
        );
    }

    private int staffAlerts(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;
        if (staffAlerts.containsKey(player.getUniqueId())) {
            staffAlerts.remove(player.getUniqueId());
            player.sendMessage(colorize("&cStaff alerts disabled"));
        } else {
            staffAlerts.put(player.getUniqueId(), true);
            player.sendMessage(colorize("&aStaff alerts enabled"));
        }

        return 1;
    }
}

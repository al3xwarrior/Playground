package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class Fly extends AbstractHousingCommand{
    public Fly(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);
        commandRegistrar.register(Commands.literal("fly")
                .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.FLY))
                .executes(context -> execute(context.getSource()))
                .build());
    }

    private int execute(CommandSourceStack source) {
        Player player = (Player) source.getSender();
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(colorize("&cYou have disabled fly mode!"));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(colorize("&aYou have enabled fly mode!"));
        }
        return 1;
    }
}

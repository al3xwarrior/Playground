package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class SetSpawn extends AbstractHousingCommand {
    public SetSpawn(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("setspawn")
                .requires(ctx -> hasPermission(ctx, Permissions.HOUSE_SETTINGS))
                .executes(context -> {
                    return setSpawn(context, context.getSource().getSender());
                })
                .build()
        );
    }

    private int setSpawn(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        Location location = player.getLocation();
        house.setSpawn(location);
        player.sendMessage(colorize(String.format("&aSet house spawn location to %s, %s, %s", location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        return 1;
    }
}

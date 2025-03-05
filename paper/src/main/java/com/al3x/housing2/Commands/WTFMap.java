package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class WTFMap extends AbstractHousingCommand {
    public WTFMap(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("wtfmap")
                .requires(this::inHouse)
                .executes(context -> {
                    return wtfMap(context, context.getSource().getSender());
                })
                .build()
        );
    }

    private int wtfMap(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;

        HousingWorld house = housesManager.getHouse(player.getWorld());

        player.sendMessage(colorize("&aHouse: &7" + house.getName()));
        player.sendMessage(colorize("&aOwner: &7" + house.getOwnerName()));
        player.sendMessage(colorize("&aUUID: &7" + house.getHouseUUID()));

        return 1;
    }
}

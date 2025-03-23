package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class SetHouseSize extends AbstractHousingCommand {
    public SetHouseSize(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("sethousesize")
                .then(Commands.argument("size", StringArgumentType.greedyString())
                        .requires(ctx -> ctx.getSender() instanceof Player p && p.hasPermission("housing2.admin"))
                        .executes(context -> {
                            return execute(context, StringArgumentType.getString(context, "size"));
                        })
                ).build()
        );
    }

    private int execute(CommandContext<CommandSourceStack> context, String size) {
        Player player = (Player) context.getSource().getSender();

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou must be in a house to use this command!"));
            return 1;
        }

        house.setSize(Integer.parseInt(size));
        player.sendMessage(colorize("&aHouse size set to " + size + "! Please reload the house."));
        house.save();

        return 1;
    }
}
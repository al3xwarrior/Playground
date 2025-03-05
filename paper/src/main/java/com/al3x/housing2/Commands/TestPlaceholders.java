package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class TestPlaceholders extends AbstractHousingCommand{
    public TestPlaceholders(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("testplaceholder")
                .then(Commands.argument("placeholder", StringArgumentType.greedyString())
                        .requires(ctx -> hasPermission(ctx, Permissions.EDIT_ACTIONS))
                        .suggests((ctx, builder) -> getPlaceholdersSuggestions(ctx, builder, true))
                        .executes(context -> {
                            return testPlaceholder(context, context.getSource().getSender(), StringArgumentType.getString(context, "placeholder"));
                        })
                )
                .build()
        );
    }

    private int testPlaceholder(CommandContext<CommandSourceStack> context, CommandSender sender, String placeholder) {
        Player player = (Player) sender;
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        player.sendMessage(colorize("&e" + HandlePlaceholders.parsePlaceholders(player, house, placeholder)));

        return 1;
    }
}

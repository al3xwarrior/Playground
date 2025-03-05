package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static com.al3x.housing2.Utils.Color.colorize;

public class ViewGlobalStats extends AbstractHousingCommand {

    public ViewGlobalStats(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("viewglobalstats")
                .requires(ctx -> hasPermission(ctx, Permissions.COMMAND_EDITSTATS))
                .executes(context -> {
                    return viewGlobalStats(context, context.getSource().getSender());
                })
                .build()
        );
    }

    private int viewGlobalStats(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;
        HousingWorld house = housesManager.getHouse(player.getWorld());

        if (house.getStatManager().getGlobalStats() == null || house.getStatManager().getGlobalStats().isEmpty()) {
            player.sendMessage(colorize("&cThe current house has no stats"));
            return 1;
        }
        player.sendMessage(colorize("&aGlobal stats for " + house.getName() + ":"));
        house.getStatManager().getGlobalStats().forEach((stat) -> {
            player.sendMessage(colorize("&a" + stat.getStatName() + ": &f" + stat.getValue()));
        });

        return 1;
    }
}

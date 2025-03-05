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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.al3x.housing2.Utils.Color.colorize;

public class EditGlobalStats extends AbstractHousingCommand {

    public EditGlobalStats(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("editglobalstats")
                .requires(ctx -> hasPermission(ctx, Permissions.COMMAND_EDITSTATS))
                .then(Commands.argument("name", StringArgumentType.string())
                        .then(Commands.argument("operator", StringArgumentType.word())
                                .suggests(this::getOperators)
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .executes(context -> {
                                            return editGlobalStats(context, context.getSource().getSender());
                                        })
                                )
                        )
                )
                .build()
        );
    }

    private CompletableFuture<Suggestions> getOperators(CommandContext<CommandSourceStack> context, SuggestionsBuilder suggestionsBuilder) {
        Arrays.stream(StatOperation.values())
                .filter(entry -> entry.name().toLowerCase().startsWith(suggestionsBuilder.getRemaining().toLowerCase()))
                .forEach(statOperation -> suggestionsBuilder.suggest(statOperation.name()));
        return suggestionsBuilder.buildFuture();
    }

    private int editGlobalStats(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;
        HousingWorld house = housesManager.getHouse(player.getWorld());

        String statName = StringArgumentType.getString(context, "name");
        StatOperation operation = StatOperation.valueOf(StringArgumentType.getString(context, "operator").toUpperCase());
        String value = Placeholder.handlePlaceholders(StringArgumentType.getString(context, "value"), house, null);

        Stat stat = house.getStatManager().getGlobalStatByName(statName);

        try {
            stat.modifyStat(operation, value);
        } catch (IllegalArgumentException e) {
            player.sendMessage(colorize("&cFailed to modify stat: " + statName + " with mode: " + operation + " and value: " + value));
            return 0;
        }
        return 1;
    }
}

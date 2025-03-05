package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class ViewPlayerStats extends AbstractHousingCommand {

    public ViewPlayerStats(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("viewplayerstats")
                .requires(ctx -> hasPermission(ctx, Permissions.COMMAND_EDITSTATS))
                .then(Commands.argument("target", StringArgumentType.word())
                        .suggests(Housing::getPlayerWorldSuggestions)
                        .executes(context -> {
                            return viewPlayerStats(context, context.getSource().getSender());
                        })
                        .build()
                )
                .build()
        );
    }

    private int viewPlayerStats(CommandContext<CommandSourceStack> context, CommandSender sender) {
        Player player = (Player) sender;
        HousingWorld house = housesManager.getHouse(player.getWorld());
        String targetName = StringArgumentType.getString(context, "target");
        Player target = player.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(colorize("&cPlayer not found."));
            return 1;
        }

        if (house.getStatManager().getPlayerStats(target) == null || house.getStatManager().getPlayerStats(target).isEmpty()) {
            player.sendMessage(colorize("&cThat player has no stats!"));
            return 1;
        }
        player.sendMessage(colorize("&aStats for " + target.getName() + ":"));
        house.getStatManager().getPlayerStats(target).forEach((stat) -> {
            player.sendMessage(colorize("&a" + stat.getStatName() + ": &f" + stat.getValue()));
        });
        return 1;
    }
}

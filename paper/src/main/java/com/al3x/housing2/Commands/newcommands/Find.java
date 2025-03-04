package com.al3x.housing2.Commands.newcommands;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class Find extends AbstractHousingCommand {
    public Find(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);
        commandRegistrar.register(Commands.literal("find")
                .requires(context -> context.getSender().hasPermission("housing2.admin"))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> {
                            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                            final Player target = targetResolver.resolve(context.getSource()).getFirst();
                            return execute(context.getSource(), target);
                        }))
                .build());
    }

    private int execute(CommandSourceStack source, Player target) {
        HousingWorld house = housesManager.getHouse(target.getWorld());
        if (house == null) {
            source.getSender().sendMessage(colorize("&cPlayer is not in a house."));
            return 1;
        }

        source.getSender().sendMessage(colorize("&e" + target.getName() + " &7is in the house &e" + house.getName() + "&7 by &e" + house.getOwner().getName() + "&7."));
        return 1;
    }
}

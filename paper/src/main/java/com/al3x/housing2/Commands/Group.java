package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class Group extends AbstractHousingCommand {
    public Group(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);
        commandRegistrar.register(Commands.literal("group")
                .then(Commands.argument("target", StringArgumentType.word())
                        .suggests(Housing::getPlayerWorldSuggestions)
                        .then(Commands.argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    HousingWorld house = housesManager.getHouse(((Player) context.getSource().getSender()).getWorld());
                                    house.getGroups().forEach(group -> builder.suggest(group.getName()));
                                    return builder.buildFuture();
                                })
                                .executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "target"), StringArgumentType.getString(context, "name")))
                        )
                )
                .build()
        );
    }

    private int execute(CommandSourceStack source, String target, String name) {
        Player player = (Player) source.getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (!house.hasPermission(player, Permissions.CHANGE_PLAYER_GROUP)) {
            player.sendMessage(colorize("&cYou don't have permission to change groups in this house!"));
            return 1;
        }
        if (Bukkit.getPlayer(target) == null) {
            player.sendMessage(colorize("&cThat player is not online!"));
            return 1;
        }
        if (Bukkit.getPlayer(target) == player) {
            player.sendMessage(colorize("&cYou can't change your own group!"));
            return 1;
        }
        boolean online = house.getWorld().getPlayers().contains(Bukkit.getPlayer(target));
        if (!online) {
            player.sendMessage(colorize("&cThat player is not in the same house as you!"));
            return 1;
        }
        if (!house.getGroups().stream().map(com.al3x.housing2.Instances.Group::getName).toList().contains(name)) {
            player.sendMessage(colorize("&cThat group does not exist!"));
            return 1;
        }
        if (house.getGroup(name).getPriority() > house.getGroup(house.getPlayersData().get(player.getUniqueId().toString()).getGroup()).getPriority()) {
            player.sendMessage(colorize("&cThis group has a higher priority than yours!"));
            return 1;
        }
        if (house.getGroup(name).getPriority() == 2147483647) {
            player.sendMessage(colorize("&cYou can't make another player the owner of this house!"));
            return 1;
        }
        if (house.getGroup(house.getPlayersData().get(player.getUniqueId().toString()).getGroup()).getPriority() < house.getGroup(house.getPlayersData().get(Bukkit.getPlayer(target).getUniqueId().toString()).getGroup()).getPriority()) {
            player.sendMessage(colorize("&cThis player has a higher priority group than you!"));
            return 1;
        }
        house.getPlayersData().get(Bukkit.getPlayer(target).getUniqueId().toString()).setGroup(name);
        player.sendMessage(String.format(colorize("&aChanged %s's group to %s&a!"), Bukkit.getPlayer(target).getName(), colorize(house.getGroup(name).getDisplayName())));
        return 1;
    }
}

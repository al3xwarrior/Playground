package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class Gamemode extends AbstractHousingCommand{
    public Gamemode(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);
        commandRegistrar.register(Commands.literal("gamemode")
                .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.COMMAND_GAMEMODE))
                        .then(Commands.argument("gamemode", ArgumentTypes.gameMode())
                        .executes(context -> execute(context.getSource(), context.getArgument("gamemode", GameMode.class))))
                .build());
    }

    private int execute(CommandSourceStack source, GameMode gamemode) {
        Player player = (Player) source.getSender();
        player.setGameMode(gamemode);
        return 1;
    }
}

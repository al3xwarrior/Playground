package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class LockHouse extends AbstractHousingCommand {
    public LockHouse(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);
        commandRegistrar.register(Commands.literal("lockhouse")
                .requires(this::inHouse)
                .requires(this::isAdmin)
                .then(Commands.argument("reason", StringArgumentType.greedyString())
                        .executes(context -> {
                            String reason = StringArgumentType.getString(context, "reason");
                            lockHouse(context, reason);
                            return 1;
                        })
                )
                .build()
        );
    }

    private void lockHouse(CommandContext<CommandSourceStack> context, String reason) {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());

        house.setPrivacy(HousePrivacy.LOCKED);
        house.setLockedReason(reason.toString());

        player.sendMessage(colorize("&aHouse locked for reason: &e" + reason));

        for (Player playerInHouse : player.getWorld().getPlayers()) {
            if (playerInHouse.getUniqueId().equals(house.getOwnerUUID())) {
                playerInHouse.sendMessage(colorize("&6&m                                 "));
                playerInHouse.sendMessage(colorize("&cThis house has been &4&lLOCKED&c!"));
                playerInHouse.sendMessage(colorize("&r"));
                playerInHouse.sendMessage(colorize("&cReason: &e" + reason));
                playerInHouse.sendMessage(colorize("&r"));
                playerInHouse.sendMessage(colorize("&7&oYou may make the house public again after changes have been made."));
                playerInHouse.sendMessage(colorize("&6&m                                 "));
                continue;
            }

            house.kickPlayerFromHouse(playerInHouse);
        }
    }
}

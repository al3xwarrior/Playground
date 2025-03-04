package com.al3x.housing2.Commands.newcommands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Menus.ItemEditor.EditItemMainMenu;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.AIR;

public class Edit extends AbstractHousingCommand {
    public Edit(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("edit")
                .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.ITEM_EDITOR))
                .executes(this::execute)
                .build()
        );
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();

        if (player.getInventory().getItemInMainHand().getType() == AIR || player.getInventory().getItemInMainHand().getItemMeta() == null) {
            player.sendMessage(colorize("&cYou must be holding an item to edit it!"));
            return 1;
        }

        new EditItemMainMenu(player).open();
        return 1;
    }
}

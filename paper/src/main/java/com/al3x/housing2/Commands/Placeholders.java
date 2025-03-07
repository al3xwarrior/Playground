package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Color;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class Placeholders extends AbstractHousingCommand {
    public Placeholders(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("placeholders")
                .requires(ctx -> hasPermission(ctx, Permissions.EDIT_ACTIONS))
                .then(Commands.argument("filter", StringArgumentType.greedyString())
                        .suggests((ctx, builder) -> getPlaceholdersSuggestions(ctx, builder, false))
                        .executes(context -> {
                            return placeholders(context, context.getSource().getSender(), StringArgumentType.getString(context, "filter"));
                        })
                )
                .executes(context -> {
                    return placeholders(context, context.getSource().getSender(), "");
                })
                .build()
        );
    }

    private int placeholders(CommandContext<CommandSourceStack> context, CommandSender sender, String filter) {
        Player player = (Player) sender;
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        player.sendMessage(colorize("&eHousing Placeholders:"));
        player.sendMessage(colorize("&7Use &e/placeholders <filter> &7to filter the list."));

        List<Placeholder> placeholders = Placeholder.placeholders;
        // Filter placeholders
        if (!filter.isEmpty()) {
            String search = filter.toLowerCase();
            placeholders = placeholders.stream().filter(i -> Color.removeColor(i.getDisplayName().toLowerCase()).contains(search)).toList();
        }

        //Go through all placeholders and send them to the player
        for (Placeholder placeholder : placeholders) {
            TextComponent comp = new TextComponent(colorize("&7- &6" + placeholder +
                    (!placeholder.hasArgs() ?
                            " &7(" + placeholder.handlePlaceholder("", house, player) + "&7)" :
                            "") //Basically with this we are just checking if the placeholder has a unique display name, if it does we show the parsed value
            ));
            comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Color.removeColor(placeholder.getDisplayName())));
            comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(colorize("&eInsert " + Color.removeColor(placeholder.getDisplayName()) + "&e into chat"))));
            player.spigot().sendMessage(comp);
        }

        return 1;
    }
}

package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Color;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractHousingCommand extends AbstractCommand {
    HousesManager housesManager;
    Main main;
    public AbstractHousingCommand(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar);
        this.housesManager = housesManager;
        this.main = Main.getInstance();
    }

    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        return 0;
    }

    protected boolean inHouse(CommandSourceStack commandSourceStack) {
        return commandSourceStack.getSender() instanceof Player p && housesManager.getHouse(p.getWorld()) != null;
    }

    protected boolean hasPermission(CommandSourceStack commandSourceStack, Permissions permission) {
        return commandSourceStack.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, permission);
    }

    public static CompletableFuture<Suggestions> getPlaceholdersSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder, boolean symbol) {
        List<Placeholder> placeholders = Placeholder.placeholders;
        if (!symbol) {
            placeholders.stream()
                    .filter(entry -> Color.removeColor(entry.getDisplayName().toLowerCase()).startsWith(builder.getRemainingLowerCase()))
                    .forEach((entry) -> builder.suggest(Color.removeColor(entry.getDisplayName().toLowerCase())));
        } else {
            placeholders.stream()
                    .filter(entry -> entry.getPlaceholder().startsWith(builder.getRemaining()))
                    .forEach((entry) -> builder.suggest(entry.getPlaceholder()));
        }
        return builder.buildFuture();
    }
}

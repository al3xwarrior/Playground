package com.al3x.housing2.Commands;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Network.PlayerNetwork;
import com.al3x.housing2.network.payload.clientbound.ClientboundSyntax;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.al3x.housing2.Utils.Color.colorize;

public class Housing extends AbstractHousingCommand implements HousingPunishments {
    public Housing(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("housing")
                .requires(context -> context.getSender() instanceof Player)
                .then(Commands.literal("create").executes(this::create))
                .then(Commands.literal("name")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.playerHasHouse(p))
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(this::name)
                        )
                )
                .then(Commands.literal("browse").executes(this::browse))
                .then(Commands.literal("menu")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.getHouse(p.getWorld()) != null)
                        .executes(this::menu)
                )
                .then(Commands.literal("random").executes(this::random))
                .then(Commands.literal("ptsl").executes(this::ptsl))
                .then(Commands.literal("kick")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.KICK))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .suggests(Housing::getPlayerWorldSuggestions)
                                .executes(this::kick)
                        )
                )
                .then(Commands.literal("ban")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.BAN))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .then(Commands.argument("duration", StringArgumentType.string())
                                        .executes(this::ban)
                                )
                                .suggests(Housing::getPlayerWorldSuggestions)
                                .executes(this::ban)
                        )
                )
                .then(Commands.literal("unban")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.BAN))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .suggests(Housing::getPlayerOnlineSuggestions)
                                .executes(this::unban)
                        )
                )
                .then(Commands.literal("kickall")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.KICK))
                        .executes(this::kickall)
                )
                .then(Commands.literal("kickallguests")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.KICK))
                        .executes(this::kickallguests)
                )
                .then(Commands.literal("mute")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.MUTE))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .then(Commands.argument("duration", StringArgumentType.string())
                                        .executes(this::mute)
                                )
                                .suggests(Housing::getPlayerWorldSuggestions)
                                .executes(this::mute)
                        )
                )
                .then(Commands.literal("unmute")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.MUTE))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .suggests(Housing::getPlayerWorldSuggestions)
                                .executes(this::unmute)
                        )
                )

                .then(Commands.literal("tp")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.COMMAND_TP))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .suggests(Housing::getPlayerWorldSuggestions)
                                .executes(this::tp)
                        )
                        .then(Commands.argument("location", ArgumentTypes.finePosition(true))
                                .executes(this::tp)
                        )
                )
                .then(Commands.literal("tpall")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.COMMAND_TP))
                        .executes(this::tpall)
                )
                .then(Commands.literal("invite")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.HOUSE_SETTINGS))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(Housing::getPlayerOnlineSuggestions)
                                .executes(this::invite)
                        )
                )
                .then(Commands.literal("whitelist")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.HOUSE_SETTINGS))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(Housing::getPlayerOnlineSuggestions)
                                .executes(this::whitelist)
                        )
                )
                .then(Commands.literal("unwhitelist")
                        .requires(context -> context.getSender() instanceof Player p && housesManager.hasPermissionInHouse(p, Permissions.HOUSE_SETTINGS))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(Housing::getPlayerOnlineSuggestions)
                                .executes(this::unwhitelist)
                        )
                )
                .then(Commands.literal("migrate")
                        .requires(this::isAdmin)
                        .executes(this::migrate)
                )
                .then(Commands.literal("reload")
                        .requires(this::isAdmin)
                        .executes(this::reload)
                )
                .then(Commands.literal("help").executes(this::help))
                .executes(this::help)
                .build(), List.of("h")
        );
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        housesManager.clear();
        housesManager.loadPlayerHouses();

        return Command.SINGLE_SUCCESS;
    }

    private int migrate(CommandContext<CommandSourceStack> context) {
//        housesManager.migrateHouses();
        return Command.SINGLE_SUCCESS;
    }

    private int tpall(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        player.sendMessage(colorize("&aTeleporting all players to your location"));
        for (Player p : house.getWorld().getPlayers()) {
            p.teleportAsync(player.getLocation());
        }

        return Command.SINGLE_SUCCESS;
    }

    private int tp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        try {
            String target = StringArgumentType.getString(context, "target");
            Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer != null && house.getWorld().equals(targetPlayer.getWorld())) {
                player.teleportAsync(targetPlayer.getLocation());
                player.sendMessage(colorize(String.format("&aTeleporting you to %s", targetPlayer.getName())));
            } else {
                player.sendMessage(colorize("&cPlayer not found!"));
            }
        } catch (IllegalArgumentException e) {
            final FinePositionResolver resolver = context.getArgument("location", FinePositionResolver.class);
            final FinePosition finePosition = resolver.resolve(context.getSource());
            player.teleportAsync(finePosition.toLocation(player.getWorld()));
            player.sendMessage(colorize(String.format("&aTeleporting you to %s, %s, %s", finePosition.blockX(), finePosition.blockY(), finePosition.blockZ())));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static CompletableFuture<Suggestions> getPlayerWorldSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        List<Player> players = ((Player) ctx.getSource().getSender()).getWorld().getPlayers();
        players.stream()
                .filter(entry -> entry.getName().toLowerCase().startsWith(builder.getRemainingLowerCase()))
                .forEach((entry) -> builder.suggest(entry.getName()));
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> getPlayerOnlineSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        players.stream()
                .filter(entry -> entry.getName().toLowerCase().startsWith(builder.getRemainingLowerCase()))
                .forEach((entry) -> builder.suggest(entry.getName()));
        return builder.buildFuture();
    }

    private int help(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();
        player.sendMessage(colorize("&7&m---------------------------------------"));
        player.sendMessage(colorize("&6&lHousing Commands:"));
        player.sendMessage(colorize("&7- &f/housing create &7&o- start the creation process"));
        player.sendMessage(colorize("&7- &f/housing name <name> &7&o- rename your housing"));
        player.sendMessage(colorize("&7- &f/housing menu &7&o- view the housing browser"));
        player.sendMessage(colorize("&7- &f/housing kick <player> &7&o- kick player from your house"));
        player.sendMessage(colorize("&7- &f/housing ban <player> <duration>&7&o- bans player from your house"));
        player.sendMessage(colorize("&7- &f/housing unban <player> &7&o- unbans player from your house"));
        player.sendMessage(colorize("&7- &f/housing mute <player> <duration>&7&o- mutes player in your house"));
        player.sendMessage(colorize("&7- &f/housing unmute <player> &7&o- unmutes player in your house"));
        player.sendMessage(colorize("&7- &f/housing kickall &7&o- kick all players from your house"));
        player.sendMessage(colorize("&7- &f/housing kickallguests &7&o- kick all players with the default group from your house"));
        player.sendMessage(colorize("&7&m---------------------------------------"));

        return Command.SINGLE_SUCCESS;
    }

    private int ptsl(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        if (PlayerNetwork.getNetwork(player).isUsingMod()) {
            String actionsSyntax = "";
            for (HTSLImpl action : Arrays.stream(ActionEnum.values()).map(ActionEnum::getActionInstance).filter(a -> a instanceof HTSLImpl).map(a -> (HTSLImpl) a).toList()) {
                actionsSyntax += action.syntax() + "\n";
            }

            String conditionsSyntax = "";
            for (CHTSLImpl condition : Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).filter(c -> c instanceof CHTSLImpl).map(c -> (CHTSLImpl) c).toList()) {
                conditionsSyntax += condition.syntax() + "\n";
            }

            ClientboundSyntax syntax = new ClientboundSyntax(actionsSyntax, conditionsSyntax);
            PlayerNetwork.getNetwork(player).sendMessage(syntax);
        } else {
            player.sendMessage(colorize("&cYou must be using the mod to use this command!"));
        }

        return Command.SINGLE_SUCCESS;
    }

    HashMap<UUID, Long> randomCooldown = new HashMap<>();

    private int random(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        if (randomCooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis() - randomCooldown.get(player.getUniqueId()) < 5000) {
            player.sendMessage(colorize("&cYou must wait 5 seconds before using this command again!"));
            return Command.SINGLE_SUCCESS;
        }
        randomCooldown.put(player.getUniqueId(), System.currentTimeMillis());
        HousingWorld house = housesManager.getRandomPublicHouse(player);
        if (house != null) {
            house.sendPlayerToHouse(player);
        } else {
            player.sendMessage(colorize("&cThere are no public houses available!"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private int menu(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house.hasPermission(player, Permissions.HOUSING_MENU)) {
            new HousingMenu(main, player, house).open();
            return Command.SINGLE_SUCCESS;
        }
        player.sendMessage(colorize("&cYou do not have permission to use the housing menu!"));
        return Command.SINGLE_SUCCESS;
    }

    private int browse(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        new HouseBrowserMenu(player, housesManager).open();
        return Command.SINGLE_SUCCESS;
    }

    private int name(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return Command.SINGLE_SUCCESS;
        }

        String name = StringArgumentType.getString(context, "name");
        house.setName(name);
        player.sendMessage(colorize("&aThe name of your house was set to " + name + "&a!"));
        return Command.SINGLE_SUCCESS;
    }

    private int invite(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return Command.SINGLE_SUCCESS;
        }

        Player invitedPlayer = Bukkit.getPlayer(StringArgumentType.getString(context, "player"));
        if (invitedPlayer == null) {
            player.sendMessage(colorize("&cInvalid player!"));
            return Command.SINGLE_SUCCESS;
        }
        house.setInvitedPlayer(invitedPlayer);
        invitedPlayer.sendMessage(colorize("&eYou were invited by &b" + player.getName() + " &eto visit the house &b" + house.getName()));
        player.sendMessage(colorize("&eSent &b" + invitedPlayer.getName() + "&e an invite!"));
        return Command.SINGLE_SUCCESS;
    }

    private int whitelist(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return Command.SINGLE_SUCCESS;
        }

        house.addWhitelistedPlayer(Bukkit.getOfflinePlayer(StringArgumentType.getString(context, "player")));
        player.sendMessage(colorize("&eAdded to the whitelist!"));
        return Command.SINGLE_SUCCESS;
    }

    private int unwhitelist(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            player.sendMessage(colorize("&cYou are not in a house!"));
            return Command.SINGLE_SUCCESS;
        }

        house.removeWhitelistedPlayer(Bukkit.getOfflinePlayer(StringArgumentType.getString(context, "player")));
        player.sendMessage(colorize("&eRemoved from the whitelist!"));
        return Command.SINGLE_SUCCESS;
    }

    protected int create(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        if (housesManager.getHouseCount(player) >= 3) {
            player.sendMessage(colorize("&cYou have the maximum amount of houses!"));
            return Command.SINGLE_SUCCESS;
        }
        player.sendMessage(colorize("&eCreating your house..."));
        HousingWorld house = housesManager.createHouse(player, HouseSize.XLARGE);
        house.runOnLoadOrNow((h) -> {
            player.sendMessage(colorize("&aYour house has been created!"));
            h.sendPlayerToHouse(player);
        });
        return Command.SINGLE_SUCCESS;
    }

    @Override
    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        return 0; // we wont use this
    }
}

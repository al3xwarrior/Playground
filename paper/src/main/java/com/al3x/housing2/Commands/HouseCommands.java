package com.al3x.housing2.Commands;

import com.al3x.housing2.Instances.Command;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.al3x.housing2.Instances.CommandRegistry.commandArgsResults;

public class HouseCommands {
    public static void register(Commands commandRegistrar) {
        //Were just not going to look down here :)

        for (HousingWorld house : Main.getInstance().getHousesManager().getLoadedHouses()) {

            for (Command command : house.getCommands()) {
                LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(command.getName())
                        .requires(ctx -> ((Player) ctx.getSender()).getWorld().equals(house.getWorld()));
                List<ArgumentBuilder<CommandSourceStack, ?>> argumentBuilders = new ArrayList<>();

                for (Command.CommandArg argument : command.getArgs()) {
                    ArgumentType type = switch (argument.getType()) {
                        case STRING ->
                                (argument.isGreedy() ? StringArgumentType.greedyString() : StringArgumentType.string());
                        case INT -> IntegerArgumentType.integer();
                        case BOOLEAN -> BoolArgumentType.bool();
                        case DOUBLE -> DoubleArgumentType.doubleArg();
                        case PLAYER -> StringArgumentType.word();
                    };

                    ArgumentBuilder<CommandSourceStack, ?> newBuilder = Commands.argument(argument.getName(), type);
                    if (!argument.isRequired()) {
                        newBuilder.executes(context -> housingCommand(context, command, house));
                    }
                    argumentBuilders.add(newBuilder);
                }

                //help me chatgippity
                ArgumentBuilder<CommandSourceStack, ?> firstArgument = (!argumentBuilders.isEmpty()) ? argumentBuilders.getFirst() : null;
                if (firstArgument != null) {
                    AtomicReference<ArgumentBuilder<CommandSourceStack, ?>> currentBuilder = new AtomicReference<>(firstArgument);
                    for (int i = 1; i < argumentBuilders.size(); i++) {
                        ArgumentBuilder<CommandSourceStack, ?> argument = argumentBuilders.get(i);
                        if (i == argumentBuilders.size() - 1) {
                            argument.executes(context -> housingCommand(context, command, house));
                        }
                        currentBuilder.get().then(argument);
                        currentBuilder.set(argument);
                    }
                    builder.then(firstArgument);
                } else {
                    builder.executes(context -> housingCommand(context, command, house));
                }

                commandRegistrar.register(builder.build());
            }
        }
    }

    private static int housingCommand(CommandContext<CommandSourceStack> context, Command command, HousingWorld housingWorld) {
        Player player = (Player) context.getSource().getSender();
        List<String> results = new ArrayList<>();
        for (Command.CommandArg arg : command.getArgs()) {
            switch (arg.getType()) {
                case PLAYER -> {
                    String playerName = StringArgumentType.getString(context, arg.getName());
                    Player target = Bukkit.getPlayer(playerName);
                    if (target == null) {
                        player.sendMessage("Player not found");
                        return 1;
                    }
                    results.add(target.getName());
                }
                case INT -> {
                    int number = IntegerArgumentType.getInteger(context, arg.getName());
                    results.add(String.valueOf(number));
                }
                case DOUBLE -> {
                    double number = DoubleArgumentType.getDouble(context, arg.getName());
                    results.add(String.valueOf(number));
                }
                case BOOLEAN -> {
                    boolean bool = BoolArgumentType.getBool(context, arg.getName());
                    results.add(String.valueOf(bool));
                }
                case STRING -> {
                    String string = StringArgumentType.getString(context, arg.getName());
                    results.add(string);
                }
                case null, default -> {
                }
            }
        }

        commandArgsResults.remove(player.getUniqueId());

        commandArgsResults.put(player.getUniqueId(), results);

        command.execute(player, housingWorld);

        return 1;
    }
}

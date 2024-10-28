package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class CommandRegistry extends BukkitCommand {
    public static HashMap<UUID, List<String>> commandArgsResults = new HashMap<>();

    private Command command;
    protected CommandRegistry(@NotNull String name, Command comand) {
        super(name);
        this.command = comand;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String commandName, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        HousingWorld world = Main.getInstance().getHousesManager().getHouse(player.getWorld());

        if (world == null) return true;

        List<String> args = List.of(strings);

        List<String> results = new ArrayList<>();
        for (int i = 0; i < command.getArgs().size(); i++) {
            Command.CommandArg arg = command.getArgs().get(i);

            // Check if the argument is required and if it is, check if it exists
            if (args.size() < i + 1) {
                if (arg.isRequired()) {
                    player.sendMessage(colorize("&cUsage: " + command.getUsage()));
                    return true;
                }
                continue;
            }

            //Check if the argument is greedy and if so, combine all the arguments into one
            String argValue = args.get(i);
            if (arg.isGreedy()) {
                for (int j = i + 1; j < args.size(); j++) {
                    argValue += " " + args.get(j);
                }
            }

            // Validate the argument
            String finalArgValue = argValue;
            switch (arg.getType()) {
                case STRING -> {
                    if (!argValue.matches(".*[a-zA-Z]+.*")) {
                        player.sendMessage(colorize("&cUsage: " + command.getUsage()));
                        return true;
                    }

                }
                case INT -> {
                    if (!argValue.matches(".*[0-9]+.*")) {
                        player.sendMessage(colorize("&cUsage: " + command.getUsage()));
                        return true;
                    }
                }
                case BOOLEAN -> {
                    if (!argValue.equalsIgnoreCase("true") && !argValue.equalsIgnoreCase("false")) {
                        player.sendMessage(colorize("&cUsage: " + command.getUsage()));
                        return true;
                    }
                }
                case PLAYER -> {
                    if (world.getWorld().getPlayers().stream().noneMatch(p -> p.getName().equalsIgnoreCase(finalArgValue))) {
                        player.sendMessage(colorize("&cUsage: " + command.getUsage()));
                        return true;
                    }
                }
                case DOUBLE -> {
                    if (!argValue.matches(".*[0-9.]+.*")) {
                        player.sendMessage(colorize("&cUsage: " + command.getUsage()));
                        return true;
                    }
                }
            }

            // Add the argument to the results
            results.add(argValue);
        }
        commandArgsResults.put(player.getUniqueId(), results);

        command.execute(player, world, results);

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!(sender instanceof Player player)) return List.of();

        HousingWorld world = Main.getInstance().getHousesManager().getHouse(player.getWorld());

        if (world == null) return List.of();

        List<String> completions = new ArrayList<>();
        List<Command.CommandArg> commandArgs = command.getArgs();
        for (int i = 0; i < commandArgs.size(); i++) {
            Command.CommandArg arg = commandArgs.get(i);
            if (args.length == i + 1) {
                if (arg.getType() == Command.ArgType.PLAYER) {
                    for (Player p : world.getWorld().getPlayers()) {
                        completions.add(p.getName());
                    }
                } else if (arg.getType() == Command.ArgType.BOOLEAN) {
                    completions.add("true");
                    completions.add("false");
                }
            }
        }
        return completions;
    }
}

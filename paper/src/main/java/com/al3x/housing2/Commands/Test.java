package com.al3x.housing2.Commands;

import com.al3x.housing2.Tests.ActionSaveTest;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Tests.Test.all;

public class Test extends AbstractCommand {
    public static HashMap<UUID, Boolean> memory = new HashMap<>();

    public Test(Commands commandRegistrar) {
        super(commandRegistrar);

        commandRegistrar.register(Commands.literal("test")
                .requires((ctx) -> isAdmin(ctx) && isPlayer(ctx))
                .then(Commands.argument("arg1", StringArgumentType.word())
                        .suggests((ctx, builder) -> builder.suggest("all").suggest("memory").suggest("actionsave").buildFuture())
                        .executes(ctx -> command(ctx, ctx.getSource().getSender(), StringArgumentType.getString(ctx, "arg1")))
                )
                .build()
        );
    }

    @Override
    protected int command(CommandContext<CommandSourceStack> ctx, CommandSender sender, Object... args) throws CommandSyntaxException {
        Player player = (Player) sender;
        switch (((String) args[0]).toLowerCase()) {
            case "all" -> {
                all(player);
                return 1;
            }
            case "memory" -> {
                memory.put(player.getUniqueId(), !memory.getOrDefault(player.getUniqueId(), false));
                player.sendMessage("§cMemory Viewer is now " + (memory.get(player.getUniqueId()) ? "enabled" : "disabled"));
                return 1;
            }
            case "actionsave" -> {
                new ActionSaveTest().execute(player, 1, 1, new ArrayList<>());
                return 1;
            }
            default -> throw new IllegalStateException("Unexpected value: " + args[0]);
        }
    }
}

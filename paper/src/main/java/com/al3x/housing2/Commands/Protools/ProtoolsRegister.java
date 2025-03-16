package com.al3x.housing2.Commands.Protools;

import com.al3x.housing2.Commands.AbstractHousingCommand;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.BlockList;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProtoolsRegister extends AbstractHousingCommand implements ProtoolsExecutor {
    public ProtoolsRegister(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);

        commandRegistrar.register(Commands.literal("wand")
                .requires(this::canUseProtoolsIS)
                .executes(this::wand)
                .build(), List.of("/", "/wand")
        );
        commandRegistrar.register(Commands.literal("set")
                .then(Commands.argument("blocks", StringArgumentType.string())
                        .suggests(ProtoolsRegister::getBlocksSuggestions)
                        .executes(this::set)
                )
                .build(), List.of("/set")
        );
        commandRegistrar.register(Commands.literal("replace")
                .then(Commands.argument("blocks1", StringArgumentType.string())
                        .suggests(ProtoolsRegister::getBlocksSuggestions)
                        .then(Commands.argument("blocks2", StringArgumentType.string())
                                .suggests(ProtoolsRegister::getBlocksSuggestions)
                                .executes(this::replace)
                        )
                )
                .build(), List.of("/replace")
        );
        commandRegistrar.register(Commands.literal("sphere")
                .then(Commands.argument("block", StringArgumentType.string())
                        .suggests(ProtoolsRegister::getBlocksSuggestions)
                        .then(Commands.argument("radius", IntegerArgumentType.integer(0, 60))
                                .executes(this::sphere)
                        )
                )
                .build(), List.of("/sphere")
        );
        commandRegistrar.register(Commands.literal("undo")
                .requires(this::canUseProtoolsIS)
                .executes(this::undo)
                .build(), List.of("/undo")
        );
        commandRegistrar.register(Commands.literal("copy")
                .requires(this::canUseProtoolsIS)
                .executes(this::copy)
                .build(), List.of("/copy")
        );
        commandRegistrar.register(Commands.literal("paste")
                .requires(this::canUseProtoolsIS)
                .executes(this::paste)
                .build(), List.of("/paste")
        );
        commandRegistrar.register(Commands.literal("removeselection")
                .requires(this::canUseProtoolsIS)
                .executes(this::removeSelection)
                .build(), List.of("/removeselection", "clearselection", "/clearselection", "desel", "/desel")
        );

        commandRegistrar.register(Commands.literal("protools")
                .requires(this::canUseProtoolsIS)
                .executes(this::protools)
                .build()
        );

        commandRegistrar.register(Commands.literal("pos1")
                .requires(this::canUseProtoolsIS)
                .executes(this::pos1)
                .build()
        );

        commandRegistrar.register(Commands.literal("pos2")
                .requires(this::canUseProtoolsIS)
                .executes(this::pos2)
                .build()
        );

    }

    private boolean canUseProtools(CommandSourceStack commandSourceStack) {
        Player player = (Player) commandSourceStack.getSender();
        return Main.getInstance().getProtoolsManager().canUseProtools(player, false);
    }

    private boolean canUseProtoolsIS(CommandSourceStack commandSourceStack) {
        Player player = (Player) commandSourceStack.getSender();
        return Main.getInstance().getProtoolsManager().canUseProtools(player, true);
    }

    public static CompletableFuture<Suggestions> getBlocksSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        List<String> block = BlockList.completionsForArgument(builder.getRemainingLowerCase());
        block.forEach(builder::suggest);
        return builder.buildFuture();
    }
}

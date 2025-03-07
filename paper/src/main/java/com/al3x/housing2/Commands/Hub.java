package com.al3x.housing2.Commands;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class Hub extends AbstractCommand {
    public Hub(Commands registrar) {
        super(registrar);

        registrar.register(Commands.literal("hub")
                .requires(context -> context.getSender() instanceof Player)
                .executes(context -> command(context, context.getSource().getSender()))
                .build(), List.of("l", "lobby"));
    }

    @Override
    protected int command(CommandContext<CommandSourceStack> context, CommandSender sender, Object... args) throws CommandSyntaxException {
        Player player = (Player) sender;
        World hub = Bukkit.getWorld("world");
        if (hub == null) return 1;
        player.teleport(new Location(hub, -6.5, 68, 5.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return 1;
    }
}

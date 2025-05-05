package com.al3x.housing2.Commands;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class Teleport extends AbstractHousingCommand {
    public Teleport(Commands commandRegistrar, HousesManager housesManager) {
        super(commandRegistrar, housesManager);
        commandRegistrar.register(Commands.literal("teleport")
                .requires(ctx -> hasPermission(ctx, Permissions.COMMAND_TP))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(ctx -> {
                            PlayerSelectorArgumentResolver resolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player player = resolver.resolve(ctx.getSource()).getFirst();
                            if (player != null) {
                                ctx.getSource().getSender().sendMessage("§aTeleporting to " + player.getName());
                                ((Player) ctx.getSource().getSender()).teleport(player.getLocation());
                            } else {
                                ctx.getSource().getSender().sendMessage("§cPlayer not found");
                            }
                            return 1;
                        }))
                .then(Commands.argument("loc", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            String loc = StringArgumentType.getString(ctx, "loc");
                            String[] pos = loc.split(" ");
                            if (pos.length != 3 && pos.length != 5) {
                                ctx.getSource().getSender().sendMessage("§cInvalid location format. Use <x> <y> <z> or <x> <y> <z> <pitch> <yaw>");
                                return 1;
                            }
                            Player player = (Player) ctx.getSource().getSender();
                            Location playerLoc = player.getLocation();
                            String posX = pos[0].startsWith("~") ? (pos[0].substring(1).isEmpty() ? "0" : pos[0].substring(1)) : pos[0];
                            String posY = pos[1].startsWith("~") ? (pos[1].substring(1).isEmpty() ? "0" : pos[1].substring(1)) : pos[1];
                            String posZ = pos[2].startsWith("~") ? (pos[2].substring(1).isEmpty() ? "0" : pos[2].substring(1)) : pos[2];
                            double x = pos[0].startsWith("~") ? playerLoc.getX() + Double.parseDouble(posX) : Double.parseDouble(posX);
                            double y = pos[1].startsWith("~") ? playerLoc.getY() + Double.parseDouble(posY) : Double.parseDouble(posY);
                            double z = pos[2].startsWith("~") ? playerLoc.getZ() + Double.parseDouble(posZ) : Double.parseDouble(posZ);
                            Float yaw = (pos.length == 5) ? Float.parseFloat(pos[3]) : playerLoc.getYaw();
                            Float pitch = (pos.length == 5) ? Float.parseFloat(pos[4]) : playerLoc.getPitch();

                            Location newLoc = new Location(player.getWorld(), x, y, z, yaw, pitch);
                            player.teleport(newLoc);
                            return 1;
                        }))
                .build(), List.of("tp")
        );
    }
}

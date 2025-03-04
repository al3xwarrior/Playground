package com.al3x.housing2.Commands.newcommands;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.DurationString;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;

import static com.al3x.housing2.Utils.Color.colorize;

public interface HousingPunishments {
    default int kick(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String target = context.getArgument("target", String.class);

        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cYou can't kick yourself!");
            return 1;
        }
        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null || !player.getWorld().getPlayers().contains(targetPlayer)) {
            player.sendMessage("§cPlayer not found!");
            return 1;
        }

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        house.kickPlayerFromHouse(targetPlayer);
        player.sendMessage(String.format(colorize("&cKicked %s from the house!"), targetPlayer.getName()));
        return 1;
    }

    default int ban(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String target = context.getArgument("target", String.class);

        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cYou can't ban yourself!");
            return 1;
        }
        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null || !player.getWorld().getPlayers().contains(targetPlayer)) {
            player.sendMessage("§cPlayer not found!");
            return 1;
        }

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());

        //Eventually this should be replaced with a custom argument type
        String duration = context.getArgument("duration", String.class);
        if (duration == null || duration.isEmpty()) {
            house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setBanExpiration(DurationString.convertToExpiryTime(duration));
            player.sendMessage(String.format(colorize("&cBanned %s from the house for %s!"), targetPlayer.getName(), duration));
        } else {
            house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setBanExpiration(DurationString.convertToExpiryTime("99999d"));
            player.sendMessage(String.format(colorize("&cBanned %s from the house for %s!"), targetPlayer.getName(), duration));
        }
        house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setBanned(true);
        house.kickPlayerFromHouse(targetPlayer);

        return 1;
    }

    default int unban(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String target = context.getArgument("target", String.class);

        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cYou can't unban yourself!");
            return 1;
        }
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        if (targetPlayer == null || !player.getWorld().getPlayers().contains(targetPlayer)) {
            player.sendMessage("§cPlayer not found!");
            return 1;
        }

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setBanExpiration(Instant.now());
        house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setBanned(false);
        player.sendMessage(String.format(colorize("&cUnbanned %s from the house!"), targetPlayer.getName()));
        return 1;
    }

    default int kickall(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        for (Player playerToKick : player.getWorld().getPlayers()) {
            if (playerToKick.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            house.kickPlayerFromHouse(playerToKick);
        }
        player.sendMessage(colorize("&cKicked all players from the house!"));
        return 1;
    }

    default int kickallguests(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        for (Player playerToKick : player.getWorld().getPlayers()) {
            if (house.houseData.getPlayerData().get(playerToKick.getUniqueId().toString()).getGroup().equals(house.getDefaultGroup())) {
                house.kickPlayerFromHouse(playerToKick);
            }
        }
        player.sendMessage(colorize("&cKicked all guests from the house!"));
        return 1;
    }

    default int mute(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String target = context.getArgument("target", String.class);

        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cYou can't mute yourself!");
            return 1;
        }
        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null || !player.getWorld().getPlayers().contains(targetPlayer)) {
            player.sendMessage("§cPlayer not found!");
            return 1;
        }

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());

        //Eventually this should be replaced with a custom argument type
        String duration = context.getArgument("duration", String.class);
        if (duration == null || duration.isEmpty()) {
            house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setMuteExpiration(DurationString.convertToExpiryTime(duration));
            player.sendMessage(String.format(colorize("&cMuted %s from the house for %s!"), targetPlayer.getName(), duration));
        } else {
            house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setMuteExpiration(DurationString.convertToExpiryTime("99999d"));
            player.sendMessage(String.format(colorize("&cMuted %s from the house for %s!"), targetPlayer.getName(), duration));
        }
        house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setMuted(true);

        return 1;
    }

    default int unmute(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource().getSender();
        String target = context.getArgument("target", String.class);

        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cYou can't unmute yourself!");
            return 1;
        }
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        if (targetPlayer == null || !player.getWorld().getPlayers().contains(targetPlayer)) {
            player.sendMessage("§cPlayer not found!");
            return 1;
        }

        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setMuteExpiration(Instant.now());
        house.getPlayersData().get(targetPlayer.getUniqueId().toString()).setMuted(false);
        player.sendMessage(String.format(colorize("&cUnmuted %s from the house!"), targetPlayer.getName()));
        return 1;
    }
}

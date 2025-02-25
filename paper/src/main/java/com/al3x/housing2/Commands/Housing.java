package com.al3x.housing2.Commands;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.*;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.al3x.housing2.Network.PlayerNetwork;
import com.al3x.housing2.network.payload.clientbound.ClientboundExport;
import com.al3x.housing2.network.payload.clientbound.ClientboundSyntax;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class Housing implements CommandExecutor {

    private final Main main;
    private final HousesManager housesManager;

    public Housing(HousesManager housesManager, Main main) {
        this.housesManager = housesManager;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be done by players.");
            return true;
        }

        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("create")) {
                if (housesManager.getHouseCount(player) >= 3) {
                    player.sendMessage(colorize("&cYou have the maximum amount of houses!"));
                    return true;
                }

                player.sendMessage(colorize("&eCreating your house..."));
                HousingWorld house = housesManager.createHouse(player, HouseSize.XLARGE);
                player.sendMessage(colorize("&aYour house has been created!"));
                house.sendPlayerToHouse(player);
                return true;
            }

            if (strings[0].equalsIgnoreCase("home")) {
                new MyHousesMenu(main, player, player).open();
                return true;
            }

            if (strings[0].equalsIgnoreCase("name")) {
                if (!housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou don't have a house!"));
                    return true;
                }

                if (strings.length < 2) {
                    player.sendMessage(colorize("&cYou need to supply a name!"));
                    return true;
                }

                StringBuilder fullName = new StringBuilder();
                for (int i = 1; i < strings.length; i++) {
                    fullName.append(strings[i]);
                    if (i + 1 != strings.length) fullName.append(" ");
                }

                HousingWorld house = housesManager.getHouse(player);
                house.setName(fullName.toString());
                player.sendMessage(colorize("&aThe name of your house was set to " + fullName + "&a!"));

                return true;
            }

            if (strings[0].equalsIgnoreCase("goto")) {
                if (housesManager.playerHasHouse(player)) {
                    HousingWorld house = housesManager.getHouse(player);
                    house.sendPlayerToHouse(player);
                    return true;
                }
                player.sendMessage(colorize("&cYou don't have a house!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("visit")) {
                if (strings.length == 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);

                    if (!housesManager.playerHasHouse(target)) {
                        player.sendMessage(colorize("&cThat player doesn't have a house!"));
                        return true;
                    }

                    MyHousesMenu menu = new MyHousesMenu(main, player, target);
                    menu.open();
                    return true;
                }

                player.sendMessage(colorize("&cUsage: /housing visit <player>"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("hub")) {
                World hub = Bukkit.getWorld("world");
                if (hub == null) return false;
                player.teleport(hub.getSpawnLocation());
                return true;
            }

            if (strings[0].equalsIgnoreCase("browse")) {
                new HouseBrowserMenu(player, housesManager).open();
                return true;
            }

            if (strings[0].equalsIgnoreCase("menu")) {
                if (housesManager.getHouse(player.getWorld()).getOwnerUUID() == player.getUniqueId()) {
                    HousingWorld house = housesManager.getHouse(player.getWorld());
                    new HousingMenu(main, player, house).open();
                    return true;
                }
                player.sendMessage(colorize("&cYou are not the owner of this house!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("playerstats")) {
                if (strings.length == 2) {
                    Player target = Bukkit.getPlayer(strings[1]);
                    if (target == null) {
                        player.sendMessage(colorize("&cThere is no player with that username online!"));
                        return true;
                    }

                    HousingWorld house = housesManager.getHouse(player.getWorld());

                    if (!house.hasPermission(player, Permissions.COMMAND_EDITSTATS)) {
                        player.sendMessage(colorize("&cYou do not have permission to view player stats!"));
                        return true;
                    }

                    if (house.getStatManager().getPlayerStats(target) == null || house.getStatManager().getPlayerStats(target).isEmpty()) {
                        player.sendMessage(colorize("&cThat player has no stats!"));
                        return true;
                    }
                    player.sendMessage(colorize("&aStats for " + target.getName() + ":"));
                    house.getStatManager().getPlayerStats(target).forEach((stat) -> {
                        player.sendMessage(colorize("&a" + stat.getStatName() + ": &f" + stat.getValue()));
                    });
                    return true;
                }

                player.sendMessage(colorize("&cUsage: /housing playerstats <player>"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("globalstats")) {
                if (strings.length == 1) {
                    HousingWorld house = housesManager.getHouse(player.getWorld());
                    if (house == null) {
                        player.sendMessage(colorize("&cYou are not in a house!"));
                        return true;
                    }
                    if (!house.hasPermission(player, Permissions.COMMAND_EDITSTATS)) {
                        player.sendMessage(colorize("&cYou do not have permission to view global stats!"));
                        return true;
                    }

                    if (house.getStatManager().getGlobalStats() == null || house.getStatManager().getGlobalStats().isEmpty()) {
                        player.sendMessage(colorize("&cThe current house has no stats"));
                        return true;
                    }
                    player.sendMessage(colorize("&aGlobal stats for " + house.getName() + ":"));
                    house.getStatManager().getGlobalStats().forEach((stat) -> {
                        player.sendMessage(colorize("&a" + stat.getStatName() + ": &f" + stat.getValue()));
                    });
                    return true;
                }

                player.sendMessage(colorize("&cUsage: /housing globalstats"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("random")) {
                HousingWorld house = housesManager.getRandomPublicHouse();
                if (house != null) {
                    house.sendPlayerToHouse(player);
                } else {
                    player.sendMessage(colorize("&cThere are no public houses available!"));
                }
                return true;
            }

            if (strings[0].equalsIgnoreCase("ptsl")) {
                if (PlayerNetwork.getNetwork(player).isUsingMod()) {
                    String actionsSyntax = "";
                    for (HTSLImpl action : Arrays.stream(ActionEnum.values()).map(ActionEnum::getActionInstance).filter(a -> a instanceof HTSLImpl).map(a -> (HTSLImpl) a).toList()) {
                        actionsSyntax += action.syntax() + "\n";
                    }

                    String conditionsSyntax = "";
                    for (CHTSLImpl condition : Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).filter(c -> c instanceof CHTSLImpl).map(c -> (CHTSLImpl) c).toList()) {
                        conditionsSyntax += condition.syntax() + "\n";
                    }

                    conditionsSyntax += "\n\nPermissions:\n";
                    for (Permissions permissions : Permissions.values()) {
                        conditionsSyntax += permissions + ",";
                    }

                    conditionsSyntax += "\n\nDamage Types:\n";
                    for (DamageTypes damageTypes : DamageTypes.values()) {
                        conditionsSyntax += damageTypes + ",";
                    }

                    conditionsSyntax += "\n\nProjectiles:\n";
                    for (Projectile projectile : Projectile.values()) {
                        conditionsSyntax += projectile + ",";
                    }

                    conditionsSyntax += "\n\nParticles:\n";
                    for (Particles particle : Particles.values()) {
                        conditionsSyntax += particle + ",";
                    }

                    conditionsSyntax += "\n\nSounds:\n";
                    for (org.bukkit.Sound sound : org.bukkit.Sound.values()) {
                        conditionsSyntax += sound + ",";
                    }

                    conditionsSyntax += "\n\nAttributes:\n";
                    for (AttributeType attribute : AttributeType.values()) {
                        conditionsSyntax += attribute + ",";
                    }

                    ClientboundSyntax syntax = new ClientboundSyntax(actionsSyntax, conditionsSyntax);
                    PlayerNetwork.getNetwork(player).sendMessage(syntax);
                } else {
                    player.sendMessage(colorize("&cYou must be using the mod to use this command!"));
                }
                return true;
            }

            if (strings[0].equalsIgnoreCase("givecookie") && player.hasPermission("housing2.admin")) {
                CookieManager.givePhysicalCookie(player);
                return true;
            }

            if (strings[0].equalsIgnoreCase("kick")) {
                if (strings.length == 2) {
                    HousingWorld house = housesManager.getHouse(player.getWorld());
                    if (house == null) {
                        player.sendMessage(colorize("&cYou are not in a house!"));
                        return true;
                    }
                    if (!house.hasPermission(player, Permissions.KICK)) {
                        player.sendMessage(colorize("&cYou don't have permission to kick players from this house!"));
                        return true;
                    }
                    if (Bukkit.getPlayer(strings[1]) == null) {
                        player.sendMessage(colorize("&cThat player is not online!"));
                        return true;
                    }
                    boolean online = house.getWorld().getPlayers().contains(Bukkit.getPlayer(strings[1]));
                    if (!online) {
                        player.sendMessage(colorize("&cThat player is not in the same house as you!"));
                        return true;
                    }
                    if (player.getName().equals(strings[1])) {
                        player.sendMessage(colorize("&cYou can't kick yourself from this house!"));
                        return true;
                    }
                    house.kickPlayerFromHouse(Bukkit.getPlayer(strings[1]));
                    player.sendMessage(String.format(colorize("&cKicked %s from the house!"), Bukkit.getPlayer(strings[1]).getName()));
                    return true;
                }
                player.sendMessage(colorize("&cYou need to specify a player!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("ban")) {
                if (strings.length == 2) {
                    HousingWorld house = housesManager.getHouse(player.getWorld());
                    if (house == null) {
                        player.sendMessage(colorize("&cYou are not in a house!"));
                        return true;
                    }
                    if (!house.hasPermission(player, Permissions.BAN)) {
                        player.sendMessage(colorize("&cYou don't have permission to ban players in this house!"));
                        return true;
                    }
                    if (Bukkit.getPlayer(strings[1]) == null) {
                        player.sendMessage(colorize("&cThat player is not online!"));
                        return true;
                    }
                    boolean online = house.getWorld().getPlayers().contains(Bukkit.getPlayer(strings[1]));
                    if (!online) {
                        player.sendMessage(colorize("&cThat player is not in the same house as you!"));
                        return true;
                    }
                    if (player.getName().equals(strings[1])) {
                        player.sendMessage(colorize("&cYou can't ban yourself from this house!"));
                        return true;
                    }
                    house.getPlayersData().get(Bukkit.getPlayer(strings[1]).getUniqueId().toString()).setBanned(true);
                    house.kickPlayerFromHouse(Bukkit.getPlayer(strings[1]));
                    player.sendMessage(String.format(colorize("&cBanned %s from this house!"), Bukkit.getPlayer(strings[1]).getName()));
                    return true;
                }
                player.sendMessage(colorize("&cYou need to specify a player!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("unban")) {
                if (strings.length == 2) {
                    HousingWorld house = housesManager.getHouse(player.getWorld());
                    if (house == null) {
                        player.sendMessage(colorize("&cYou are not in a house!"));
                        return true;
                    }
                    if (!house.hasPermission(player, Permissions.BAN)) {
                        player.sendMessage(colorize("&cYou don't have permission to ban players in this house!"));
                        return true;
                    }
                    if (Bukkit.getPlayer(strings[1]) == null) {
                        player.sendMessage(colorize("&cThat player is not online!"));
                        return true;
                    }
                    house.getPlayersData().get(Bukkit.getPlayer(strings[1]).getUniqueId().toString()).setBanned(false);
                    player.sendMessage(String.format(colorize("&cUnbanned %s from this house!"), Bukkit.getPlayer(strings[1]).getName()));
                    return true;
                }
                player.sendMessage(colorize("&cYou need to specify a player!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("kickall")) {
                HousingWorld house = housesManager.getHouse(player.getWorld());
                if (house == null) {
                    player.sendMessage(colorize("&cYou are not in a house!"));
                    return true;
                }
                if (house.getOwnerUUID() != player.getUniqueId()) {
                    player.sendMessage(colorize("&cYou don't have permission to kick all players from this house!"));
                    return true;
                }
                for (Player playerToKick : player.getWorld().getPlayers()) {
                    if (playerToKick.getUniqueId() == player.getUniqueId()) {
                        continue;
                    }
                    house.kickPlayerFromHouse(playerToKick);
                }
                player.sendMessage(colorize("&cKicked all players from this house!"));
                return true;
            }
        }

        if (strings[0].equalsIgnoreCase("mute")) {
            if (strings.length == 2) {
                HousingWorld house = housesManager.getHouse(player.getWorld());
                if (house == null) {
                    player.sendMessage(colorize("&cYou are not in a house!"));
                    return true;
                }
                if (!house.hasPermission(player, Permissions.MUTE)) {
                    player.sendMessage(colorize("&cYou don't have permission to mute players in this house!"));
                    return true;
                }
                if (Bukkit.getPlayer(strings[1]) == null) {
                    player.sendMessage(colorize("&cThat player is not online!"));
                    return true;
                }
                boolean online = house.getWorld().getPlayers().contains(Bukkit.getPlayer(strings[1]));
                if (!online) {
                    player.sendMessage(colorize("&cThat player is not in the same house as you!"));
                    return true;
                }
                if (player.getName().equals(strings[1])) {
                    player.sendMessage(colorize("&cYou can't mute yourself in this house!"));
                    return true;
                }
                house.getPlayersData().get(Bukkit.getPlayer(strings[1]).getUniqueId().toString()).setMuted(true);
                player.sendMessage(String.format(colorize("&cMuted %s in this house!"), Bukkit.getPlayer(strings[1]).getName()));
                return true;
            }
            player.sendMessage(colorize("&cYou need to specify a player!"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("unmute")) {
            if (strings.length == 2) {
                HousingWorld house = housesManager.getHouse(player.getWorld());
                if (house == null) {
                    player.sendMessage(colorize("&cYou are not in a house!"));
                    return true;
                }
                if (!house.hasPermission(player, Permissions.MUTE)) {
                    player.sendMessage(colorize("&cYou don't have permission to mute players in this house!"));
                    return true;
                }
                if (Bukkit.getPlayer(strings[1]) == null) {
                    player.sendMessage(colorize("&cThat player is not online!"));
                    return true;
                }
                boolean online = house.getWorld().getPlayers().contains(Bukkit.getPlayer(strings[1]));
                if (!online) {
                    player.sendMessage(colorize("&cThat player is not in the same house as you!"));
                    return true;
                }
                if (player.getName().equals(strings[1])) {
                    player.sendMessage(colorize("&cYou can't mute yourself in this house!"));
                    return true;
                }
                house.getPlayersData().get(Bukkit.getPlayer(strings[1]).getUniqueId().toString()).setMuted(false);
                player.sendMessage(String.format(colorize("&cUnmuted %s in this house!"), Bukkit.getPlayer(strings[1]).getName()));
                return true;
            }
            player.sendMessage(colorize("&cYou need to specify a player!"));
            return true;
        }

        player.sendMessage(colorize("&7&m---------------------------------------"));
        player.sendMessage(colorize("&6&lHousing Commands:"));
        player.sendMessage(colorize("&7- &f/housing create &7&o- start the creation process"));
        player.sendMessage(colorize("&7- &f/housing home &7&o- open the my houses menu"));
        player.sendMessage(colorize("&7- &f/housing name <name> &7&o- rename your housing"));
        player.sendMessage(colorize("&7- &f/housing goto &7&o- teleport to your housing"));
        player.sendMessage(colorize("&7- &f/housing visit <player> &7&o- visit another users housing"));
        player.sendMessage(colorize("&7- &f/housing hub &7&o- go back to the lobby"));
        player.sendMessage(colorize("&7- &f/housing menu &7&o- view the housing browser"));
        player.sendMessage(colorize("&7- &f/housing playerstats <player> &7&o- view a players stats"));
        player.sendMessage(colorize("&7- &f/housing globalstats &7&o- view the global stats"));
        player.sendMessage(colorize("&7- &f/housing kick <player> &7&o- kick player from your house"));
        player.sendMessage(colorize("&7- &f/housing ban <player> &7&o- bans player from your house"));
        player.sendMessage(colorize("&7- &f/housing unban <player> &7&o- unbans player from your house"));
        player.sendMessage(colorize("&7- &f/housing mute <player> &7&o- mutes player in your house"));
        player.sendMessage(colorize("&7- &f/housing unmute <player> &7&o- unmutes player in your house"));
        player.sendMessage(colorize("&7- &f/housing kickall &7&o- kick all players from your house"));
        player.sendMessage(colorize("&7&m---------------------------------------"));

        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Override
        public java.util.List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
            if (args.length == 1) {
                return java.util.List.of("create", "delete", "home", "name", "goto", "visit", "hub", "browse", "menu", "playerstats", "globalstats", "ptsl", "kick", "ban", "unban", "kickall", "mute", "unmute").stream().filter(i -> i.startsWith(args[0])).toList();
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("visit")) {
                    return HouseBrowserMenu.getSortedHouses().stream()
                            .map((h) -> Bukkit.getOfflinePlayer(UUID.fromString(h.getOwnerID())).getName())
                            .filter(i -> i.startsWith(args[1])).limit(20).toList();
                }

                if (args[0].equalsIgnoreCase("playerstats") || args[0].equalsIgnoreCase("unban")) {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList().stream().filter(i -> i.startsWith(args[1])).toList();
                }

                if (List.of("kick", "ban", "mute", "unmute").contains(args[0].toLowerCase())) {
                    return Bukkit.getPlayer(commandSender.getName()).getWorld().getPlayers().stream().map(Player::getName).toList();
                }
            }

            return java.util.List.of();
        }
    }
}

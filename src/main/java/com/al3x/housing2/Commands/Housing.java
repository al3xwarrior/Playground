package com.al3x.housing2.Commands;

import com.al3x.housing2.Action.Actions.ChatAction;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.CookieManager;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;

public class Housing implements CommandExecutor {

    private final Main main;
    private HousesManager housesManager;

    public Housing(HousesManager housesManager, Main main) {
        this.housesManager = housesManager;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be done by players.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("create")) {
                if (housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou already have a house!"));
                    return true;
                }

                player.sendMessage(colorize("&eCreating your house..."));
                HousingWorld house = housesManager.createHouse(player, HouseSize.LARGE);
                player.sendMessage(colorize("&aYour house has been created!"));
                house.sendPlayerToHouse(player);
                return true;
            }

            if (strings[0].equalsIgnoreCase("delete")) {
                if (!housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou don't have a house!"));
                    return true;
                }

                player.sendMessage(colorize("&cDeleting..."));
                housesManager.deleteHouse(player);
                player.sendMessage(colorize("&cHouse Deleted!"));
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

                if (strings.length < 1) {
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
                    if (target == null) {
                        player.sendMessage(colorize("&cThere is no player with that username online!"));
                        return true;
                    }

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

                    if (house.hasPermission(player, Permissions.COMMAND_EDITSTATS) && !player.hasPermission("housing.playerstats")) {
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
                    if (house.hasPermission(player, Permissions.COMMAND_EDITSTATS) && !player.hasPermission("housing.globalstats")) {
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

            if (strings[0].equalsIgnoreCase("save") && player.hasPermission("housing.save")) {
                if (housesManager.getHouse(player.getWorld()) == null) {
                    player.sendMessage(colorize("&cYou are not in a house!"));
                    return true;
                }
                housesManager.saveHouseAndUnload(housesManager.getHouse(player.getWorld()));
                player.sendMessage(colorize("&aHouses saved and unloaded!"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("editor") && player.hasPermission("housing.editor")) {

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

            if (strings[0].equalsIgnoreCase("givecookie") && player.hasPermission("housing2.admin")) {
                CookieManager.givePhysicalCookie(player);
                return true;
            }
        }

        player.sendMessage(colorize("&7&m---------------------------------------"));
        player.sendMessage(colorize("&6&lHousing Commands:"));
        player.sendMessage(colorize("&7- &f/housing create &7&o- start the creation process"));
        player.sendMessage(colorize("&7- &f/housing delete &7&o- delete your housing"));
        player.sendMessage(colorize("&7- &f/housing home &7&o- open the my houses menu"));
        player.sendMessage(colorize("&7- &f/housing name <name> &7&o- rename your housing"));
        player.sendMessage(colorize("&7- &f/housing goto &7&o- teleport to your housing"));
        player.sendMessage(colorize("&7- &f/housing visit <player> &7&o- visit another users housing"));
        player.sendMessage(colorize("&7- &f/housing hub &7&o- go back to the lobby"));
        player.sendMessage(colorize("&7- &f/housing menu &7&o- view the housing browser"));
        player.sendMessage(colorize("&7- &f/housing playerstats <player> &7&o- view a players stats"));
        player.sendMessage(colorize("&7- &f/housing globalstats &7&o- view the global stats"));
        player.sendMessage(colorize("&7&m---------------------------------------"));

        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Override
        public java.util.List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
            if (args.length == 1) {
                return java.util.List.of("create", "delete", "home", "name", "goto", "visit", "hub", "browse", "menu", "playerstats", "globalstats", "save").stream().filter(i -> i.startsWith(args[0])).toList();
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("visit")) {
                    return HouseBrowserMenu.getSortedHouses().stream()
                            .map((h) -> Bukkit.getOfflinePlayer(UUID.fromString(h.getOwnerID())).getName())
                            .filter(i -> i.startsWith(args[1])).limit(20).toList();
                }

                if (args[0].equalsIgnoreCase("playerstats")) {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList().stream().filter(i -> i.startsWith(args[1])).toList();
                }
            }

            return java.util.List.of();
        }
    }
}

package com.al3x.housing2.Menus.HousingMenu.PlayerListing;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Data.PlayerData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Enums.permissions.Permissions.*;
import static com.al3x.housing2.Utils.ItemBuilder.ActionType.*;

public class EditPlayerMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private OfflinePlayer targetPlayer;

    public EditPlayerMenu(Main main, Player player, HousingWorld house, OfflinePlayer targetPlayer) {
        super(player, colorize("&7Actions for " + targetPlayer.getName()), 4*9);
        this.main = main;
        this.player = player;
        this.house = house;
        this.targetPlayer = targetPlayer;
        setupItems();
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        int i = 0;

        boolean online = player.getWorld().getPlayers().contains(targetPlayer.getPlayer());

        PlayerData targetPlayerData = house.getPlayersData().get(targetPlayer.getUniqueId().toString());
        PlayerData playerData = house.getPlayersData().get(player.getUniqueId().toString());

        addItem(31, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), () -> {
            new PlayerListingMenu(main, player, house).open();
        });

        if (playerData.getGroupInstance(house).getPriority() <= targetPlayerData.getGroupInstance(house).getPriority() && player != targetPlayer) return; // less group priority, cant edit
        boolean isSamePlayer = player.getUniqueId().equals(targetPlayer.getUniqueId());

        if (online && house.hasPermission(player, Permissions.KICK) && !isSamePlayer) {
            addItem(slots[i], new ItemBuilder().material(Material.IRON_BARS).name(colorize("&cKick Player")).lClick(ItemBuilder.ActionType.KICK).build(), (e) -> {
                if (!e.isLeftClick()) return;
                if (targetPlayer.getPlayer().equals(player)) {
                    player.sendMessage(colorize("&cYou can't kick yourself silly!"));
                    return;
                }
                if (online) house.kickPlayerFromHouse(targetPlayer.getPlayer());
            });
            i++;
        }

        if (house.hasPermission(player, MUTE) && !isSamePlayer) {
            addItem(slots[i], new ItemBuilder().material(Material.CHAIN).name(colorize((targetPlayerData.isMuted()) ? "&aUnmute Player" : "&cMute Player")).lClick(TOGGLE_YELLOW).build(), (e) -> {
                if (!e.isLeftClick()) return;
                if (targetPlayer.getPlayer().equals(player)) {
                    player.sendMessage(colorize("&cYou can't mute yourself silly!"));
                    return;
                }
                targetPlayerData.setMuted(!targetPlayerData.isMuted());
                open();
            });
            i++;
        }

        if (house.hasPermission(player, BAN) && !isSamePlayer) {
            addItem(slots[i], new ItemBuilder().material(Material.BARRIER).name(colorize((targetPlayerData.isBanned()) ? "&aUnban Player" : "&cBan Player")).lClick(TOGGLE_YELLOW).build(), (e) -> {
                if (!e.isLeftClick()) return;
                if (targetPlayer.getPlayer().equals(player)) {
                    player.sendMessage(colorize("&cYou can't ban yourself silly!"));
                    return;
                }
                targetPlayerData.setBanned(!targetPlayerData.isBanned());
                if (online) house.kickPlayerFromHouse(targetPlayer.getPlayer());
                open();
            });
            i++;
        }

        if (online && house.hasPermission(player, GAMEMODE)) {
            addItem(slots[i], new ItemBuilder().material(Material.DAYLIGHT_DETECTOR).name(colorize("&eCycle Gamemode")).info("Current Gamemode", "&7" + targetPlayer.getPlayer().getGameMode()).lClick(CYCLE_FORWARD).rClick(CYCLE_BACKWARD).build(), (e) -> {
                int gm = targetPlayer.getPlayer().getGameMode().ordinal();

                if (e.isLeftClick()) gm += 1;
                else gm -= 1;

                if (gm < 0) gm += 4;

                targetPlayer.getPlayer().setGameMode(GameMode.values()[gm % 4]);

                open();
            });
            i++;
        }

        if ((house.hasPermission(player, CHANGE_PLAYER_GROUP) || house.hasPermission(player, EDIT_PERMISSIONS_AND_GROUP))) {
            addItem(slots[i], new ItemBuilder().material(Material.PLAYER_HEAD).name(colorize("&eCycle Group")).info("Current Group", "&7" + targetPlayerData.getGroupInstance(house).getDisplayName()).lClick(CYCLE_FORWARD).rClick(CYCLE_BACKWARD).build(), (e) -> {
                int groupIndex = house.getGroups().indexOf(targetPlayerData.getGroupInstance(house));

                int direction;
                if (e.isLeftClick()) direction = 1;
                else direction = -1;

                groupIndex += direction;

                for (int j = 0; j < house.getGroups().size(); j ++) { // check groups until one is found with lower priority than the player, or do nothing
                    if (groupIndex < 0) groupIndex += house.getGroups().size();
                    if (house.getGroups().get(groupIndex % house.getGroups().size()).getPriority() < playerData.getGroupInstance(house).getPriority() || player.getUniqueId().equals(targetPlayer.getUniqueId())) {
                        targetPlayerData.setGroup(house.getGroups().get(groupIndex % house.getGroups().size()).getName());
                        break;
                    }
                    groupIndex += direction;
                }

                open();
            });
            i++;
        }

        if (house.hasPermission(player, CHANGE_PLAYER_TEAM) || house.hasPermission(player, EDIT_TEAMS)) {
            addItem(slots[i], new ItemBuilder().material(Material.OAK_SIGN).name(colorize("&eCycle Team")).info("Current Team", "&7" + targetPlayerData.getTeamInstance(house).getDisplayName()).lClick(CYCLE_FORWARD).rClick(CYCLE_BACKWARD).build(), (e) -> {

                int teamIndex = house.getTeams().indexOf(targetPlayerData.getTeamInstance(house)); // no team returns -1, so this actually works fine with no team :P

                if (e.isLeftClick()) teamIndex += 1;
                else teamIndex -= 1;

                if (teamIndex < 0) teamIndex += house.getTeams().size() + 1;

                if (house.getTeams().isEmpty() || teamIndex == house.getTeams().size()) targetPlayerData.setTeam(null);
                else targetPlayerData.setTeam(house.getTeams().get(teamIndex % house.getTeams().size()).getName());
                open();
            });
            i++;
        }

        if (!house.hasPermission(player, RESET_PLAYER_DATA) || targetPlayer.getPlayer() == player) return;
        addItem(35, new ItemBuilder().material(Material.TNT).name(colorize("&aReset Player Data")).description("Clears ALL player data of this player, including stats, inventory, group, and team.").mClick(RESET).build(), () -> {
            house.getPlayersData().remove(targetPlayer.getUniqueId().toString());
            new PlayerListingMenu(main, player, house).open();
        });

    }

}

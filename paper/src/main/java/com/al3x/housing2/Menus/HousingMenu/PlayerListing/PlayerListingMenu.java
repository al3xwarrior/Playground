package com.al3x.housing2.Menus.HousingMenu.PlayerListing;

import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.*;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Utils.SkinCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Enums.permissions.Permissions.*;
import static com.al3x.housing2.Utils.Color.colorize;

public class PlayerListingMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;

    private String search;
    private int currentPage = 1;

    public PlayerListingMenu(Main main, Player player, HousingWorld house) {
        super(player, colorize("&7Player Listing"), 6*9);
        this.main = main;
        this.player = player;
        this.house = house;
        setupItems();
    }


    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        HashMap<String, PlayerData> playersData = new HashMap<>(house.getPlayersData());
        if (search != null && !search.isBlank()) {
            for (Map.Entry<String, PlayerData> entry : house.getPlayersData().entrySet()) {
                PlayerData data = entry.getValue();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
                if (offlinePlayer.getName() == null) {
                    playersData.remove(entry);
                    continue;
                }
                if (!offlinePlayer.getName().toLowerCase().contains(search.toLowerCase())) {
                    playersData.remove(entry);
                }
            }
        }

        PaginationList<Map.Entry<String, PlayerData>> paginationList = new PaginationList<>(house.getPlayersData().entrySet(), slots.length);


        List<Map.Entry<String, PlayerData>> players = paginationList.getPage(currentPage);

        if (players != null) {

            PlayerData playerData = house.getPlayersData().get(player.getUniqueId().toString());
            for (int i = 0; i < players.size(); i++) {
                OfflinePlayer listedPlayer = Bukkit.getOfflinePlayer(UUID.fromString(players.get(i).getKey()));
                PlayerData listedPlayerData = players.get(i).getValue();
                boolean online = player.getWorld().getPlayers().contains(listedPlayer.getPlayer());
                ItemBuilder item = ItemBuilder.create(Material.PLAYER_HEAD);
                HashMap<String, String> skullData = SkinCache.getSkins();
                if (skullData == null) return;
                item.skullTexture(skullData.get(listedPlayer.getUniqueId().toString()));
                item.name(colorize("&f" + listedPlayer.getName() + ((!online || player.canSee(listedPlayer.getPlayer())) ? "" : " &7(hidden)")));
                boolean higherPriority = playerData.getGroupInstance(house).getPriority() <= listedPlayerData.getGroupInstance(house).getPriority() && !player.getUniqueId().equals(listedPlayer.getUniqueId());
                item.info("Online", (online) ? "&aYes" : "&cNo");
                if (online)
                    item.info("Visible to you", player.canSee(listedPlayer.getPlayer()) ? "&aYes" : "&cNo");
                if (house.hasPermission(player, MUTE))
                    item.info("Muted", (listedPlayerData.getMuted()) ? "&aYes" : "&cNo");
                if (house.hasPermission(player, BAN))
                    item.info("Banned", (listedPlayerData.getBanned()) ? "&aYes" : "&cNo");
                if (house.hasPermission(player, GAMEMODE) && online)
                    item.info("Gamemode", listedPlayer.getPlayer().getGameMode().toString());
                if (house.hasPermission(player, CHANGE_PLAYER_GROUP) || house.hasPermission(player, EDIT_PERMISSIONS_AND_GROUP))
                    item.info("Group", listedPlayerData.getGroupInstance(house).getDisplayName());
                if (house.hasPermission(player, CHANGE_PLAYER_TEAM) || house.hasPermission(player, EDIT_TEAMS))
                    item.info("Team", listedPlayerData.getTeamInstance(house).getName());

                item.lClick(ItemBuilder.ActionType.EDIT_YELLOW);

                if (online) item.rClick(ItemBuilder.ActionType.TOGGLE_VISIBILITY);

                addItem(slots[i], item.build(), (e) -> {
                    if (e.getClick().isLeftClick()) {
                        if (higherPriority) {
                            player.sendMessage(colorize("&cYou can't edit this player, they have a higher priority than you!"));
                            return;
                        }
                        new EditPlayerMenu(main, player, house, listedPlayer).open();
                    } else if (e.getClick().isRightClick() && online) {
                        if (player.canSee(listedPlayer.getPlayer())) {
                            player.hidePlayer(main, listedPlayer.getPlayer());
                        } else {
                            player.showPlayer(main, listedPlayer.getPlayer());
                        }
                        open();
                    }
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BEDROCK).name(colorize("&cNo Items!")).build(), () -> {
            });
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(Material.ARROW).name(colorize("&7Previous Page")).build(), () -> {
                currentPage--;
                open();
            });
        }

        if (currentPage < paginationList.getPageCount()) {
            addItem(53, new ItemBuilder().material(Material.ARROW).name(colorize("&7Next Page")).build(), () -> {
                currentPage++;
                open();
            });
        }

        addItem(48, new ItemBuilder().material(Material.NAME_TAG).name(colorize("&aSearch")).info("Current Search", (search == null || search.isBlank()) ? "&7None" : "&a" + search).lClick(ItemBuilder.ActionType.EDIT_YELLOW).rClick(ItemBuilder.ActionType.CLEAR_SEARCH).build(), (e) -> {
            if (e.isLeftClick()) {
                openChat(main, message -> {
                    search = message;
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), this::open, 1L);
                });
            } else if (e.isRightClick()) {
                search = "";
                open();
            }
        });

        addItem(49, new ItemBuilder().material(Material.ARROW).name(colorize("&aGo Back")).build(), () -> {
            new HousingMenu(main, player, house).open();
        });
    }
}

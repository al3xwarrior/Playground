package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.scoreboard.HousingScoreboard;
import com.al3x.housing2.Utils.tablist.HousingTabList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.HandlePlaceholders.parsePlaceholders;

public class JoinLeaveHouse implements Listener {

    private Main main;
    private HousesManager housesManager;

    public JoinLeaveHouse(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    // Actions that modify the player's "profile" need to be reset.
    public static void resetPlayer(Player player) {
        player.setMaximumAir(300); // 300 is the default
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setMaximumNoDamageTicks(20); // 10 i think?
        player.getInventory().clear();
    }

    private void joinHouse(Player player) {
        //Set the scoreboard
        HousingScoreboard scoreboard = new HousingScoreboard();
        scoreboard.setScoreboard(player);

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) { return; }

        PlayerData data = house.loadOrCreatePlayerData(player);

        loadPermissions(player, house, data);

        //Set the tablist
        HousingTabList.setTabList(player, house);

        house.setGuests();
        player.teleport(house.getSpawn());

        resetPlayer(player);

        // If the person joining is the owner
        if (house.getOwnerUUID().equals(player.getUniqueId())) {
            player.setGameMode(GameMode.CREATIVE);
        }
        // Normal player joins
        else {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void loadPermissions(Player player, HousingWorld house, PlayerData data) {
        if (house == null) return;

        if (house.hasPermission(player, Permissions.FLY)) {
            player.setAllowFlight(true);
        }

        Gamemodes value = Gamemodes.valueOf((String) data.getGroupInstance(house).getPermissions().get(Permissions.GAMEMODE));
        if (value != null) {
            player.setGameMode(value.getGameMode());
        }
    }

    private void leaveHouse(Player player, HousingWorld from) {
        if (from == null) return;
        from.setGuests();
        from.broadcast(colorize(player.getDisplayName() + " &eleft the world."));
    }

    @EventHandler
    public void enterWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        World from = e.getFrom();
        World to = player.getWorld();

        // They are not leaving the hub so that means they are leaving another house
        if (!from.getName().equals("world")) {
            leaveHouse(player, housesManager.getHouse(UUID.fromString(from.getName())));
        }

        // They are entering a house, not the hub
        if (!to.getName().equals("world") && from.getName().equals("world")) {
            joinHouse(player);
        } else {
            resetPlayer(player);
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String worldName = player.getWorld().getName();

        HousingWorld house = null;
        try {
            UUID worldUUID = UUID.fromString(worldName);
            house = housesManager.getHouse(worldUUID);
        } catch (IllegalArgumentException ignored) {}

        // If what they are leaving is indeed a house, then we trigger the leave house method
        if (house != null) {
            leaveHouse(player, house);
        }

        e.setQuitMessage(colorize( "&7&o" + player.getName() + " left the server."));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(colorize("&7&o" + player.getName() + " joined the server."));
        resetPlayer(player);
    }

}

package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.al3x.housing2.Utils.tablist.HousingTabList;
import com.google.gson.internal.LinkedTreeMap;
import com.mongodb.internal.logging.LogMessage;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.Adventure;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.IOException;
import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class JoinLeaveHouse implements Listener {

    private HousesManager housesManager;

    public JoinLeaveHouse(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    // Actions that modify the player's "profile" need to be reset.
    public static void resetPlayer(Player player) {
        player.setMaximumAir(300); // 300 is the default
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setMaximumNoDamageTicks(20); // 10 i think?
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);

        player.sendActionBar("");
        player.sendTitle("", "");

        // Attributes (https://minecraft.wiki/w/Attribute) I hope the website is right!
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        //error with the one below
//        player.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(32); // this might not apply to the player but just incase
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_LUCK).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).setBaseValue(4);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_STEP_HEIGHT).setBaseValue(0.6);
        player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.42);
        player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).setBaseValue(4.5);
        player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).setBaseValue(3);
        player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.08);
        player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(3);
        player.getAttribute(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_BURNING_TIME).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE).setBaseValue(0);
        player.getAttribute(Attribute.PLAYER_MINING_EFFICIENCY).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_EFFICIENCY).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_OXYGEN_BONUS).setBaseValue(0);
        player.getAttribute(Attribute.PLAYER_SNEAKING_SPEED).setBaseValue(0.3);
        player.getAttribute(Attribute.PLAYER_SUBMERGED_MINING_SPEED).setBaseValue(0.2);
        player.getAttribute(Attribute.PLAYER_SWEEPING_DAMAGE_RATIO).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY).setBaseValue(0);
    }

    private void joinHouse(Player player) {
        //Set the scoreboard
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            return;
        }

        if (CitizensAPI.getNPCRegistry().isNPC(player)) {
            NPC cNPC = CitizensAPI.getNPCRegistry().getNPC(player);
            HousingNPC npc = house.getNPC(cNPC.getId());
            if (npc != null && npc.isCanBePlayer()) {

            }
            return;
        }

        house.getScoreboardInstance().addPlayer(player);

        PlayerData data = house.loadOrCreatePlayerData(player);

        loadPermissions(player, house, data);

        //Set the tablist
        HousingTabList.setTabList(player, house);

        house.setGuests();
        player.teleport(house.getSpawn());

        resetPlayer(player);
        try {
            if (data.getInventory() != null) {
                player.getInventory().setContents(Serialization.itemStacksFromBase64(data.getInventory()).toArray(new ItemStack[0]));
            }
            if (data.getEnderchest() != null) {
                player.getEnderChest().setContents(Serialization.itemStacksFromBase64(data.getEnderchest()).toArray(new ItemStack[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If the person joining is the owner
        if (house.getOwnerUUID().equals(player.getUniqueId())) {
            player.setGameMode(GameMode.CREATIVE);
        }
        // Normal player joins
        else {
            player.setGameMode(((Gamemodes) house.getPermission(player, Permissions.GAMEMODE)).getGameMode());
        }

        //give player permission "housing.world.<worlduuid>"
        player.addAttachment(Main.getInstance(), "housing.world." + house.getHouseUUID(), true);

        // Cookies
        // CookieManager.givePhysicalCookie(player);
    }

    private void loadPermissions(Player player, HousingWorld house, PlayerData data) {
        if (house == null) return;

        if (house.hasPermission(player, Permissions.FLY)) {
            player.setAllowFlight(true);
        }

        Object gamemodeObj = data.getGroupInstance(house).getPermissions().get(Permissions.GAMEMODE);
        if (gamemodeObj instanceof String gamemode) {
            Gamemodes value = Gamemodes.valueOf((gamemode));
            player.setGameMode(value.getGameMode());
        } else if (gamemodeObj instanceof LinkedTreeMap<?, ?> gamemodeMap) {
            Gamemodes value = Gamemodes.valueOf((String) gamemodeMap.get("name"));
            player.setGameMode(value.getGameMode());
        }
    }

    private void leaveHouse(Player player, HousingWorld from) {
        if (from == null) return;
        player.displayName(StringUtilsKt.housingStringFormatter(player.getName()));
        from.getScoreboardInstance().removePlayer(player);
        from.setGuests();
        from.broadcast(colorize(player.getDisplayName() + " &eleft the world."));

        player.addAttachment(Main.getInstance(), "housing.world." + from.getHouseUUID(), false);

        PlayerData data = from.loadOrCreatePlayerData(player);
        data.setInventory(Serialization.itemStacksToBase64(new ArrayList<>(Arrays.stream(player.getInventory().getContents()).toList())));
        data.setEnderchest(Serialization.itemStacksToBase64(new ArrayList<>(Arrays.stream(player.getEnderChest().getContents()).toList())));

        HousingTabList.lobbyTabList(player);
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
        } catch (IllegalArgumentException ignored) {
        }

        // If what they are leaving is indeed a house, then we trigger the leave house method
        if (house != null) {
            leaveHouse(player, house);
        }

        e.setQuitMessage(colorize("&7&o" + player.getName() + " left the server."));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(colorize("&7&o" + player.getName() + " joined the server."));
        if (player.getWorld().getName().equals("world")) {
            resetPlayer(player);
        } else {
            joinHouse(player);
        }
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

}

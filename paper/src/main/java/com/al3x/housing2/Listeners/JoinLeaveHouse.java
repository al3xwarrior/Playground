package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.AttributeType;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.*;
import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.LuckpermsHandler;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.al3x.housing2.Utils.VoiceChat;
import com.al3x.housing2.Utils.tablist.HousingTabList;
import com.google.gson.internal.LinkedTreeMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class JoinLeaveHouse implements Listener {
    private HousesManager housesManager;
    private ResourcePackManager resourcePackManager;
    HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

    public JoinLeaveHouse(HousesManager housesManager, ResourcePackManager resourcePackManager) {
        this.housesManager = housesManager;
        this.resourcePackManager = resourcePackManager;
    }

    // Actions that modify the player's "profile" need to be reset.
    public static void resetPlayer(Player player) {
        player.setMaximumAir(300); // 300 is the default
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setMaximumNoDamageTicks(20); // 10 i think?
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.getActivePotionEffects().clear();

        player.playerListName(Component.text(player.getName()));

        player.sendActionBar(Component.empty());
        player.sendTitlePart(TitlePart.TITLE, Component.empty());
        player.sendTitlePart(TitlePart.SUBTITLE, Component.empty());
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(0), Duration.ofSeconds(0)));

        player.activeBossBars().forEach(bossBar -> bossBar.removeViewer(player));

        player.clearActivePotionEffects();

        // Clear inventory and enderchest
        player.getInventory().clear();
        player.getEnderChest().clear();

        // Reset attributes
        for (AttributeType attribute : AttributeType.values()) {
            AttributeInstance attributeInstance = player.getAttribute(attribute.getAttribute());
            if (attributeInstance == null) continue;

            attributeInstance.setBaseValue(attributeInstance.getDefaultValue());
        }
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.2f);

        Main.getInstance().getProtoolsManager().clearSelection(player);
    }

    private void joinHouse(Player player) {
        //Set the scoreboard
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) {
            return;
        }

        if (CitizensAPI.getNPCRegistry().isNPC(player)) {
            NPC cNPC = CitizensAPI.getNPCRegistry().getNPC(player);
            HousingNPC npc = house.getNPCByCitizensID(cNPC.getId());
            if (npc != null && npc.isCanBeDamaged()) {

            }
            return;
        }

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
            if (house.getPermission(player, Permissions.GAMEMODE) instanceof LinkedTreeMap<?,?> gamemodeMap) {
                Gamemodes value = Gamemodes.valueOf((String) gamemodeMap.get("name"));
                player.setGameMode(value.getGameMode());
            } else {
                player.setGameMode(((Gamemodes) house.getPermission(player, Permissions.GAMEMODE)).getGameMode());
            }
        }

        player.updateCommands();

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            LuckpermsHandler.addPermission(player, "housing.world." + house.getHouseUUID());
        } else {
            PermissionAttachment attachment = player.addAttachment(Main.getInstance(), "housing.world." + house.getHouseUUID(), true);
            perms.put(player.getUniqueId(), attachment);
        }

        if (house.getJoinLeaveMessages()) house.broadcast(colorize(player.getDisplayName() + " &eentered the world."));

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
        if (from.isAdminMode(player)) {
            from.removeFromAdminMode(player);
        }
        player.displayName(StringUtilsKt.housingStringFormatter(player.getName()));
        player.playerListName(Component.text(player.getName()));
        from.setGuests();
        if (from.getJoinLeaveMessages()) from.broadcast(colorize(player.getDisplayName() + " &eleft the world."));

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            LuckpermsHandler.removePermission(player, "housing.world." + from.getHouseUUID());
        } else {
            if (perms.containsKey(player.getUniqueId())) player.removeAttachment(perms.remove(player.getUniqueId()));
        }
        PlayerData data = from.loadOrCreatePlayerData(player);
        data.setInventory(Serialization.itemStacksToBase64(new ArrayList<>(Arrays.stream(player.getInventory().getContents()).toList())));
        data.setEnderchest(Serialization.itemStacksToBase64(new ArrayList<>(Arrays.stream(player.getEnderChest().getContents()).toList())));

        HousingTabList.lobbyTabList(player);
        resourcePackManager.removeResourcePack(player, from);
    }

    @EventHandler
    public void enterWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        World from = e.getFrom();
        World to = player.getWorld();

        if (VoiceChat.isPlayerConnected(player)) {
            VoiceChat.setPlayerGroup(player, null);
        }

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
        player.teleport(new Location(Bukkit.getWorld("world"), -6.5, 68, 5.5));
        HousingTabList.lobbyTabList(player);
    }

}

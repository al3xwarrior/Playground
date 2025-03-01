package com.al3x.housing2.Utils;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class VoiceChat implements VoicechatPlugin {
    private static VoicechatServerApi serverApi;
    private static VoicechatApi api;
    private Main main;

    @Override
    public String getPluginId() {
        return "playground";
    }

    @Override
    public void initialize(VoicechatApi api) {
        VoiceChat.serverApi = (VoicechatServerApi) api;
    }

    public void startup(Main main) {
        this.main = main;
    }

    // Events
    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(JoinGroupEvent.class, this::groupJoin);
        registration.registerEvent(CreateGroupEvent.class, this::groupCreate);
    }

    private void groupJoin(JoinGroupEvent event) {
        Player player = (Player) event.getConnection().getPlayer().getPlayer();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) {
             event.cancel(); // disallow joining groups from the lobby
            return;
        }
        // Disallow joining groups from other housings
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!isPlayerConnected(onlinePlayer)) continue;
            if (!serverApi.getConnectionOf(onlinePlayer.getUniqueId()).getGroup().equals(event.getGroup())) continue; // Online player is in target group
            if (!onlinePlayer.getWorld().equals(player.getWorld())) {
                event.cancel();
                return;
            }
            break;
        }

        sendEventExecution(main.getHousesManager(), EventType.PLAYER_JOIN_VOICE_GROUP, (Player) event.getConnection().getPlayer().getPlayer(), event);
    }
    private void groupCreate(CreateGroupEvent event) {
        Player player = (Player) event.getConnection().getPlayer().getPlayer();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) {
            event.cancel(); // disallow creating groups from the lobby
            return;
        }
        sendEventExecution(main.getHousesManager(), EventType.PLAYER_CREATE_VOICE_GROUP, (Player) event.getConnection().getPlayer().getPlayer(), event);
    }

    // Utils
    public static boolean isPlayerConnected(Player player) {
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if (connection == null) return false;
        return connection.isInstalled();
    }

    public static String getPlayerGroup(Player player) {
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if (connection == null) return null;
        Group group = connection.getGroup();
        if (group == null) return null;
        return group.getName();
    }

    public static void setPlayerGroup(Player player, String name) {
        VoicechatConnection connection = serverApi.getConnectionOf(player.getUniqueId());
        if (!isPlayerConnected(player)) return;
        Collection<Group> groups = serverApi.getGroups();
        for (Group group : groups) {
            if (group.getName().equals(name)) {
                connection.setGroup(group);
                return;
            }
        }
        connection.setGroup(null);
    }
}

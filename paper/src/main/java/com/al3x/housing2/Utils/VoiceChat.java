package com.al3x.housing2.Utils;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class VoiceChat implements VoicechatPlugin {
    private static VoicechatServerApi serverApi;
    private static VoicechatApi api;
    private static Main main;
    private static Group transferGroup;
    private static Map<Player, ArrayList<Player>> audibility = new HashMap<>(); // terrible name but I can't think of anything better

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
        registration.registerEvent(EntitySoundPacketEvent.class, this::sendVoicePacket);
    }

    private void sendVoicePacket(EntitySoundPacketEvent event) {
        if (audibility.containsKey(event.getReceiverConnection().getPlayer().getPlayer())) {
            Player sender = (Player) event.getSenderConnection().getPlayer().getPlayer();
            if (audibility.get(event.getReceiverConnection().getPlayer().getPlayer()).contains(sender)) event.cancel();
        }
    }
    private void groupJoin(JoinGroupEvent event) {
        if (transferGroup != null) if (event.getGroup().equals(transferGroup)) return;
        Player player = (Player) event.getConnection().getPlayer().getPlayer();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) {
             event.cancel(); // disallow joining groups from the lobby
            return;
        }
        // Disallow joining groups from other housings
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!isPlayerConnected(onlinePlayer)) continue;
            if (!serverApi.getConnectionOf(onlinePlayer.getUniqueId()).isInGroup()) continue;
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
        if (transferGroup != null) if (event.getGroup().equals(transferGroup)) return;
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
        if (name == null) {
            connection.setGroup(null);
            return;
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!isPlayerConnected(onlinePlayer)) continue;
            if (!serverApi.getConnectionOf(onlinePlayer.getUniqueId()).isInGroup()) continue;
            Group group = serverApi.getConnectionOf(onlinePlayer.getUniqueId()).getGroup();
            if (!group.isHidden()) continue;
            if (group.getName().equals(name) && onlinePlayer.getWorld().equals(player.getWorld())) {
                connection.setGroup(group);
                return;
            }
        }
        Group group = serverApi.groupBuilder()
                .setName(name)
                .setHidden(true)
                .build();
        connection.setGroup(group);
    }

    public static Group getGroup(World world, String name) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!isPlayerConnected(onlinePlayer)) continue;
            if (!serverApi.getConnectionOf(onlinePlayer.getUniqueId()).isInGroup()) continue;
            Group group = serverApi.getConnectionOf(onlinePlayer.getUniqueId()).getGroup();
            if (!group.isHidden()) continue;
            if (group.getName().equals(name) && onlinePlayer.getWorld().equals(world)) {
                return group;
            }
        }
        return null;
    }

    public static void setGroupType(Group group, Group.Type type) {
        if (group.getType() == type) return;
        Group newGroup = serverApi.groupBuilder()
                .setName(group.getName())
                .setType(type)
                .setHidden(true)
                .build();
        transferGroup = newGroup;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!isPlayerConnected(onlinePlayer)) continue;
            if (!serverApi.getConnectionOf(onlinePlayer.getUniqueId()).isInGroup()) continue;
            if (!serverApi.getConnectionOf(onlinePlayer.getUniqueId()).getGroup().equals(group)) continue;
            serverApi.getConnectionOf(onlinePlayer.getUniqueId()).setGroup(newGroup);
        }
        transferGroup = null;
    }

    public static void resetAudibility(Player player) {
        if (!isPlayerConnected(player)) return;
        audibility.remove(player);
        for (Player hearer : audibility.keySet()) {
            audibility.get(hearer).remove(player);
        }
    }

    public static void editAudibility(Player player, Player toHear, boolean hear) {
        if (!isPlayerConnected(player)) return;
        if (!hear) {
            if (audibility.containsKey(player)) {
                audibility.get(player).add(toHear);
            } else {
                ArrayList<Player> players = new ArrayList<>();
                players.add(toHear);
                audibility.put(player, players);
            }
        } else if (audibility.containsKey(player)) audibility.get(player).remove(toHear);
    }
}

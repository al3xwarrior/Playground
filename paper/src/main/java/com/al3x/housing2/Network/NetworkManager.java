package com.al3x.housing2.Network;

import com.al3x.housing2.Main;
import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import com.al3x.housing2.network.payload.serverbound.ServerboundImport;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import wtf.choco.network.Message;
import wtf.choco.network.data.NamespacedKey;

import java.util.HashMap;
import java.util.UUID;

public class NetworkManager implements Listener {
    private final HashMap<UUID, PlaygroundServerboundMessageListener> listeners = new HashMap<>();

    public NetworkManager(Main plugin) {
        Playground.PROTOCOL.registerChannels(new PlaygroundBukkitChannelRegistrar(plugin));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        PlayerNetwork player = PlayerNetwork.getNetwork(event.getPlayer());
        this.listeners.put(player.getPlayerUUID(), player.getServerboundMessageListener()); // Our "impl" type!
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        PlayerNetwork.removeNetwork(event.getPlayer());
        this.listeners.remove(event.getPlayer().getUniqueId()); // Clean up!
    }

    public PlaygroundServerboundMessageListener getListener(Player player) {
        return listeners.get(player.getUniqueId());
    }
}

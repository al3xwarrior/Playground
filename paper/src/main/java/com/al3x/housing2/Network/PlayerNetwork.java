package com.al3x.housing2.Network;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import wtf.choco.network.Message;
import wtf.choco.network.data.NamespacedKey;
import wtf.choco.network.receiver.MessageReceiver;

import java.util.HashMap;
import java.util.UUID;

public class PlayerNetwork implements MessageReceiver {
    private static HashMap<UUID, PlayerNetwork> playerNetworks = new HashMap<>();

    private final Player player;
    private final PlayerNetworkListener networkListener;
    private int protocolVersion = 1;

    //port, ActionsMenu
    private HashMap<Integer, ActionsMenu> actionsMenus = new HashMap<>();

    private boolean isUsingMod = false;

    @ApiStatus.Internal
    public PlayerNetwork(@NotNull Player player) {
        this.player = player;
        this.networkListener = new PlayerNetworkListener(this);
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public boolean isUsingMod() {
        return isUsingMod;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    @NotNull
    public UUID getPlayerUUID() {
        return player.getUniqueId();
    }

    public void setUsingMod(boolean usingMod) {
        isUsingMod = usingMod;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setActionsMenu(int port, ActionsMenu menu) {
        actionsMenus.put(port, menu);
    }

    public ActionsMenu getActionsMenu(int port) {
        return actionsMenus.get(port);
    }

    public void removeActionsMenu(int port) {
        actionsMenus.remove(port);
    }

    public void clearActionsMenus() {
        actionsMenus.clear();
    }

    @ApiStatus.Internal
    @NotNull
    public PlaygroundServerboundMessageListener getServerboundMessageListener() {
        return networkListener;
    }

    @Override
    public void sendMessage(@NotNull NamespacedKey channel, byte @NotNull [] message) {
        this.player.sendPluginMessage(Main.getInstance(), channel.toString(), message);
    }

    public void sendMessage(@NotNull Message<PlaygroundClientboundMessageListener> message) {
        Playground.PROTOCOL.sendMessageToClient(this, message);
    }

    public static PlayerNetwork getNetwork(@NotNull Player player) {
        return playerNetworks.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerNetwork(player));
    }

    public static void removeNetwork(@NotNull Player player) {
        playerNetworks.remove(player.getUniqueId());
    }
}

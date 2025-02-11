package com.al3x.housing2.Network;

import com.al3x.housing2.Main;
import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import wtf.choco.network.ChannelRegistrar;
import wtf.choco.network.Message;
import wtf.choco.network.bukkit.BukkitChannelRegistrar;

/**
 * A {@link ChannelRegistrar} implementation for VeinMiner on Bukkit servers.
 */
public final class PlaygroundBukkitChannelRegistrar extends BukkitChannelRegistrar<Main, PlaygroundServerboundMessageListener, PlaygroundClientboundMessageListener> {

    /**
     * Construct a new {@link PlaygroundBukkitChannelRegistrar}.
     *
     * @param plugin the plugin instance
     */
    public PlaygroundBukkitChannelRegistrar(@NotNull Main plugin) {
        super(plugin, Playground.PROTOCOL);
    }

    @Override
    protected PlaygroundServerboundMessageListener onSuccessfulMessage(@NotNull Player player, @NotNull String channel, @NotNull Message<PlaygroundServerboundMessageListener> message) {
        return Main.getInstance().getNetworkManager().getListener(player);
    }

}
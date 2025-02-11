package com.al3x.playground.fabric.networking;

import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import com.al3x.playground.fabric.FabricPlayground;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.choco.network.Message;
import wtf.choco.network.fabric.FabricChannelRegistrar;

public final class PlaygroundClientChannelRegistrar extends FabricChannelRegistrar<PlaygroundServerboundMessageListener, PlaygroundClientboundMessageListener> {

    public PlaygroundClientChannelRegistrar(@NotNull org.slf4j.Logger logger) {
        super(Playground.PROTOCOL, logger, true /* passing true lets the registrar know we're on the client! */);
    }

    @Override
    protected @Nullable PlaygroundClientboundMessageListener onSuccessfulClientboundMessage(@NotNull Identifier channel, @NotNull Message<PlaygroundClientboundMessageListener> message) {
        return FabricPlayground.LISTENER;
    }
}
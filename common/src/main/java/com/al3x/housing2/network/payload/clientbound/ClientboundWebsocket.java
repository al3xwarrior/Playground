package com.al3x.housing2.network.payload.clientbound;

import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import wtf.choco.network.Message;
import wtf.choco.network.MessageByteBuffer;

/**
 * A client bound {@link Message} with no data.
 * <p>
 */
public final class ClientboundWebsocket implements Message<PlaygroundClientboundMessageListener> {    /*
     * At the moment, this message serves no purpose other than to inform the client that
     * the server has acknowledged its presence. In the future, this message may be used to return
     * to the client crucial information.
     */

    private int port;

    /**
     * Construct a new {@link ClientboundWebsocket}.
     */
    public ClientboundWebsocket(int port) {
        this.port = port;
    }

    /**
     * Construct a new {@link ClientboundWebsocket} with input.
     *
     * @param buffer the input buffer
     */
    @Internal
    public ClientboundWebsocket(@NotNull MessageByteBuffer buffer) {
        this.port = buffer.readVarInt();
    }

    @Override
    public void write(@NotNull MessageByteBuffer buffer) {
        buffer.writeVarInt(port);
    }

    @Override
    public void handle(@NotNull PlaygroundClientboundMessageListener listener) {
        listener.handleWebsocket(this);
    }

    public int getPort() {
        return port;
    }

}
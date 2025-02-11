package com.al3x.housing2.network.payload.serverbound;

import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import wtf.choco.network.Message;
import wtf.choco.network.MessageByteBuffer;

/**
 * A server bound {@link Message} including the following data:
 * <ol>
 *   <li><strong>VarInt</strong>: protocol version
 * </ol>
 * Sent when a client joins the server.
 */
public final class ServerboundHandshake implements Message<PlaygroundServerboundMessageListener> {

    private final int protocolVersion;
    /**
     * Construct a new {@link ServerboundHandshake}.
     *
     * @param protocolVersion the client's VeinMiner protocol version
     */
    public ServerboundHandshake(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Construct a new {@link ServerboundHandshake} with input.
     *
     * @param buffer the input buffer
     */
    @Internal
    public ServerboundHandshake(@NotNull MessageByteBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
    }

    /**
     * Get the client's VeinMiner protocol version.
     *
     * @return the protocol version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public void write(@NotNull MessageByteBuffer buffer) {
        buffer.writeVarInt(protocolVersion);
    }

    @Override
    public void handle(@NotNull PlaygroundServerboundMessageListener listener) {
        listener.handleHandshake(this);
    }

}
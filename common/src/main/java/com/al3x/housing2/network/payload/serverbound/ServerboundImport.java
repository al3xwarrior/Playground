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
public final class ServerboundImport implements Message<PlaygroundServerboundMessageListener> {

    private final int protocolVersion;
    private final String htslContent;
    /**
     * Construct a new {@link ServerboundImport}.
     *
     * @param protocolVersion the client's VeinMiner protocol version
     */
    public ServerboundImport(int protocolVersion, String htslContent) {
        this.protocolVersion = protocolVersion;
        this.htslContent = htslContent;
    }

    /**
     * Construct a new {@link ServerboundImport} with input.
     *
     * @param buffer the input buffer
     */
    @Internal
    public ServerboundImport(@NotNull MessageByteBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.htslContent = buffer.readString();
    }

    /**
     * Get the client's VeinMiner protocol version.
     *
     * @return the protocol version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getHtslContent() {
        return htslContent;
    }

    @Override
    public void write(@NotNull MessageByteBuffer buffer) {
        buffer.writeVarInt(protocolVersion);
        buffer.writeString(htslContent);
    }

    @Override
    public void handle(@NotNull PlaygroundServerboundMessageListener listener) {
        listener.handleImport(this);
    }

}
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
public final class ServerboundWebsocket implements Message<PlaygroundServerboundMessageListener> {

    private final int protocolVersion;
    private final String htslContent;
    private final int port;
    /**
     * Construct a new {@link ServerboundWebsocket}.
     *
     * @param protocolVersion the client's VeinMiner protocol version
     */
    public ServerboundWebsocket(int protocolVersion, String htslContent, int port) {
        this.protocolVersion = protocolVersion;
        this.htslContent = htslContent;
        this.port = port;
    }

    /**
     * Construct a new {@link ServerboundWebsocket} with input.
     *
     * @param buffer the input buffer
     */
    @Internal
    public ServerboundWebsocket(@NotNull MessageByteBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.htslContent = buffer.readString();
        this.port = buffer.readVarInt();
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

    public int getPort() {
        return port;
    }

    @Override
    public void write(@NotNull MessageByteBuffer buffer) {
        buffer.writeVarInt(protocolVersion);
        buffer.writeString(htslContent);
        buffer.writeVarInt(port);
    }

    @Override
    public void handle(@NotNull PlaygroundServerboundMessageListener listener) {
        listener.handleWebsocket(this);
    }

}
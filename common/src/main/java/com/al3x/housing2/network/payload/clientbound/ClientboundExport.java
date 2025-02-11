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
public final class ClientboundExport implements Message<PlaygroundClientboundMessageListener> {
    private final String htslContent;
    /*
     * At the moment, this message serves no purpose other than to inform the client that
     * the server has acknowledged its presence. In the future, this message may be used to return
     * to the client crucial information.
     */

    /**
     * Construct a new {@link ClientboundExport}.
     */
    public ClientboundExport(String htslContent) {
        this.htslContent = htslContent;
    }

    /**
     * Construct a new {@link ClientboundExport} with input.
     *
     * @param buffer the input buffer
     */
    @Internal
    public ClientboundExport(@NotNull MessageByteBuffer buffer) {
        this.htslContent = buffer.readString();
    }

    public String getHtslContent() {
        return htslContent;
    }

    @Override
    public void write(@NotNull MessageByteBuffer buffer) {
        buffer.writeString(htslContent);
    }

    @Override
    public void handle(@NotNull PlaygroundClientboundMessageListener listener) {
        listener.handleExport(this);
    }

}
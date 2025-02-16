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
public final class ClientboundSyntax implements Message<PlaygroundClientboundMessageListener> {
    private final String actions;
    private final String conditions;
    /**
     * Construct a new {@link ClientboundImport}.
     */
    public ClientboundSyntax(String actions, String conditions) {
        this.actions = actions;
        this.conditions = conditions;
    }

    /**
     * Construct a new {@link ClientboundImport} with input.
     *
     * @param buffer the input buffer
     */
    @Internal
    public ClientboundSyntax(@NotNull MessageByteBuffer buffer) {
        this.actions = buffer.readString();
        this.conditions = buffer.readString();
    }

    @Override
    public void write(@NotNull MessageByteBuffer buffer) {
        buffer.writeString(actions);
        buffer.writeString(conditions);
    }

    @Override
    public void handle(@NotNull PlaygroundClientboundMessageListener listener) {
        listener.handleSyntax(this);
    }

    public String getActions() {
        return actions;
    }

    public String getConditions() {
        return conditions;
    }

}
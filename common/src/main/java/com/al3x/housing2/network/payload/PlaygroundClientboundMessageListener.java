package com.al3x.housing2.network.payload;

import com.al3x.housing2.network.payload.clientbound.*;
import org.jetbrains.annotations.NotNull;

import wtf.choco.network.listener.ClientboundMessageListener;

/**
 * VeinMiner's client bound plugin message listener.
 * <p>
 * Methods in this class are intentionally undocumented as they are mostly self-explanatory.
 */
public interface PlaygroundClientboundMessageListener extends ClientboundMessageListener {

    /**
     * Handles the {@link ClientboundExport} message.
     *
     * @param message the message
     */
    public void handleExport(@NotNull ClientboundExport message);

    public void handleHandshake(@NotNull ClientboundHandshake message);

    public void handleImport(ClientboundImport clientboundImport);

    public void handleSyntax(ClientboundSyntax clientboundSyntax);

    void handleWebsocket(ClientboundWebsocket clientboundWebsocket);
}
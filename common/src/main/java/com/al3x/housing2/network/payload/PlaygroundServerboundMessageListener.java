package com.al3x.housing2.network.payload;

import com.al3x.housing2.network.payload.clientbound.ClientboundExport;
import com.al3x.housing2.network.payload.serverbound.ServerboundHandshake;
import com.al3x.housing2.network.payload.serverbound.ServerboundImport;
import com.al3x.housing2.network.payload.serverbound.ServerboundWebsocket;
import org.jetbrains.annotations.NotNull;

import wtf.choco.network.listener.ServerboundMessageListener;

/**
 * VeinMiner's server bound plugin message listener.
 * <p>
 * Methods in this class are intentionally undocumented as they are mostly self-explanatory.
 */
public interface PlaygroundServerboundMessageListener extends ServerboundMessageListener {

    /**
     * Handles the {@link ClientboundExport} message.
     *
     * @param message the message
     */
    public void handleImport(@NotNull ServerboundImport message);

    public void handleHandshake(@NotNull ServerboundHandshake message);

    public void handleWebsocket(ServerboundWebsocket serverboundWebsocket);
}
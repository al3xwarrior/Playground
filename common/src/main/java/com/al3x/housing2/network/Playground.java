package com.al3x.housing2.network;

import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import com.al3x.housing2.network.payload.clientbound.*;
import com.al3x.housing2.network.payload.serverbound.ServerboundHandshake;
import com.al3x.housing2.network.payload.serverbound.ServerboundImport;
import com.al3x.housing2.network.payload.serverbound.ServerboundWebsocket;
import wtf.choco.network.MessageProtocol;
import wtf.choco.network.data.NamespacedKey;

public final class Playground {

    public static final NamespacedKey CHANNEL = NamespacedKey.of("playground", "playground");
    public static final int VERSION = 1;

    public static final MessageProtocol<PlaygroundServerboundMessageListener, PlaygroundClientboundMessageListener> PROTOCOL = new MessageProtocol<>(CHANNEL, VERSION,
            // These can be empty for now
            serverboundRegistry ->
                    serverboundRegistry
                            .registerMessage(ServerboundHandshake.class, ServerboundHandshake::new)
                            .registerMessage(ServerboundImport.class, ServerboundImport::new)
                            .registerMessage(ServerboundWebsocket.class, ServerboundWebsocket::new),
            clientboundRegistry ->
                    clientboundRegistry
                            .registerMessage(ClientboundHandshake.class, ClientboundHandshake::new)
                            .registerMessage(ClientboundExport.class, ClientboundExport::new)
                            .registerMessage(ClientboundImport.class, ClientboundImport::new)
                            .registerMessage(ClientboundSyntax.class, ClientboundSyntax::new)
                            .registerMessage(ClientboundWebsocket.class, ClientboundWebsocket::new)
    );

    private Playground() { } // This will prevent anyone from constructing this class

}
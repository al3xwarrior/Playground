package com.al3x.playground.fabric;

import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.serverbound.ServerboundHandshake;
import com.al3x.playground.fabric.networking.FabricServerState;
import com.al3x.playground.fabric.networking.PlaygroundClientChannelRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.choco.network.fabric.FabricProtocolConfiguration;


public class FabricPlayground implements ClientModInitializer {
    public static FabricServerState LISTENER; // Our "impl" type!

    private static final Logger LOGGER = LoggerFactory.getLogger("playground");

    @Override
    public void onInitializeClient() {
        LISTENER = new FabricServerState(this, MinecraftClient.getInstance());

        Playground.PROTOCOL.registerChannels(new PlaygroundClientChannelRegistrar(LOGGER));
        Playground.PROTOCOL.configure(new FabricProtocolConfiguration(true));

        LOGGER.info("Registered Playground client channel");

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ServerboundHandshake handshake = new ServerboundHandshake(1);
            LISTENER.sendMessage(handshake);
        });

    }
}

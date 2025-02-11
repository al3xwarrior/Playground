package com.al3x.playground.fabric.networking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import com.al3x.housing2.network.payload.clientbound.ClientboundExport;
import com.al3x.housing2.network.payload.clientbound.ClientboundHandshake;
import com.al3x.housing2.network.payload.clientbound.ClientboundImport;
import com.al3x.housing2.network.payload.serverbound.ServerboundImport;
import com.al3x.playground.fabric.FabricPlayground;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import wtf.choco.network.Message;
import wtf.choco.network.data.NamespacedKey;
import wtf.choco.network.fabric.FabricMessageReceiver;
import wtf.choco.network.fabric.RawDataPayload;

/**
 * The client's state on a connected server.
 */
public final class FabricServerState implements FabricMessageReceiver, PlaygroundClientboundMessageListener {
    private boolean enabledOnServer;

    /**
     * Construct a new {@link FabricServerState}.
     *
     * @param client the client instance
     */
    public FabricServerState(@NotNull FabricPlayground client, @NotNull MinecraftClient minecraft) {
        // We'll enable VeinMiner if we're in single player development mode, just for testing
        if (minecraft.isInSingleplayer() && FabricLoader.getInstance().isDevelopmentEnvironment()) {
            this.enabledOnServer = true;
        }
    }

    public boolean isEnabledOnServer() {
        return enabledOnServer;
    }

    @Override
    public void sendMessage(@NotNull RawDataPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public void sendMessage(@NotNull Message<PlaygroundServerboundMessageListener> message) {
        Playground.PROTOCOL.sendMessageToServer(this, message);
    }

    @Override
    public void handleExport(@NotNull ClientboundExport message) {
        File exportFile = new File(FabricLoader.getInstance().getConfigDirectory(), "playground_export.htsl");
        try {
            FileUtils.writeStringToFile(exportFile, message.getHtslContent(), "UTF-8");

            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("§aHTSL file exported to §6" + exportFile.getAbsolutePath())
                            .setStyle(
                                    Style.EMPTY
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, exportFile.getAbsolutePath()))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("Click to open file")))
                            ), false
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleHandshake(@NotNull ClientboundHandshake message) {
        enabledOnServer = true;
    }

    @Override
    public void handleImport(ClientboundImport clientboundImport) {
        File importFile = new File(FabricLoader.getInstance().getConfigDirectory(), "playground_export.htsl");

        if (!importFile.exists()) {
            MinecraftClient.getInstance().player.sendMessage(
                Text.literal("§cNo HTSL file found at §6" + importFile.getAbsolutePath()), false
            );
            return;
        }

        try {
            String htslContent = FileUtils.readFileToString(importFile, "UTF-8");
            sendMessage(new ServerboundImport(1, htslContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
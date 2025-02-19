package com.al3x.playground.fabric.networking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.al3x.housing2.network.Playground;
import com.al3x.housing2.network.payload.PlaygroundClientboundMessageListener;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import com.al3x.housing2.network.payload.clientbound.*;
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
        File exportFile = new File(FabricLoader.getInstance().getConfigDirectory(), "playground_export.ptsl");
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
        File importFile = new File(FabricLoader.getInstance().getConfigDirectory(), "playground_export.ptsl");

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

    @Override
    public void handleSyntax(ClientboundSyntax clientboundSyntax) {
        String actionsSyntax = clientboundSyntax.getActions();
        String conditionsSyntax = clientboundSyntax.getConditions();

        String output = "Actions Syntax:\n" + actionsSyntax + "\n\nConditions Syntax:\n" + conditionsSyntax;

        File syntaxFile = new File(FabricLoader.getInstance().getConfigDirectory(), "playground_syntax.txt");
        try {
            FileUtils.writeStringToFile(syntaxFile, output, "UTF-8");

            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("§aSyntax file exported to §6" + syntaxFile.getAbsolutePath())
                            .setStyle(
                                    Style.EMPTY
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, syntaxFile.getAbsolutePath()))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("Click to open file")))
                            ), false
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleWebsocket(ClientboundWebsocket clientboundWebsocket) {
        String url = "localhost:" + clientboundWebsocket.getPort();
        MinecraftClient.getInstance().player.sendMessage(Text.literal("§aOpening websocket at §6" + url), false);
        try {
            new AutoImportWebsocket().start(url, this);
            MinecraftClient.getInstance().player.sendMessage(Text.literal("§aWebsocket opened successfully"), false);
        } catch (Exception e) {
            e.printStackTrace();
            MinecraftClient.getInstance().player.sendMessage(Text.literal("§cFailed to open websocket: " + e.getMessage()), false);
        }
    }
}
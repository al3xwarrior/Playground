package com.al3x.playground.fabric.networking;

import com.al3x.housing2.network.payload.serverbound.ServerboundWebsocket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AutoImportWebsocket {
    FabricServerState state;
    public void start(String url, FabricServerState state) throws IOException, WebSocketException {
        WebSocket ws = new WebSocketFactory().createSocket("ws://" + url);
        ws.addListener(new AutoImportWebsocketListener());
        ws.connect();
        this.state = state;
    }

    public class AutoImportWebsocketListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket ws, String message) {
            int port = ws.getURI().getPort();
            JsonObject json = new Gson().fromJson(message, JsonObject.class);

            if (state != null) {
                ServerboundWebsocket websocket = new ServerboundWebsocket(1, json.get("content").getAsString(), port);
                state.sendMessage(websocket);
            }
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            ServerboundWebsocket message = new ServerboundWebsocket(1, "cannotconnect:" + exception.getMessage(), websocket.getURI().getPort());
            state.sendMessage(message);
        }

        @Override
        public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onCloseFrame(websocket, frame);
            System.out.println("Websocket closed on port " + websocket.getURI().getPort());
            ServerboundWebsocket message = new ServerboundWebsocket(1, "closed", websocket.getURI().getPort());
            state.sendMessage(message);
        }
    }
}

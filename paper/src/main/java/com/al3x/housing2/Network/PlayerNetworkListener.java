package com.al3x.housing2.Network;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.network.payload.PlaygroundServerboundMessageListener;
import com.al3x.housing2.network.payload.clientbound.ClientboundHandshake;
import com.al3x.housing2.network.payload.serverbound.ServerboundHandshake;
import com.al3x.housing2.network.payload.serverbound.ServerboundImport;
import com.al3x.housing2.network.payload.serverbound.ServerboundWebsocket;
import com.google.common.base.Preconditions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

@Internal
public final class PlayerNetworkListener implements PlaygroundServerboundMessageListener {
    private final PlayerNetwork player;

    PlayerNetworkListener(@NotNull PlayerNetwork player) {
        Preconditions.checkArgument(player != null, "player must not be null");
        this.player = player;
    }

    @Override
    public void handleImport(@NotNull ServerboundImport message) {
        try {
            List<Action> action = HTSLHandler.importActions(message.getHtslContent(), "");
            Menu menu = MenuManager.getPlayerMenu(player.getPlayer());
            if (menu instanceof ActionsMenu actionsMenu) {
                actionsMenu.getActions().clear();
                actionsMenu.getActions().addAll(action);
                actionsMenu.setupItems();
                player.getPlayer().sendMessage(colorize("&aSuccessfully imported actions!"));
            } else {
                player.getPlayer().sendMessage(colorize("&cFailed to import actions!"));
                player.getPlayer().sendMessage(colorize("&8 - You must have an actions menu open to import actions!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Main.getInstance().getLogger().severe("Failed to import actions: " + e.getMessage());
            player.getPlayer().sendMessage(colorize("&cFailed to import actions!"));
            player.getPlayer().sendMessage(colorize("&8 - " + e.getMessage()));
        }
    }

    @Override
    public void handleHandshake(@NotNull ServerboundHandshake message) {
        if (message.getProtocolVersion() != 2) {
            player.getPlayer().sendMessage(colorize("&6You are using an outdated version of the fabric mod!"));
            player.getPlayer().sendMessage(colorize("&6Please update to the latest version!"));
        }
        player.setProtocolVersion(message.getProtocolVersion());
        player.setUsingMod(true);

        player.getPlayer().sendMessage(colorize("&aSuccessfully connected with fabric mod!"));

        ClientboundHandshake response = new ClientboundHandshake(2);
        player.sendMessage(response);
    }

    @Override
    public void handleWebsocket(ServerboundWebsocket serverboundWebsocket) {
        if (player.getActionsMenu(serverboundWebsocket.getPort()) != null) {
            //The user closed the websocket and we need to remove the actions menu so we can open a new one or whatever
            if (serverboundWebsocket.getHtslContent().equalsIgnoreCase("closed")) {
                player.setActionsMenu(serverboundWebsocket.getPort(), null);
                return;
            }
            //The user failed to connect to the websocket
            if (serverboundWebsocket.getHtslContent().startsWith("cannotconnect")) {
                player.getPlayer().sendMessage(colorize("&cFailed to connect to websocket!"));
                player.getPlayer().sendMessage(colorize("&8 - " + serverboundWebsocket.getHtslContent().substring(14)));
                return;
            }
            try {
                List<Action> action = HTSLHandler.importActions(serverboundWebsocket.getHtslContent(), "");
                ActionsMenu actionsMenu = player.getActionsMenu(serverboundWebsocket.getPort());
                actionsMenu.getActions().clear(); //This will update the actions internally
                actionsMenu.getActions().addAll(action);
                actionsMenu.setupItems(); //we call this just to update an npc or whatever if it uses a different forum of storing actions
                if (MenuManager.getPlayerMenu(player.getPlayer()) instanceof ActionsMenu menu) {
                    menu.setupItems();
                }
            } catch (Exception ignored) { //We don't want to spam the user with errors since it gets called every time the websocket updates
            }
        }
    }
}
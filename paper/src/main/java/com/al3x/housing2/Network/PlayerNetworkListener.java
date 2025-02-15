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
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Failed to import actions: " + e.getMessage());
            player.getPlayer().sendMessage(colorize("&cFailed to import actions!"));
        }
    }

    @Override
    public void handleHandshake(@NotNull ServerboundHandshake message) {
        player.setUsingMod(true);

        player.getPlayer().sendMessage(colorize("&aSuccessfully connected with fabric mod!"));

        ClientboundHandshake response = new ClientboundHandshake();
        player.sendMessage(response);
    }
}
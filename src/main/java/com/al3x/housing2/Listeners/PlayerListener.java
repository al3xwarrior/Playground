package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static com.al3x.housing2.Utils.Color.colorize;

public class PlayerListener implements Listener {

    // private final Map<Player, Double> playerSpeeds = new HashMap<>();

    // @EventHandler
    // public void onPlayerMove(PlayerMoveEvent event) {
    //     Player player = event.getPlayer();
    //
    //     // Get previous and new location as vectors
    //     Vector from = event.getFrom().toVector();
    //     Vector to = event.getTo().toVector();
    //
    //     // Calculate velocity magnitude (distance per tick)
    //     double speed = to.subtract(from).length();
    //
    //     playerSpeeds.put(player, speed);
    // }

    // public double getPlayerSpeed(Player player) {
    //    return playerSpeeds.getOrDefault(player, 0.0);
    // }

    private HousesManager housesManager;

    public PlayerListener(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        // Disable teleporting in spectator mode
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            // ...if they don't have permission to teleport
            if (!housesManager.hasPermissionInHouse(player, Permissions.COMMAND_TP)) {
                player.sendMessage(colorize("&cYou don't have permission to teleport in this house."));
                event.setCancelled(true);
            }

            // ...or if the player is not in the same house
            if (event.getFrom().getWorld() != event.getTo().getWorld()) {
                player.sendMessage(colorize("&cThat player is not in the same house as you!"));
                event.setCancelled(true);
            }
        }
    }
}

package com.al3x.housing2.Listeners;

import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Utils.NbtItemBuilder;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
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

        // Prevent teleporting between worlds
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (event.getFrom().getWorld() != event.getTo().getWorld()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickupItem(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack().clone();
        NbtItemBuilder nbt = new NbtItemBuilder(itemStack);
        String droppedItem = nbt.getString("droppedItem");
        if (droppedItem != null) {
            nbt.remove("droppedItem");
        }
        nbt.build();
    }
}

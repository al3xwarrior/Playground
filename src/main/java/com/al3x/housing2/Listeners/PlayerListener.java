package com.al3x.housing2.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {

    private final Map<Player, Double> playerSpeeds = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Get previous and new location as vectors
        Vector from = event.getFrom().toVector();
        Vector to = event.getTo().toVector();

        // Calculate velocity magnitude (distance per tick)
        double speed = to.subtract(from).length();

        playerSpeeds.put(player, speed);
    }

    public double getPlayerSpeed(Player player) {
        return playerSpeeds.getOrDefault(player, 0.0);
    }
}

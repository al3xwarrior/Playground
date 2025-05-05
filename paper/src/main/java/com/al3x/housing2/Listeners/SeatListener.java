package com.al3x.housing2.Listeners;

import com.al3x.housing2.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SeatListener implements Listener {

    private Main main;

    public SeatListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        // Only handle unsit when the player is sitting (inside a seat) and is toggling sneak ON.
        if (player.isInsideVehicle() && event.isSneaking()) {
            // Call unsit – this method should handle safe dismount (delayed teleport, flags, etc.)
            this.main.getSeatManager().unsit(player);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted().getScoreboardTags().contains("SimpleSeatEntity")) {
            event.getDismounted().remove();
        }
    }
}

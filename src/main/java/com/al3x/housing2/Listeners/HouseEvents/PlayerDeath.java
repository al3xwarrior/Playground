package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.JoinLeaveHouse;
import com.al3x.housing2.Utils.AbstractCancellable;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerDeath implements Listener {

    private HousesManager housesManager;

    public PlayerDeath(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getHealth() - e.getFinalDamage() > 0 || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
            return;
        }

        Cancellable event = new AbstractCancellable();
        sendEventExecution(housesManager, EventType.PLAYER_DEATH, player, event);

        if (event.isCancelled()) {
            HousingWorld house = housesManager.getHouse(player.getWorld());
            JoinLeaveHouse.resetPlayer(player);
            if (house != null) {
                player.teleport(house.getSpawn());
            }
        }
    }
}

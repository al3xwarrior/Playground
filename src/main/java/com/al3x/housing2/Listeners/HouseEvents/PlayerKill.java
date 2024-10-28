package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.JoinLeaveHouse;
import com.al3x.housing2.Utils.AbstractCancellable;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerKill implements Listener {

    private HousesManager housesManager;

    public PlayerKill(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(EntityDamageByEntityEvent e) {
        DamageSource damageSource = e.getDamageSource();

        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getHealth() - e.getFinalDamage() > 0) {
            return;
        }

        if (damageSource.getCausingEntity() == null || !(damageSource.getCausingEntity() instanceof Player)) {
            return;
        }

        Cancellable event = new AbstractCancellable();
        sendEventExecution(housesManager, EventType.PLAYER_KILL, (Player) e.getDamageSource().getCausingEntity(), event);

        if (event.isCancelled()) {
            HousingWorld house = housesManager.getHouse(player.getWorld());
            JoinLeaveHouse.resetPlayer(player);
            if (house != null) {
                player.teleport(house.getSpawn());
            }
        }

    }
}

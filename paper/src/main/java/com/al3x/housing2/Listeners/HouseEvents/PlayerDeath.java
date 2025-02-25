package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Listeners.JoinLeaveHouse;
import com.al3x.housing2.Utils.AbstractCancellable;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerDeath implements Listener {

    private HousesManager housesManager;

    public static HashMap<UUID, Duple<UUID, Integer>> keepHouseLoaded = new HashMap<>();

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        HousingWorld house = housesManager.getHouse(player.getWorld());

        e.getDrops().removeIf(item -> item.getItemMeta().getDisplayName().equals("§dHousing Menu §7(Right-Click)"));

        keepHouseLoaded.put(player.getUniqueId(), new Duple<>(house.getHouseUUID(), 0)); //We put them in a map so we know to keep the house loaded

        if (house != null) {
            if (house.getDeathMessages()) {
                String deathMessage = e.getDeathMessage();
                e.setDeathMessage(null);
                house.getWorld().getPlayers().forEach(p -> p.sendMessage(deathMessage));
            } else {
                e.setDeathMessage(null);
            }

            if (house.getKeepInventory()) {
                e.getDrops().clear();
            }

            e.setKeepInventory(house.getKeepInventory());
            e.setKeepLevel(house.getKeepInventory());
        }
    }
}

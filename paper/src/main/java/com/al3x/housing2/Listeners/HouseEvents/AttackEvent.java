package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class AttackEvent implements Listener {
    public static HashMap<UUID, Entity> lastAttacked = new HashMap<>();
    public static HashMap<UUID, EntityDamageByEntityEvent> lastDamage = new HashMap<>();

    private HousesManager housesManager;

    public AttackEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;

        HousingWorld house = housesManager.getHouse(e.getEntity().getWorld());
        if (house == null) return;
        if (e.getEntity().getType().equals(EntityType.ITEM_FRAME) && !((house.hasPermission(player, Permissions.BUILD) && house.getOwner().isOnline()) || house.hasPermission(player, Permissions.OFFLINE_BUILD) && house.getOwner().isOnline())) {
            e.setCancelled(true);
        }

        lastAttacked.put(player.getUniqueId(), e.getEntity());
        lastDamage.put(player.getUniqueId(), e);
        sendEventExecution(housesManager, EventType.PLAYER_ATTACK, player, e);
        if (e.getEntity() instanceof Player) {
            sendEventExecution(housesManager, EventType.PLAYER_DAMAGE, (Player) e.getEntity(), e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile projectile)) return;
        if (!(projectile.getShooter() instanceof Player player)) return;
        lastAttacked.put(player.getUniqueId(), e.getEntity());
        lastDamage.put(player.getUniqueId(), e);
        sendEventExecution(housesManager, EventType.PLAYER_ATTACK, player, e);
        if (e.getEntity() instanceof Player) {
            sendEventExecution(housesManager, EventType.PLAYER_DAMAGE, (Player) e.getEntity(), e);
        }
    }
}

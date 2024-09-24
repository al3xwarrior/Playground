package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerKill implements Listener {

    private HousesManager housesManager;

    public PlayerKill(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        DamageSource damageSource = e.getDamageSource();
        if (damageSource.getCausingEntity() != null && damageSource.getCausingEntity() instanceof Player) {
            Player player = e.getDamageSource().getCausingEntity(); 
            World world = player.getWorld();
            if (world.getName().equals("world")) return;

            HousingWorld house = housesManager.getHouse(world);
            if (house == null) return;

            house.executeEventActions(EventType.PLAYER_KILL, player);
        }
    }
}

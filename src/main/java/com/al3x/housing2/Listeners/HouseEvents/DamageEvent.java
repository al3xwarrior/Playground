package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class DamageEvent implements Listener {

    private HousesManager housesManager;

    public DamageEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {return;}

        Player player = (Player) e.getEntity();
        World world = player.getWorld();
        if (world.getName().equals("world")) return;

        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return;

        house.executeEventActions(EventType.PLAYER_DAMAGE, player);
    }
}

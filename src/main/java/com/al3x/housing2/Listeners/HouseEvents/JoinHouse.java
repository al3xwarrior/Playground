package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class JoinHouse implements Listener {

    private HousesManager housesManager;

    public JoinHouse(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        World world = player.getWorld();
        if (world.getName().equals("world")) return;

        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return;

        house.executeEventActions(EventType.PLAYER_JOIN, player);
    }

}

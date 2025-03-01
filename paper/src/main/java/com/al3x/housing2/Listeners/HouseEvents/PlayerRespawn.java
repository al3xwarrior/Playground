package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerRespawn implements Listener {

    private Main main;
    private HousesManager housesManager;

    public PlayerRespawn(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        sendEventExecution(housesManager, EventType.PLAYER_RESPAWN, player, (Cancellable) null);

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;
        e.setRespawnLocation(house.getSpawn());

        PlayerDeath.keepHouseLoaded.remove(player.getUniqueId()); //Once they have respawned, we know they are either still in the house or have left on their own.

        Bukkit.getScheduler().runTaskLater(main, () -> {
            player.teleport(house.getSpawn());
        }, 1L);
    }

}

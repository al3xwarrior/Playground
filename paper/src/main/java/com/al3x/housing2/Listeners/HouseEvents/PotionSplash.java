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
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PotionSplash implements Listener {
    public static PotionSplashEvent potionSplashEvent = null;
    private HousesManager housesManager;

    public PotionSplash(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (!(e.getPotion().getShooter() instanceof Player player)) {
            return;
        }
        potionSplashEvent = e;
        sendEventExecution(housesManager, EventType.SPLASH_POTION, player, e);
    }
}

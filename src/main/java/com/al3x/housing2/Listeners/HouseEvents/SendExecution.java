package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class SendExecution {

    public static void sendEventExecution(HousesManager housesManager, EventType event, Player player, Cancellable e) {
        World world = player.getWorld();
        if (world.getName().equals("world")) return;
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return;
        house.executeEventActions(event, player, e);
    }
}

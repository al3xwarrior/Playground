package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import de.maxhenkel.voicechat.api.events.Event;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class SendExecution {

    public static boolean sendEventExecution(EventType event, Player player, HousingWorld house, Cancellable e) {
        if (house == null) return false;
        return house.executeEventActions(event, player, new CancellableEvent(null, e));
    }

    public static boolean sendEventExecution(HousesManager housesManager, EventType event, Player player, Cancellable e) {
        World world = player.getWorld();
        if (world.getName().equals("world")) return false;
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return false;
        return house.executeEventActions(event, player, new CancellableEvent(null, e));
    }

    public static boolean sendEventExecution(HousesManager housesManager, EventType event, Player player, Event e) {
        World world = player.getWorld();
        if (world.getName().equals("world")) return false;
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return false;
        return house.executeEventActions(event, player, new CancellableEvent(e, null));
    }
}

package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Action.Actions.ChangePlayerAttributeAction;
import com.al3x.housing2.Enums.AttributeType;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class LeaveHouse implements Listener {

    private final HousesManager housesManager;

    public LeaveHouse(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    private void leaveHouse(Player player, World world) {
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) { return; }
        sendEventExecution(EventType.PLAYER_QUIT, player, house, null);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (!e.getFrom().getName().equals("world")) {
            leaveHouse(e.getPlayer(), e.getFrom());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        leaveHouse(player.getPlayer(), player.getWorld());
    }

}

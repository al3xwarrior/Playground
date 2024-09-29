package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class AttackEvent implements Listener {

    private HousesManager housesManager;

    public AttackEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getDamageSource().getCausingEntity() instanceof Player)) {return;}
        sendEventExecution(housesManager, EventType.PLAYER_ATTACK, (Player) e.getDamageSource().getCausingEntity());
    }
}

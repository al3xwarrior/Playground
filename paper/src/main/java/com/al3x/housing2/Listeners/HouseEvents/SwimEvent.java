package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class SwimEvent implements Listener {
    private HousesManager housesManager;

    public SwimEvent(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onToggleSwim(EntityToggleSwimEvent e) {
        World world = e.getEntity().getWorld();
        if (world.getName().equals("world")) return;
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return;
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
            HousingNPC housingNPC = house.getNPC(npc.getId());
            if (housingNPC != null) {
                housingNPC.executeEventActions(house, EventType.NPC_DEATH, null, e);
            }
        } else {
            if (e.getEntity() instanceof Player player) {
                sendEventExecution(housesManager, EventType.ENTITY_SWIM_CRAWL, player, e);
            }
        }
    }
}

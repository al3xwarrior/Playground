package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class NPCEvents implements Listener {

    private final HousesManager housesManager;

    public NPCEvents(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onNPCDeath(EntityDeathEvent e) {
        if (e.getDamageSource().getCausingEntity() == null || !(e.getDamageSource().getCausingEntity() instanceof Player player)) return;
        World world = e.getEntity().getWorld();
        if (world.getName().equals("world")) return;
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return;
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
            HousingNPC housingNPC = house.getNPC(npc.getId());
            if (housingNPC != null) {
                housingNPC.executeEventActions(house, EventType.NPC_DEATH, player, e);
            }
        }
    }

    @EventHandler
    public void onNPCDamage(EntityDamageEvent e) {
        World world = e.getEntity().getWorld();
        if (world.getName().equals("world")) return;
        HousingWorld house = housesManager.getHouse(world);
        if (house == null) return;
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
            HousingNPC housingNPC = house.getNPC(npc.getId());
            if (housingNPC != null) {
                List<Action> actions = housingNPC.getEventActions().get(EventType.NPC_DAMAGE);
                if (actions != null) {
                    ActionExecutor executor = new ActionExecutor("event");
                    executor.addActions(actions);
                    executor.execute(npc, (e.getDamageSource().getCausingEntity() == null || !(e.getDamageSource().getCausingEntity() instanceof Player)) ? null : (Player) e.getDamageSource().getCausingEntity(), house, e);
                }
            }
        }
    }

}

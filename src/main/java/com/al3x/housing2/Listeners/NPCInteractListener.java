package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import static de.oliver.fancynpcs.api.actions.ActionTrigger.ANY_CLICK;
import static de.oliver.fancynpcs.api.actions.ActionTrigger.RIGHT_CLICK;

public class NPCInteractListener implements Listener {

    private Main main;
    private HousesManager housesManager;

    public NPCInteractListener(Main main) {
        this.main = main;
        this.housesManager = main.getHousesManager();
    }

    private void npcInteract(Player player, Npc npc, boolean rightClick, NpcInteractEvent e) {
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;

        HousingNPC hNpc = house.getNPC(npc.getEntityId());
        if (hNpc == null) return;

        e.setCancelled(true);
        if (house.getOwnerUUID().equals(player.getUniqueId()) && player.isSneaking() && rightClick) {
            new NPCMenu(main, player, hNpc).open();
            return;
        }

        hNpc.sendExecuteActions(house, player);
    }

    @EventHandler
    public void NPCInteraction(NpcInteractEvent e) {
        npcInteract(e.getPlayer(), e.getNpc(), e.getInteractionType() == ANY_CLICK, e);
    }

}

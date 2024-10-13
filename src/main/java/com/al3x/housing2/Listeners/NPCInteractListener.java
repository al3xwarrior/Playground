package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCInteractListener implements Listener {

    private Main main;
    private HousesManager housesManager;

    public NPCInteractListener(Main main) {
        this.main = main;
        this.housesManager = main.getHousesManager();
    }

    private void npcInteract(Player player, Entity entity, boolean rightClick) {
        if (!entity.hasMetadata("NPC")) return;

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;

        NPC citizensNPC = CitizensAPI.getNPCRegistry().getNPC(entity);
        HousingNPC npc = house.getNPC(citizensNPC.getId());
        if (npc == null) return;

        if (house.getOwnerUUID().equals(player.getUniqueId()) && player.isSneaking() && rightClick) {
            new NPCMenu(main, player, npc).open();
            return;
        }

        npc.sendExecuteActions(house, player);
    }

    @EventHandler
    public void leftClickNPC(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        npcInteract((Player) e.getDamager(), e.getEntity(), false);
    }


    @EventHandler
    public void rightClickNPC(PlayerInteractEntityEvent e) {
        npcInteract(e.getPlayer(), e.getRightClicked(), true);
    }


}

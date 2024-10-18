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

import java.util.HashMap;
import java.util.UUID;

public class NPCInteractListener implements Listener {
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
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
        if (cooldowns.containsKey(e.getDamager().getUniqueId())) {
            if (cooldowns.get(e.getDamager().getUniqueId()) > System.currentTimeMillis()) {
                return;
            }
        }
        npcInteract((Player) e.getDamager(), e.getEntity(), false);
        cooldowns.put(e.getDamager().getUniqueId(), System.currentTimeMillis() + 200);
    }


    @EventHandler
    public void rightClickNPC(PlayerInteractEntityEvent e) {
        if (cooldowns.containsKey(e.getPlayer().getUniqueId())) {
            if (cooldowns.get(e.getPlayer().getUniqueId()) > System.currentTimeMillis()) {
                return;
            }
        }
        npcInteract(e.getPlayer(), e.getRightClicked(), true);
        cooldowns.put(e.getPlayer().getUniqueId(), System.currentTimeMillis() + 200);
    }


}

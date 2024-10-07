package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.NPCMenu;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCInteractListener implements Listener {

    private Main main;
    private HousesManager housesManager;

    public NPCInteractListener(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void clickNPC(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        player.sendMessage("clicked");

        Entity entity = e.getRightClicked();
        if (!entity.hasMetadata("NPC")) return;

        player.sendMessage("its an npc");

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;

        player.sendMessage("house found");

        NPC citizensNPC = CitizensAPI.getNPCRegistry().getNPC(entity);
        HousingNPC npc = house.getNPC(citizensNPC.getId());
        if (npc == null) return;

        player.sendMessage("npc found");

        player.sendMessage((house.getOwnerUUID().equals(player.getUniqueId())) + " " + player.isSneaking());
        if (house.getOwnerUUID().equals(player.getUniqueId()) && player.isSneaking()) {
            new NPCMenu(main, player, housesManager, npc).open();
            return;
        }
    }

}

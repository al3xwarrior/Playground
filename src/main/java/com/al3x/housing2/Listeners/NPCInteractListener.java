package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.NPCMenu;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCInteractListener implements Listener {

    private Main main;
    private HousesManager housesManager;

    public NPCInteractListener(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void clickNPC(NpcInteractEvent e) {
        Player player = e.getPlayer();
        player.sendMessage("clicked npc");
        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;

        player.sendMessage("1");
        player.sendMessage(e.getNpc().getData().getId());
        HousingNPC npc = house.getNPC(e.getNpc().getData().getId());
        if (npc == null) return;

        player.sendMessage((house.getOwnerUUID().equals(player.getUniqueId())) + " " + player.isSneaking());
        if (house.getOwnerUUID().equals(player.getUniqueId()) && player.isSneaking()) {
            new NPCMenu(main, player, housesManager, npc).open();
            return;
        }
    }

}

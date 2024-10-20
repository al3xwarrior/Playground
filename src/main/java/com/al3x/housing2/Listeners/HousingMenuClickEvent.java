package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.OwnerHousingMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HousingMenuClickEvent implements Listener {

    private Main main;
    private HousesManager housesManager;

    public HousingMenuClickEvent(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getItem() != null && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {

            ItemStack item = e.getItem();

            // Whole lotta yap but it makes sure the player is house owner, and is clicking the menu
            if (item.getType().equals(Material.NETHER_STAR) && item.getItemMeta().getDisplayName().equals("§dHousing Menu §7(Right-Click") && housesManager.getHouse(player.getWorld()) != null && housesManager.getHouse(player.getWorld()).getOwnerUUID().equals(player.getUniqueId())) {
                new OwnerHousingMenu(main, player, housesManager.getHouse(player.getWorld())).open();
            }
        }
    }

}

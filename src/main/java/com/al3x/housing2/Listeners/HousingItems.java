package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HousingItems implements Listener {

    private HousesManager housesManager;

    public HousingItems(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        // Click block
        if (e.getItem() != null && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = e.getItem();

            Block block = e.getClickedBlock();
            Material itemType = item.getType();
            String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getItemMeta().getItemName();
            boolean ownerOfHouse = housesManager.getHouse(player.getWorld()) != null && housesManager.getHouse(player.getWorld()).getOwnerUUID().equals(player.getUniqueId());

            Bukkit.getLogger().info(name + " (" + name.equals("§aNPC") + " " + itemType.equals(Material.PLAYER_HEAD) + " " + ownerOfHouse + ")");
            if (name.equals("§aNPC") && itemType.equals(Material.PLAYER_HEAD) && ownerOfHouse) {
                e.setCancelled(true);
                housesManager.getHouse(player.getWorld()).createNPC(player, block.getLocation().add(new Vector(0.5, 1, 0.5)));
            }
        }
    }
}

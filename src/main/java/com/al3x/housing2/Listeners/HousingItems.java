package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
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
            ItemMeta itemMeta = item.getItemMeta();
            String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getItemMeta().getItemName();
            boolean ownerOfHouse = housesManager.getHouse(player.getWorld()) != null && housesManager.getHouse(player.getWorld()).getOwnerUUID().equals(player.getUniqueId());

            if (name.equals("§aNPC") && itemType.equals(Material.PLAYER_HEAD) && ownerOfHouse) {
                e.setCancelled(true);
                housesManager.getHouse(player.getWorld()).createNPC(player, block.getLocation().add(new Vector(0.5, 1, 0.5)));
            }

            NamespacedKey key = new NamespacedKey(Main.getInstance(), "isPath");
            NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "npc");
            if (itemMeta != null && itemMeta.getPersistentDataContainer().has(key) && itemMeta.getPersistentDataContainer().has(key2) && ownerOfHouse) {
                e.setCancelled(true);
                int id = itemMeta.getPersistentDataContainer().get(key2, PersistentDataType.INTEGER);
                HousingNPC npc = housesManager.getHouse(player.getWorld()).getNPC(id);
                npc.addPath(block.getLocation().add(0.5, 1, 0.5));
                player.sendMessage("§aAdded path node (" + (npc.getPath().size()) + ") at " + block.getX() + ", " + block.getY() + ", " + block.getZ());

            }

            if (name.equals("§aHologram") && itemType.equals(Material.NAME_TAG) && ownerOfHouse) {
                e.setCancelled(true);
                housesManager.getHouse(player.getWorld()).createHologram(player, block.getLocation().add(new Vector(0.5, 2, 0.5)));
            }
        }

        // Click air
        if (e.getItem() != null && e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack item = e.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getItemMeta().getItemName();
            boolean ownerOfHouse = housesManager.getHouse(player.getWorld()) != null && housesManager.getHouse(player.getWorld()).getOwnerUUID().equals(player.getUniqueId());

            NamespacedKey key = new NamespacedKey(Main.getInstance(), "isPath");
            NamespacedKey key2 = new NamespacedKey(Main.getInstance(), "npc");
            if (itemMeta != null && itemMeta.getPersistentDataContainer().has(key) && itemMeta.getPersistentDataContainer().has(key2) && ownerOfHouse) {
                e.setCancelled(true);
                int id = itemMeta.getPersistentDataContainer().get(key2, PersistentDataType.INTEGER);
                HousingNPC npc = housesManager.getHouse(player.getWorld()).getNPC(id);
                if (npc.getPath() != null && !npc.getPath().isEmpty()) {
                    Location loc = npc.removePath();
                    player.sendMessage("§aRemoved path node (" + (npc.getPath().size() + 1) + ") at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                }
            }
        }
    }
}

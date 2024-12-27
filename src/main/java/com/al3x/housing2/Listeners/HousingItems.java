package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.MyHousesMenu;
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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingItems implements Listener {

    private Main main;
    private HousesManager housesManager;

    public HousingItems(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        // In Lobby
        if (player.getWorld().equals(Bukkit.getWorld("world"))) {

            if (e.getItem() == null) return;

            ItemStack item = e.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getItemMeta().getItemName();

            // Browser
            if (name.equals("§aHousing Browser §7(Right-Click)")) {
                new HouseBrowserMenu(player, housesManager).open();
            }

            if (name.equals("§aMy Houses §7(Right-Click)")) {
                new MyHousesMenu(main, player, player).open();
            }

            if (name.equals("§aRandom House §7(Right-Click)")) {
                HousingWorld house = housesManager.getRandomPublicHouse();
                if (house != null) {
                    house.sendPlayerToHouse(player);
                } else {
                    player.sendMessage(colorize("&cThere are no public houses available!"));
                }
            }

            return;
        }

        executeCustomItem(player, e.getItem(), e.getAction());

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
                housesManager.getHouse(player.getWorld()).createHologram(player, block.getLocation().add(new Vector(0.5, 0, 0.5)));
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

    private void executeCustomItem(Player player, ItemStack item, Action action) {
        ClickType clickType = null;
        switch (action) {
            case LEFT_CLICK_BLOCK:
            case LEFT_CLICK_AIR: {
                clickType = ClickType.LEFT;
                break;
            }
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR: {
                clickType = ClickType.RIGHT;
                break;
            }
        }

        HousingWorld house = housesManager.getHouse(player.getWorld());
        if (house == null) return;

        if (clickType == null) return;

        Item customItem = Item.fromItemStack(item);

        if (customItem == null) return;

        if (customItem.hasActions() && customItem.getActions().containsKey(clickType)) {
            for (com.al3x.housing2.Action.Action houseAction : customItem.getActions().get(clickType)) {
                houseAction.execute(player, house, null);
            }
        }
    }
}

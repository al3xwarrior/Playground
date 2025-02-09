package com.al3x.housing2.Listeners;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HouseBrowserMenu;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
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
    public void moveToOffhand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().getName().equals("world")) {
            return;
        }

        ItemStack item = event.getOffHandItem();
        Item customItem = Item.fromItemStack(item);
        if (customItem != null) {
            ClickType type = ClickType.SWAP_OFFHAND;
            new ActionExecutor("event", customItem.getActions().get(type)).execute(player, housesManager.getHouse(player.getWorld()), event);
            if (event.isCancelled()) {
                return;
            }
        }

        ItemStack mainHandItem = event.getMainHandItem();
        Item customMainHandItem = Item.fromItemStack(mainHandItem);
        if (customMainHandItem != null) {
            ClickType type = ClickType.SWAP_OFFHAND;
            new ActionExecutor("event", customMainHandItem.getActions().get(type)).execute(player, housesManager.getHouse(player.getWorld()), event);
            if (event.isCancelled()) {
                return;
            }
        }
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        HousingWorld house = housesManager.getHouse(player.getWorld());

        ItemStack item = e.getItem();

        executeCustomItem(player, e.getItem(), e.getAction(), e);

        // In Lobby
        if (item != null && player.getWorld().equals(Bukkit.getWorld("world"))) {
            ItemMeta itemMeta = item.getItemMeta();
            Material itemType = item.getType();
            String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getItemMeta().getItemName();
            NbtItemBuilder nbt = new NbtItemBuilder(item);

            // Browser
            if (name.equals("§aHousing Browser §7(Right-Click)")) {
                new HouseBrowserMenu(player, housesManager).open();
            }
            if (name.equals("§aMy Houses §7(Right-Click)")) {
                new MyHousesMenu(main, player, player).open();
            }
            if (name.equals("§aRandom House §7(Right-Click)")) {
                HousingWorld randomHouse = housesManager.getRandomPublicHouse();
                if (randomHouse != null) {
                    randomHouse.sendPlayerToHouse(player);
                } else {
                    player.sendMessage(colorize("&cThere are no public houses available!"));
                }
            }
            return;
        }

        if (house == null) return;

        // Click block
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            // Trash Can
            if (house.trashCanAtLocation(e.getClickedBlock().getLocation()) && e.getClickedBlock().getType().equals(Material.BARRIER)) {
                player.openInventory(Bukkit.createInventory(player, 27, colorize("&cTrash Can")));
                return;
            }

            // Holding an item
            if (item != null) {
                ItemMeta itemMeta = item.getItemMeta();
                Material itemType = item.getType();
                String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getItemMeta().getItemName();
                NbtItemBuilder nbt = new NbtItemBuilder(item);

                Block block = e.getClickedBlock();
                boolean ownerOfHouse = house != null && house.getOwnerUUID().equals(player.getUniqueId());

                if (name.equals("§aNPC") && itemType.equals(Material.PLAYER_HEAD) && house.hasPermission(player, Permissions.ITEM_NPCS)) {
                    e.setCancelled(true);
                    house.createNPC(player, block.getLocation().add(new Vector(0.5, 1, 0.5)));
                }

                if (name.equals("§aHologram") && itemType.equals(Material.NAME_TAG) && house.hasPermission(player, Permissions.ITEM_HOLOGRAM)) {
                    e.setCancelled(true);
                    house.createHologram(player, block.getLocation().add(new Vector(0.5, 2, 0.5)));
                }
            }
        }

        // Click air
        if (e.getItem() != null && e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            NbtItemBuilder nbt = new NbtItemBuilder(item);

            // Cookies
            if (nbt.getBoolean("housing_cookie")) {
                e.setCancelled(true);
                if (house == null) return;
                main.getCookieManager().giveCookie(player, house);
                return;
            }

        }
    }

    private void executeCustomItem(Player player, ItemStack item, Action action, Cancellable event) {
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
            ActionExecutor executor = new ActionExecutor("item");
            executor.addActions(customItem.getActions().get(clickType));
            executor.execute(player, house, null);
        }
    }


    public static ItemStack inventoryInteractorItem(Material material) {
        ItemStack item = ItemBuilder.create(material)
                .name("&eInventory Interactor")
                .description("Place this item in a custom menu to allow players to click an item in their inventory and run actions using the item.")
                .extraLore("")
                .build();

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "inventoryInteractor"), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);

        return item;
    }
}

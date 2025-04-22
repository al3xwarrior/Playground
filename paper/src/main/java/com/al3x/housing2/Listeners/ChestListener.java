package com.al3x.housing2.Listeners;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ContainerUtils;
import com.al3x.housing2.Utils.ContainerUtils.ContainerLock;
import com.al3x.housing2.Utils.ContainerUtils.ContainerMode;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.al3x.housing2.Utils.ContainerUtils.getMainChest;
import static com.al3x.housing2.Utils.ContainerUtils.getMainContainerFromHolder;
import static com.al3x.housing2.Utils.Color.colorize;

public class ChestListener implements Listener {

    private final Main main;
    private static final Gson GSON = new Gson();

    public ChestListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || !e.hasBlock()) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        BlockState state = block.getState();
        if (!(state instanceof Container)) return;

        TileState tileState = (state instanceof Chest chest)
                ? (TileState) getMainChest(chest)
                : (TileState) state;

        PersistentDataContainer pdc = tileState.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(main, "container_lock");

        if (e.getPlayer().isSneaking()) {
            ContainerLock lockData = ContainerUtils.readLock(pdc, key, GSON);
            lockData.setStatus(lockData.getStatus().toggle());

            String json = GSON.toJson(lockData);
            pdc.set(key, PersistentDataType.STRING, json);
            tileState.update(true);
            e.setCancelled(true);
            return;
        }

        ContainerLock lockData = ContainerUtils.readLock(pdc, key, GSON);

        if (lockData.getStatus() == ContainerUtils.ContainerStatus.LOCKED) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(colorize("&cChest is locked."));
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory original = e.getInventory();
        if (original.getHolder() == null) return;

        Container mainChest = getMainContainerFromHolder(original.getHolder());

        if (mainChest == null) return;

        ContainerLock lock = getLock(mainChest);
        if (lock == null || lock.getMode() != ContainerMode.REPLICATING) return;

        // cancel the normal chest GUI
        e.setCancelled(true);

        // clone its contents
        ItemStack[] backup = original.getContents().clone();

        // prepare a new, temporary inventory with same size & title
        Component title = e.getView().title();  // the original window title
        Inventory fake = Bukkit.createInventory(null, original.getSize(), title);
        fake.setContents(backup);

        // open it for the player
        Player p = (Player) e.getPlayer();
        p.openInventory(fake);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof Chest chest)) return;
        Chest mainChest = getMainChest(chest);
        ContainerLock lock = getLock(mainChest);
        if (lock == null) return;

        if (lock.getMode() == ContainerMode.READ_ONLY) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(colorize("&cThis chest is read-only."));
        }
    }

    private ContainerLock getLock(Container container) {
        BlockState state = container.getBlock().getState();
        if (!(state instanceof TileState tileState)) return null;

        PersistentDataContainer pdc = tileState.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(main, "container_lock");
        return ContainerUtils.readLock(pdc, key, GSON);
    }
}

package com.al3x.housing2.Listeners;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ChestUtils;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

import static com.al3x.housing2.Utils.ChestUtils.getMainChest;
import static com.al3x.housing2.Utils.Color.colorize;

public class ChestListener implements Listener {

    private final Main main;
    private static final Gson GSON = new Gson();

    public ChestListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        // Only process right-click block interactions
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || !e.hasBlock()) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        BlockState state = block.getState();
        if (!(state instanceof Container)) return;

        main.getLogger().log(Level.FINE, "Inventory block with PDC: " + block.getType());

        // Determine the correct chest half (main side)
        TileState tileState = (state instanceof Chest chest)
                ? (TileState) getMainChest(chest)
                : (TileState) state;

        PersistentDataContainer pdc = tileState.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(main, "container_lock");

        // Sneak-click: toggle lock
        if (e.getPlayer().isSneaking()) {
            main.getLogger().log(Level.FINE, "Toggling container lock data!");

            ChestUtils.ContainerLock lockData = ChestUtils.readLock(pdc, key, GSON);

            lockData.setStatus(lockData.getStatus().toggle());

            String json = GSON.toJson(lockData);
            pdc.set(key, PersistentDataType.STRING, json);
            tileState.update(true);
            e.setCancelled(true);
            return;
        }

        // Regular right-click: respect lock status
        ChestUtils.ContainerLock lockData = ChestUtils.readLock(pdc, key, GSON);
        main.getLogger().log(Level.FINE, "Chest lock status: " + lockData.getStatus() + ", mode: " + lockData.getMode());

        if (lockData.getStatus() == ChestUtils.ContainerStatus.LOCKED) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(colorize("&cChest is locked."));
        }
    }
}

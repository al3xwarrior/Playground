package com.al3x.housing2.Listeners.HouseEvents;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import static com.al3x.housing2.Listeners.HouseEvents.SendExecution.sendEventExecution;

public class PlayerDropItem implements Listener {
    private HousesManager housesManager;

    public PlayerDropItem(HousesManager housesManager) {
        this.housesManager = housesManager;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        Item customItem = Item.fromItemStack(item);
        if (customItem != null && customItem.hasActions()) {
            ClickType type = (item.getAmount() > 1 ? ClickType.CONTROL_DROP : ClickType.DROP);
            new ActionExecutor("event", customItem.getActions().get(type)).execute(e.getPlayer(), housesManager.getHouse(e.getPlayer().getWorld()), e);
            if (e.isCancelled()) {
                return;
            }
        }

        sendEventExecution(housesManager, EventType.PLAYER_DROP_ITEM, e.getPlayer(), e);
    }
}

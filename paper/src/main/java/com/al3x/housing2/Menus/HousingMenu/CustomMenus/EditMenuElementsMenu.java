package com.al3x.housing2.Menus.HousingMenu.CustomMenus;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditMenuElementsMenu extends Menu {
    private Duple<ItemStack, List<Action>> holding = null;
    private final CustomMenu customMenu;

    public EditMenuElementsMenu(Player player, CustomMenu customMenu) {
        super(player, "Edit Menu: " + customMenu.getTitle(), 9 * customMenu.getRows());
        this.customMenu = customMenu;
    }

    @Override
    protected boolean isCancelled() {
        return false;
    }

    @Override
    public void setupItems() {
        Main main = Main.getInstance();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        List<Duple<ItemStack, List<Action>>> items = customMenu.getItems();

        for (int i = 0; i < 9 * customMenu.getRows(); i++) {
            Duple<ItemStack, List<Action>> duple = items.get(i);
            ItemStack item = null;
            if (duple != null && duple.getFirst() != null && duple.getFirst().getType() != Material.AIR) {
                item = duple.getFirst().clone();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore() == null ? new ArrayList<>() : new ArrayList<>(meta.getLore());
                lore.add("§b§m---------------------------------------");
                lore.add("§eLeft Click to pickup the item");
                lore.add("§eRight Click to edit actions");
                lore.add("§eShift Click to remove the item");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            int finalI = i;
            addItem(i, item, (e) -> handleItemClick(e, items, finalI, main, house));
        }
    }

    private void handleItemClick(InventoryClickEvent e, List<Duple<ItemStack, List<Action>>> items, int index, Main main, HousingWorld house) {
        if (e.isShiftClick()) {
            if (items.get(index) != null && items.get(index).getFirst() != null) {
                player.getInventory().addItem(items.get(index).getFirst());
            }
            items.set(index, null);
            setupItems();
            return;
        }

        if (handleInventoryInteractor(e)) return;

        if (e.getCursor() != null && !e.getCursor().getType().isAir()) {
            handleCursorItem(e, items, index);
        } else if (items.get(index) != null) {
            if (e.isRightClick()) {
                e.setCancelled(true);
                ActionsMenu menu = new ActionsMenu(main, player, house, items.get(index).getSecond(), this, "CustomMenuActions");
                menu.setEvent(EventType.INVENTORY_CLICK);
                menu.open();
            } else {
                holding = items.get(index);
                items.set(index, null);
                player.setItemOnCursor(holding.getFirst());
                e.setCancelled(true);
                setupItems();
            }
        }
    }

    private void handleCursorItem(InventoryClickEvent e, List<Duple<ItemStack, List<Action>>> items, int index) {
        e.setCancelled(true);
        ItemStack cursor = e.getCursor();
        ItemStack toEdit = cursor.clone();
        if (holding != null && cursor.equals(holding.getFirst())) {
            items.set(index, holding);
            holding = null;
        } else if (items.get(index) != null) {
            if (e.isRightClick()) toEdit.setAmount(1);
            items.set(index, new Duple<>(toEdit, items.get(index).getSecond()));
        } else {
            if (e.isRightClick()) toEdit.setAmount(1);
            items.set(index, new Duple<>(toEdit, new ArrayList<>()));
        }
        if (e.getCurrentItem() != null && !e.getCurrentItem().getType().isAir()) {
            holding = items.get(index);
        }
        if (e.isRightClick()) {
            cursor.setAmount(cursor.getAmount() - 1);
            player.setItemOnCursor(cursor);
        } else {
            player.setItemOnCursor(null);
        }
        setupItems();
    }

    private boolean handleInventoryInteractor(InventoryClickEvent event) {
        return false;
    }

    @Override
    public void handleDrag(InventoryDragEvent event) {
        updateItems(event.getNewItems());
    }

    private void updateItems(Map<Integer, ItemStack> newItems) {
        List<Duple<ItemStack, List<Action>>> items = customMenu.getItems();
        for (Map.Entry<Integer, ItemStack> slot : newItems.entrySet()) {
            int slotNum = slot.getKey();
            if (slotNum < 9 * customMenu.getRows()) {
                if (items.get(slotNum) != null && items.get(slotNum).getFirst() != null && items.get(slotNum).getFirst().getType() != Material.AIR) {
                    if (items.get(slotNum).getFirst().isSimilar(slot.getValue())) {
                        items.set(slotNum, new Duple<>(slot.getValue(), items.get(slotNum).getSecond()));
                    }
                } else {
                    items.set(slotNum, new Duple<>(slot.getValue(), new ArrayList<>()));
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), this::setupItems, 1);
    }

    @Override
    public void handleClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getTopInventory().equals(inventory)) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> new CustomMenuEditMenu(player, customMenu).open(), 1);
        }
    }
}
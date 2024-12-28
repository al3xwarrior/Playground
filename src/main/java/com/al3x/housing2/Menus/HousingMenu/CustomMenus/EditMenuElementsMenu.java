package com.al3x.housing2.Menus.HousingMenu.CustomMenus;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditMenuElementsMenu extends Menu {
    Duple<ItemStack, List<Action>> holding = null;
    CustomMenu customMenu;
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

        ArrayList<Duple<ItemStack, List<Action>>> items = customMenu.getItems();

        for (int i = 0; i < 9 * customMenu.getRows(); i++) {
            int finalI = i;

            Duple<ItemStack, List<Action>> duple = items.get(i);
            ItemStack item = null;
            if (duple != null && duple.getFirst() != null && duple.getFirst().getType() != Material.AIR) {
                item = duple.getFirst().clone();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<>(meta.getLore() == null ? new ArrayList<>() : meta.getLore());
                lore.add("§b§m---------------------------------------");
                lore.add("§eLeft Click to pickup the item");
                lore.add("§eRight Click to edit actions");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            addItem(i, item, (e) -> {
                if (e.getCursor() != null && !e.getCursor().getType().isAir()) {
                    e.setCancelled(true);
                    if (holding != null) {
                        items.set(finalI, holding);
                        holding = null;
                    } else if (items.get(finalI) != null) {
                        items.set(finalI, new Duple<>(e.getCursor(), items.get(finalI).getSecond()));
                    } else {
                        items.set(finalI, new Duple<>(e.getCursor(), new ArrayList<>()));
                    }
                    if (e.getCurrentItem() != null && !e.getCurrentItem().getType().isAir()) {
                        holding = items.get(finalI);
                    }
                    player.setItemOnCursor(null);
                    setupItems();
                } else if (items.get(finalI) != null) {
                    if (e.isRightClick()) {
                        e.setCancelled(true);
                        ActionsMenu menu = new ActionsMenu(main, player, house, items.get(finalI).getSecond(), this, null);
                        menu.setEvent(EventType.INVENTORY_CLICK);
                        menu.open();
                    } else {
                        holding = items.get(finalI);
                        items.set(finalI, null);

                        player.setItemOnCursor(holding.getFirst());
                        e.setCancelled(true);
                        setupItems();
                    }
                }
            });
        }
    }

//    @Override
//    public void handleClose(InventoryCloseEvent event) { //I think this may need to be called in the next tick :shrug: tho
//        new CustomMenuEditMenu(player, customMenu).open();
//    }
}

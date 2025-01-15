package com.al3x.housing2.Menus;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Instances.CustomMenu;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.removeColor;

public class CustomMenuViewer extends Menu {
    CustomMenu customMenu;

    BukkitTask task;
    public CustomMenuViewer(Player player, CustomMenu customMenu) {
        super(player, customMenu.getTitle(), 9 * customMenu.getRows());
        this.customMenu = customMenu;
    }

    @Override
    public void open() {
        super.open();

        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::setupItems, 0, customMenu.getRefreshRate());
    }

    @Override
    public void setupItems() {
        clearItems();
        Main main = Main.getInstance();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());

        for (int i = 0; i < 9 * customMenu.getRows(); i++) {
            int finalI = i;
            if (customMenu.getItems().get(i) == null) {
                continue;
            }
            ItemStack item = customMenu.getItems().get(i).getFirst().clone();
            ItemMeta meta = item.getItemMeta();
            meta.displayName(StringUtilsKt.housingStringFormatter(meta.getDisplayName(), house, player));
            ArrayList<Component> lore = new ArrayList<>();
            List<String> oldLore = new ArrayList<>(meta.getLore() == null ? new ArrayList<>() : meta.getLore());
            for (String line : oldLore) {
                lore.add(StringUtilsKt.housingStringFormatter(line, house, player));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
            addItem(i, item, (e) -> {
                new ActionExecutor(customMenu.getItems().get(finalI).getSecond()).execute(player, house, e);
            });
        }
    }

    @Override
    public void handleClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getTopInventory().equals(inventory)) {
            task.cancel();
        }
    }
}

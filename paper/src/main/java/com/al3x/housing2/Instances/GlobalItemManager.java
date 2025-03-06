package com.al3x.housing2.Instances;

import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.GlobalItem;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NbtItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GlobalItemManager {
    private static final List<GlobalItem> items = new ArrayList<>();

    public static void registerItem(GlobalItem item) {
        items.add(item);
    }

    public static void unregisterItem(GlobalItem item) {
        items.remove(item);
    }

    public static GlobalItem getItem(String id) {
        if (id == null) {
            return null;
        }
        for (GlobalItem item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static GlobalItem getItem(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        NbtItemBuilder builder = new NbtItemBuilder(stack);
        String id = builder.getString("global_item");
        return getItem(id);
    }

    public static List<GlobalItem> getItems() {
        return items;
    }

    public static void clearItems() {
        items.clear();
    }

    private static void run(PlayerInteractEvent event, BiConsumer<HousingWorld, Player> consumer) {
        if (event.getPlayer() == null) {
            return;
        }

        if (Main.getInstance().getHousesManager().getHouse(event.getPlayer().getWorld()) == null) {
            return;
        }

        consumer.accept(Main.getInstance().getHousesManager().getHouse(event.getPlayer().getWorld()), event.getPlayer());
    }

    static {
        // Register an example item
        GlobalItemManager.registerItem(new GlobalItem("action_button", ItemBuilder.create(Material.STONE_BUTTON).name("&aAction Button"), event -> {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                run(event, (house, player) -> {
                    if (player.getGameMode() == GameMode.ADVENTURE) {
                        player.sendMessage("§cYou cannot create an Action Button in Adventure Mode");
                        return;
                    }
                    if (event.getInteractionPoint() != null) {
                        house.addActionButton(event.getInteractionPoint());
                        player.sendMessage("§aSuccessfully created Action Button");
                    } else {
                        player.sendMessage("§cYou must be looking at a block to run this action");
                    }
                });
            }
        }));
    }
}

package com.al3x.housing2.Menus;

import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public class ItemSelectMenu extends Menu {
    private final Player player;
    public final Menu back;
    public final Consumer<ItemStack> consumer;
    private int easteregg = 0;
    public ItemSelectMenu(Player player, Menu back, Consumer<ItemStack> consumer) {
        super(player, "&eSelect an item", 4*9);
        this.player = player;
        this.back = back;
        this.consumer = consumer;
    }

    @Override
    public void initItems() {
        for (int i = 0; i < 27; i++) {
            ItemBuilder builder = new ItemBuilder();
            builder.material(Material.GRAY_STAINED_GLASS_PANE);
            builder.name("&bSelect an item");
            builder.description("Select an item from your inventory!");
            addItem(i, builder.build(), (e) -> {
                if (e.getClick() == ClickType.MIDDLE) consumer.accept(null);
            });
        }

        //back
        ItemBuilder backBuilder = new ItemBuilder();
        backBuilder.material(Material.ARROW);
        backBuilder.name("&aGo Back");
        backBuilder.description("To " + back.getTitle());
        addItem(31, backBuilder.build(), back::open);

        //Info
        ItemBuilder infoBuilder = new ItemBuilder();
        infoBuilder.material(Material.PAPER);
        infoBuilder.name("&eInfo");
        infoBuilder.description("Middle click any item to set that item to Not Set.");
        addItem(32, infoBuilder.build(), () -> {});
    }
}

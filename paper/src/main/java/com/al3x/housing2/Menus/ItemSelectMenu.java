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
        super(player, "&eSelect an item", 5*9);
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
            addItem(i, builder.build(), () -> {});
        }

        // air
        ItemBuilder airBuilder = new ItemBuilder();
        airBuilder.material(Material.WHITE_STAINED_GLASS);
        airBuilder.name("&eSet to Air");
        airBuilder.description("Click here to set the item to air.\n\n&cBe careful when using this, it can cause players to get stuck!");
        addItem(37, airBuilder.build(), () -> {
            ItemStack airStack = ItemStack.of(Material.AIR);
            consumer.accept(airStack);
        });

        //back
        ItemBuilder backBuilder = new ItemBuilder();
        backBuilder.material(Material.ARROW);
        backBuilder.name("&aGo Back");
        backBuilder.description("To " + back.getTitle());
        addItem(40, backBuilder.build(), back::open);

        //unset item
        ItemBuilder infoBuilder = new ItemBuilder();
        infoBuilder.material(Material.GLASS);
        infoBuilder.name("&eUnset Value");
        infoBuilder.description("Click here to unset the item value.");
        addItem(36, infoBuilder.build(), () -> {
            consumer.accept(null);
        });
    }
}

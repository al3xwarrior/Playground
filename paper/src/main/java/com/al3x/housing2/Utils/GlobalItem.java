package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.GlobalItemManager;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GlobalItem {
    public String id;
    public ItemBuilder builder;
    public Consumer<PlayerInteractEvent> runnable;

    public GlobalItem(String id, ItemBuilder builder, Consumer<PlayerInteractEvent> runnable) {
        this.id = id;
        this.builder = builder;
        this.runnable = runnable;
    }

    public void run(PlayerInteractEvent event) {
        runnable.accept(event);
    }

    public ItemBuilder getBuilder() {
        return builder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBuilder(ItemBuilder builder) {
        this.builder = builder;
    }

    public ItemStack build() {
        ItemStack stack = this.builder.build();
        NbtItemBuilder builder = new NbtItemBuilder(stack);
        builder.setString("global_item", this.id);
        builder.build();
        return stack;
    }
}

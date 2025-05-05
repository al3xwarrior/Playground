package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.IntegerProperty;
import com.al3x.housing2.Action.Properties.ItemStackProperty;
import com.al3x.housing2.Action.Properties.LocationProperty;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Serialization;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;

@Getter
@Setter
@ToString
public class DropItemAction extends Action {
    public DropItemAction() {
        super(
                ActionEnum.DROP_ITEM,
                "Drop Item",
                "Drops an item at a specified location.",
                Material.DROPPER,
                List.of("dropItem")
        );

        getProperties().addAll(List.of(
                new ItemStackProperty(
                        "item",
                        "Item",
                        "The item to drop"
                ),
                new LocationProperty(
                        "location",
                        "Location",
                        "The location to drop the item at"
                ).setValue("INVOKERS_LOCATION"),
                new BooleanProperty(
                        "drop_naturally",
                        "Drop Naturally",
                        "When enabled, the item will be dropped naturally in the world, having some slight randomness to its location"
                ).setValue(true),
                new BooleanProperty(
                        "item_merging",
                        "Item Merging",
                        "When enabled, the item will not merge with other items on the ground immediately"
                ).setValue(true),
                new BooleanProperty(
                        "prioritize_player",
                        "Prioritize Player",
                        "When enabled, the player the action is executed on will be prioritized for picking up the item"
                ).setValue(false),
                new BooleanProperty(
                        "fallback_to_inventory",
                        "Fallback to Inventory",
                        "When enabled, items will be put into the player's inventory if they cannot be dropped in the world"
                ).setValue(false),
                new BooleanProperty(
                        "display_name",
                        "Display Name",
                        "When enabled, the item will display its name above it"
                ).setValue(false),
                new IntegerProperty(
                        "pickup_delay",
                        "Pickup Delay",
                        "The delay in ticks before the item can be picked up by players"
                ).setValue(20)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        ItemStack stack = getValue("item", ItemStack.class);

        if (stack == null) return OutputType.ERROR;

        Location loc = getProperty("location", LocationProperty.class).getLocation(player, house, player.getLocation(), player.getEyeLocation());

        AtomicBoolean executed = new AtomicBoolean(false);

        AtomicInteger dropppedItems = new AtomicInteger();
        List<Entity> entities = house.getWorld().getEntities();
        boolean itemMerging = getValue("item_merging", Boolean.class);
        entities.stream().filter(entity -> entity instanceof Item).forEach(entity -> {
            dropppedItems.getAndIncrement();
            Item item = (Item) entity;
            if (item.getItemStack().isSimilar(stack) && itemMerging && !executed.get()) {
                ItemStack newItemStack = stack.clone();
                newItemStack.setAmount(item.getItemStack().getAmount() + stack.getAmount());
                item.setItemStack(newItemStack);
                executed.set(true);
            }
        });

        if (executed.get()) {
            return OutputType.ERROR;
        }

        if (dropppedItems.get() > 200) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(stack);
            }
            return OutputType.ERROR;
        }

        Item itemEntity = null;
        boolean dropNaturally = getValue("drop_naturally", Boolean.class);
        if (dropNaturally)
            itemEntity = house.getWorld().dropItemNaturally(loc, stack);
        else
            itemEntity = house.getWorld().dropItem(loc, stack);

        int pickupDelay = getValue("pickup_delay", Integer.class);
        boolean showName = getValue("display_name", Boolean.class);
        itemEntity.setPickupDelay(pickupDelay);
        itemEntity.setCustomNameVisible(showName);

        return OutputType.SUCCESS;
    }
    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

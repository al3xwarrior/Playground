package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.OutputType;
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
    private ItemStack item = null;
    private String customLocation = null;
    private Locations location = Locations.INVOKERS_LOCATION;
    private boolean dropNaturally = true;
    private boolean itemMerging = true;
    private boolean prioritizePlayer = false;
    private boolean fallbackToInventory = false;
    private boolean showName = false;
    private int pickupDelay = 20;

    public DropItemAction() {
        super(
                "drop_item_action",
                "Drop Item",
                "Drops an item at a specified location.",
                Material.DROPPER,
                List.of("dropItem")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "item",
                        "Item",
                        "The item to drop",
                        ActionProperty.PropertyType.ITEM
                ).setValue(item),
                new ActionProperty(
                        "location",
                        "Location",
                        "The location to drop the item at",
                        ActionProperty.PropertyType.ENUM,
                        Locations.class,
                        this::locationConsumer
                ).setValue(location),
                new ActionProperty(
                        "drop_naturally",
                        "Drop Naturally",
                        "When enabled, the item will be dropped naturally in the world, having some slight randomness to its location",
                        ActionProperty.PropertyType.BOOLEAN
                ).setValue(dropNaturally),
                new ActionProperty(
                        "item_merging",
                        "Item Merging",
                        "When enabled, the item will not merge with other items on the ground immediately",
                        ActionProperty.PropertyType.BOOLEAN
                ).setValue(itemMerging),
                new ActionProperty(
                        "prioritize_player",
                        "Prioritize Player",
                        "When enabled, the player the action is executed on will be prioritized for picking up the item",
                        ActionProperty.PropertyType.BOOLEAN
                ).setValue(prioritizePlayer),
                new ActionProperty(
                        "fallback_to_inventory",
                        "Fallback to Inventory",
                        "When enabled, items will be put into the player's inventory if they cannot be dropped in the world",
                        ActionProperty.PropertyType.BOOLEAN
                ).setValue(fallbackToInventory),
                new ActionProperty(
                        "display_name",
                        "Display Name",
                        "When enabled, the item will display its name above it",
                        ActionProperty.PropertyType.BOOLEAN
                ).setValue(showName),
                new ActionProperty(
                        "pickup_delay",
                        "Pickup Delay",
                        "The delay in ticks before the item can be picked up by players",
                        ActionProperty.PropertyType.INT
                ).setValue(pickupDelay)
        ));
    }

    public BiFunction<InventoryClickEvent, Object, Boolean> locationConsumer(HousingWorld house, Menu backMenu, Player player) {
        return (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
                (coords, location) -> {
                    if (location == CUSTOM) {
                        customLocation = coords;
                    } else {
                        customLocation = null;
                    }
                    this.location = location;
                    if (location == PLAYER_LOCATION) {
                        customLocation = player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ();
                    }
                    backMenu.open();
                }
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (item == null || location == null) return OutputType.ERROR;

        Location loc = null;
        switch (location) {
            case INVOKERS_LOCATION -> loc = player.getLocation();
            case HOUSE_SPAWN -> loc = house.getSpawn();
            case CUSTOM, PLAYER_LOCATION -> {
                loc = getLocationFromString(player, house, customLocation);
                if (loc == null) {
                    return OutputType.ERROR;
                }

                if (loc.getX() > 255) {
                    loc.setX(255);
                }
                if (loc.getZ() > 255) {
                    loc.setY(255);
                }
                if (loc.getY() > 255) {
                    loc.setY(255);
                }
            }
        }

        AtomicBoolean executed = new AtomicBoolean(false);

        AtomicInteger dropppedItems = new AtomicInteger();
        List<Entity> entities = house.getWorld().getEntities();
        entities.stream().filter(entity -> entity instanceof Item).forEach(entity -> {
            dropppedItems.getAndIncrement();
            Item item = (Item) entity;
            if (item.getItemStack().isSimilar(this.item) && itemMerging && !executed.get()) {
                ItemStack newItemStack = this.item.clone();
                newItemStack.setAmount(item.getItemStack().getAmount() + this.item.getAmount());
                item.setItemStack(newItemStack);
                executed.set(true);
            }
        });

        if (executed.get()) {
            return OutputType.ERROR;
        }

        if (dropppedItems.get() > 200) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            }
            return OutputType.ERROR;
        }

        Item itemEntity = null;
        if (dropNaturally)
            itemEntity = house.getWorld().dropItemNaturally(loc, item);
        else
            itemEntity = house.getWorld().dropItem(loc, item);

        itemEntity.setPickupDelay(pickupDelay);
        itemEntity.setCustomNameVisible(showName);

        return OutputType.SUCCESS;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        try {
            item = Serialization.itemStackFromBase64((String) data.get("item"));
        } catch (IOException e) {
            e.printStackTrace();
            Main.getInstance().getLogger().warning("Failed to load item from base64 string");
        }
        customLocation = (String) data.get("customLocation");
        try {
            location = Locations.valueOf((String) data.get("location"));
        } catch (IllegalArgumentException e) {
            location = Locations.INVOKERS_LOCATION;
        }
        dropNaturally = (boolean) data.get("dropNaturally");
        itemMerging = (boolean) data.get("itemMerging");
        prioritizePlayer = (boolean) data.get("prioritizePlayer");
        fallbackToInventory = (boolean) data.get("fallbackToInventory");
        showName = (boolean) data.get("showName");

        // ender dont look at this!!
        Object o = data.get("pickupDelay");
        if (o instanceof Double) {
            pickupDelay = ((Double) o).intValue();
        } else {
            pickupDelay = (int) data.get("pickupDelay");
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

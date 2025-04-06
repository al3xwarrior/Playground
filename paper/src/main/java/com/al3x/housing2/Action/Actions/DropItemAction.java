package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StackUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.al3x.housing2.Enums.Locations.CUSTOM;
import static com.al3x.housing2.Enums.Locations.PLAYER_LOCATION;

public class DropItemAction extends Action {
    private ItemStack item;
    private String customLocation;
    private Locations location;
    private boolean dropNaturally;
    private boolean itemMerging;
    private boolean prioritizePlayer;
    private boolean fallbackToInventory;
    private boolean showName;
    private int pickupDelay;

    public DropItemAction() {
        super("Drop Item Action");
        this.item = null;
        this.customLocation = null;
        this.location = Locations.INVOKERS_LOCATION;
        this.dropNaturally = true;
        this.itemMerging = true;
        this.prioritizePlayer = false;
        this.fallbackToInventory = false;
        this.showName = false;
        this.pickupDelay = 20;
    }

    @Override
    public String toString() {
        return "DropItemAction{" +
                "item=" + item +
                ", customLocation='" + customLocation + '\'' +
                ", location=" + location +
                ", dropNaturally=" + dropNaturally +
                ", itemMerging=" + itemMerging +
                ", prioritizePlayer=" + prioritizePlayer +
                ", fallbackToInventory=" + fallbackToInventory +
                ", showName=" + showName +
                ", pickupDelay=" + pickupDelay +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.DROPPER);
        builder.name("&eDrop Item");
        builder.info("&eSettings", "");
        builder.info("Item", (item == null ? "&cNone" : "&6" + item.getType()));
        builder.info("Location", "&a" + (location == CUSTOM ? customLocation : location));
        builder.info("Drop Naturally", "&a" + dropNaturally);
        builder.info("Item Merging", "&a" + itemMerging);
        builder.info("Prioritize Player", "&a" + prioritizePlayer);
        builder.info("Fallback to Inventory", "&a" + fallbackToInventory);
        builder.info("Show Name", "&a" + showName);
        builder.info("Pickup Delay", "&a" + pickupDelay);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DROPPER);
        builder.name("&aDrop Item");
        builder.description("Drop an item at a specified location");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("item", ItemBuilder.create((item == null ? Material.BOOK : item.getType()))
                        .name("&aItem")
                        .description("Select a item to drop")
                        .info("&7Current Value", "")
                        .info(null, (item == null ? "&cNone" : "&6" + StackUtils.getDisplayName(item))),
                        ActionEditor.ActionItem.ActionType.ITEM
                ),
                new ActionEditor.ActionItem("location",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eLocation")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (location == CUSTOM ? customLocation : location))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Locations.values(), Material.COMPASS,
                        (event, o) -> getCoordinate(event, o, customLocation, house, backMenu,
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
                        )
                ),
                new ActionEditor.ActionItem("dropNaturally",
                        ItemBuilder.create((this.dropNaturally ? Material.LIME_DYE : Material.RED_DYE))
                                .name("&aDrop Naturally")
                                .description("When enabled, the item will be dropped naturally in the world, having some slight randomness to its location")
                                .info("&7Current Value", "")
                                .info(null, dropNaturally ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("itemMerging",
                        ItemBuilder.create((this.itemMerging ? Material.LIME_DYE : Material.RED_DYE))
                                .name("&aDisable Item Merging")
                                .description("When enabled, the item will not merge with other items on the ground immediately.")
                                .info("&7Current Value", "")
                                .info(null, itemMerging ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("prioritizePlayer",
                        ItemBuilder.create((this.prioritizePlayer ? Material.LIME_DYE : Material.RED_DYE))
                                .name("&aDrop Naturally")
                                .description("When enabled, the player the action is executed on will be prioritized for picking up the item")
                                .info("&7Current Value", "")
                                .info(null, prioritizePlayer ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("fallbackToInventory",
                        ItemBuilder.create((this.fallbackToInventory ? Material.LIME_DYE : Material.RED_DYE))
                                .name("&aFallback to Inventory")
                                .description("When enabled, items will be put into the player's inventory if they cannot be dropped in the world")
                                .info("&7Current Value", "")
                                .info(null, fallbackToInventory ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("showName",
                        ItemBuilder.create((this.showName ? Material.LIME_DYE : Material.RED_DYE))
                                .name("&aShow Name")
                                .description("When enabled, the item will display its name above it")
                                .info("&7Current Value", "")
                                .info(null, showName ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                ),
                new ActionEditor.ActionItem("pickupDelay",
                        ItemBuilder.create(Material.CLOCK)
                                .name("&aPickup Delay")
                                .description("The amount of time in ticks before the item can be picked up")
                                .info("&7Current Value", "")
                                .info(null, "&a" + pickupDelay)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT
                )

        );
        return new ActionEditor(4, "&eDrop Item Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (item == null || location == null) return OutputType.ERROR;

        Location loc = null;
        switch (location) {
            case INVOKERS_LOCATION ->
                    loc = player.getLocation();
            case HOUSE_SPAWN ->
                    loc = house.getSpawn();
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

        if (dropppedItems.get() > Main.getInstance().getConfig().getInt("droppedItemLimit", 200)) {
            if (fallbackToInventory && player.getInventory().firstEmpty() != -1) {
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("item", Serialization.itemStackToBase64(item));
        data.put("customLocation", customLocation);
        data.put("location", location.name());
        data.put("dropNaturally", dropNaturally);
        data.put("itemMerging", itemMerging);
        data.put("prioritizePlayer", prioritizePlayer);
        data.put("fallbackToInventory", fallbackToInventory);
        data.put("showName", showName);
        data.put("pickupDelay", pickupDelay);
        return data;
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

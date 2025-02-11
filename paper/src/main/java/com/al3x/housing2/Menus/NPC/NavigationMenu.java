package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Enums.NavigationType;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class NavigationMenu extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    public NavigationMenu(Main main, Player player, HousingNPC housingNPC) {
        super(player, colorize("&7Edit NPC"), 4 * 9);
        this.main = main;
        this.player = player;
        this.housesManager = main.getHousesManager();
        this.house = housesManager.getHouse(player.getWorld());
        this.housingNPC = housingNPC;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemBuilder modeBuilder = ItemBuilder.create(Material.COMPASS)
                .name("&aMode")
                .info("&eCurrent Value", "")
                .info(null, "&6" + housingNPC.getNavigationType().name())
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
        addItem(10, modeBuilder.build(), () -> {
            List<Duple<NavigationType, ItemBuilder>> modes = new ArrayList<>();
            for (NavigationType type : NavigationType.values()) {
                modes.add(new Duple<>(type, ItemBuilder.create(type.getMaterial()).name("&6" + type.name())));
            }

            new PaginationMenu<>(main, "&eSelect a Mode", modes, player, house, this, (mode) -> {
                housingNPC.setNavigationType(mode);
                setupItems();
                open();
            }).open();
        });
        NavigationType type = housingNPC.getNavigationType();

        if (type != NavigationType.STATIONARY) {
            ItemBuilder speedBuilder = ItemBuilder.create(Material.SUGAR)
                    .name("&aSpeed")
                    .info("&eCurrent Value", "")
                    .info(null, "&a" + housingNPC.getSpeed())
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
            addItem(11, speedBuilder.build(), () -> {
                player.sendMessage(colorize("&eEnter the speed of the NPC (0.1 - 10.0)"));
                openChat(Main.getInstance(), String.valueOf(housingNPC.getSpeed()), (message) -> {
                    try {
                        double speed = Double.parseDouble(message);
                        if (speed < 0.1 || speed > 10.0) {
                            player.sendMessage(colorize("&cSpeed must be between 0.1 and 10.0!"));
                            return;
                        }
                        housingNPC.setSpeed(speed);
                        housingNPC.getCitizensNPC().getNavigator().getDefaultParameters().speedModifier((float) speed);
                        Bukkit.getScheduler().runTask(Main.getInstance(), this::open);
                    } catch (NumberFormatException e) {
                        player.sendMessage(colorize("&cInvalid number!"));
                    }
                });
            });
        }

        if (type == NavigationType.WANDER) {
            Waypoints waypoints = housingNPC.getCitizensNPC().getOrAddTrait(Waypoints.class);
            if (housingNPC.getPreviousNavigationType() != NavigationType.WANDER) {
                housingNPC.setNavigationType(NavigationType.WANDER);
                waypoints.setWaypointProvider("wander");
                waypoints.getCurrentProvider().setPaused(false);
            }

            WanderWaypointProvider wander = (WanderWaypointProvider) waypoints.getCurrentProvider();
            //delay
            ItemBuilder delayBuilder = ItemBuilder.create(Material.CLOCK)
                    .name("&aDelay")
                    .info("&eCurrent Value", "")
                    .info(null, "&a" + ((WanderWaypointProvider) housingNPC.getCitizensNPC().getOrAddTrait(Waypoints.class).getCurrentProvider()).getDelay())
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
            addItem(12, delayBuilder.build(), () -> {
                player.sendMessage(colorize("&eEnter the delay in ticks before the NPC moves to a new location"));
                openChat(Main.getInstance(), String.valueOf(wander.getDelay()), (message) -> {
                    try {
                        int delay = Integer.parseInt(message);
                        wander.setDelay(delay);
                        Bukkit.getScheduler().runTask(Main.getInstance(), this::open);
                    } catch (NumberFormatException e) {
                        player.sendMessage(colorize("&cInvalid number!"));
                    }
                });
            });

            //x,y range
            ItemBuilder rangeBuilder = ItemBuilder.create(Material.COMPASS)
                    .name("&aRange")
                    .info("&eCurrent Value", "")
                    .info(null, "&a" + wander.getXRange() + ", " + wander.getYRange())
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
            addItem(13, rangeBuilder.build(), () -> {
                player.sendMessage(colorize("&eEnter the x and y range separated by a comma that the NPC can wander (x,y)"));
                openChat(Main.getInstance(), wander.getXRange() + ", " + wander.getYRange(), (message) -> {
                    String[] split = message.split(",");
                    if (split.length == 2) {
                        try {
                            int xRange = Integer.parseInt(split[0].trim());
                            int yRange = Integer.parseInt(split[1].trim());
                            wander.setXYRange(xRange, yRange);
                            Bukkit.getScheduler().runTask(Main.getInstance(), this::open);
                        } catch (NumberFormatException e) {
                            player.sendMessage(colorize("&cInvalid number!"));
                        }
                    } else {
                        player.sendMessage(colorize("&cInvalid format!"));
                    }
                });
            });
        }

        if (type == NavigationType.STATIONARY) {
            if (housingNPC.getPreviousNavigationType() != NavigationType.STATIONARY) {
                housingNPC.setNavigationType(NavigationType.STATIONARY);
                Waypoints waypoints = housingNPC.getCitizensNPC().getOrAddTrait(Waypoints.class);
                waypoints.getCurrentProvider().setPaused(true);
            }
        }

        if (type == NavigationType.PATH) {
            if (housingNPC.getPreviousNavigationType() != NavigationType.PATH) {
                housingNPC.setNavigationType(NavigationType.PATH);
                Waypoints waypoints = housingNPC.getCitizensNPC().getOrAddTrait(Waypoints.class);
                waypoints.setWaypointProvider("linear");
                waypoints.getCurrentProvider().setPaused(false);
            }
            //Give the player a path item so they can edit the path
            ItemBuilder pathBuilder = ItemBuilder.create(Material.DIAMOND_AXE)
                    .name("&aPath")
                    .description("&bRight click &7a block too add a point\n\n&bRight click &7air to remove last point")
                    .lClick(ItemBuilder.ActionType.GIVE_ITEM);
            addItem(12, pathBuilder.build(), () -> {
                ItemBuilder pathItem = ItemBuilder.create(Material.DIAMOND_AXE)
                        .name("&aPath")
                        .description("&bRight click &7a block too add a point\n\n&bRight click &7air to remove last point");
                ItemStack itemStack = pathItem.build();
                //add nbt data to the item
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(main, "isPath");
                    container.set(key, PersistentDataType.BOOLEAN, true);

                    key = new NamespacedKey(main, "npc");
                    container.set(key, PersistentDataType.INTEGER, housingNPC.getNpcID());
                    itemStack.setItemMeta(meta);

                    player.getInventory().addItem(itemStack);
                }
            });
        }

        //back
        ItemBuilder backBuilder = ItemBuilder.create(Material.ARROW)
                .name("&cBack")
                .description("Go back to the NPC menu");
        addItem(31, backBuilder.build(), () -> {
            new NPCMenu(main, player, housingNPC).open();
        });
    }
}

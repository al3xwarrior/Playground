package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.HypixelLoreFormatter;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.order;

public class MyHousesMenu extends Menu {

    private Main main;
    private OfflinePlayer target;
    private Player player;
    private HousesManager housesManager;

    public MyHousesMenu(Main main, Player player, OfflinePlayer target) {
        super(player, colorize((player.getUniqueId().equals(target.getUniqueId()) ? "My Houses" : target.getName() + "'s Houses") ), 27);
        this.main = main;
        this.target = target;
        this.player = player;
        this.housesManager = main.getHousesManager();
    }

    @Override
    public void setupItems() {
        // only valid slot for now is 13


        if (!housesManager.playerHasHouse(target) && target.equals(player)) {
            addItem(13, ItemBuilder.create(Material.OAK_BUTTON)
                    .name("&e&oCreate a House!")
                    .build(), () -> {

                // This should never happen and most likely shoudnt even be here, but just in case!
                if (housesManager.playerHasHouse(target)) {
                    player.sendMessage(colorize("&cYou already have a house!"));
                    return;
                }

                player.sendMessage(colorize("&eCreating your house..."));
                player.closeInventory();
                HousingWorld newHouse = housesManager.createHouse(player, HouseSize.XLARGE);
                player.sendMessage(colorize("&aYour house has been created!"));
                newHouse.sendPlayerToHouse(player);
            });
            return;
        } else if (!housesManager.playerHasHouse(target)) {
            addItem(13, ItemBuilder.create(Material.BARRIER)
                    .name("&c&oThis player does not have a house!")
                    .build());
            return;
        }

        List<String> houseIDs = housesManager.getPlayerHouses().get(target.getUniqueId());
        if (houseIDs == null) {
            player.sendMessage(colorize("&cThis player does not have a house!"));
            return;
        }

//        int[] slots = getSlots(houseIDs.size());
        int[] slots = {11, 13, 15};

        for (int i = 0; i < slots.length; i++) {
            if (i >= houseIDs.size()) {
                addItem(slots[i], ItemBuilder.create(Material.OAK_BUTTON)
                        .name("&e&oCreate a House!")
                        .build(), () -> {
                    if (houseIDs.size() >= 3) {
                        player.sendMessage(colorize("&cYou have reached the maximum amount of houses!"));
                        return;
                    }
                    player.sendMessage(colorize("&eCreating your house..."));
                    player.closeInventory();
                    HousingWorld newHouse = housesManager.createHouse(player, HouseSize.XLARGE);
                    player.sendMessage(colorize("&aYour house has been created!"));
                    newHouse.sendPlayerToHouse(player);
                });
                continue;
            }
            HouseData house = housesManager.getHouseData(houseIDs.get(i));

            if (house == null) {
                addItem(slots[i], ItemBuilder.create(Material.OAK_BUTTON)
                        .name("&e&oCreate a House!")
                        .build(), () -> {
                    if (houseIDs.size() >= 3) {
                        player.sendMessage(colorize("&cYou have reached the maximum amount of houses!"));
                        return;
                    }
                    player.sendMessage(colorize("&eCreating your house..."));
                    player.closeInventory();
                    HousingWorld newHouse = housesManager.createHouse(player, HouseSize.XLARGE);
                    player.sendMessage(colorize("&aYour house has been created!"));
                    newHouse.sendPlayerToHouse(player);
                });
                continue;
            }

            final HousingWorld[] world = {housesManager.getConcurrentLoadedHouses().get(houseIDs.get(i))};

            long houseCreationDate = house.getTimeCreated();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(houseCreationDate);
            String formattedDate = formatter.format(date);

            String hIcon = house.getIcon() == null ? "OAK_DOOR" : house.getIcon();
            ItemStack item;
            if (Material.getMaterial(hIcon) != null) {
                item = new ItemStack(Material.getMaterial(hIcon));
            } else {
                try {
                    item = Serialization.itemStackFromBase64(hIcon);
                } catch (IOException e) {
                    item = new ItemStack(Material.OAK_DOOR);
                }
            }

            List<Component> lore = new ArrayList<>(HypixelLoreFormatter.hypixelLore(house.getDescription(), new ArrayList<>(), new ArrayList<>(), false, 28));

            lore.add(Component.empty());
            lore.add(Component.text("§7Created: §a" + formattedDate));
            lore.add(Component.empty());
            lore.add(Component.text("§7Players: §a" + (world[0] != null ? world[0].getWorld().getPlayers().size() : 0)));
            lore.add(Component.text("§7Cookies: §6" + house.getCookies()));

            lore.add(Component.empty());
            lore.add(Component.text("§7Privacy: §a" + HousePrivacy.valueOf(house.getPrivacy())));
            lore.add(Component.empty());
            lore.add(Component.text("§eClick to join!"));
            if (player.hasPermission("housing.admin")) {
                lore.add(Component.text("§cShift-click to Join! §4[ADMIN]"));
            }
            if (house.getOwnerID().equals(player.getUniqueId().toString())) {
                lore.add(Component.text("§eRight-click to edit!"));
            }
            if (player.hasPermission("housing.admin")) {
                lore.add(Component.text("§8" + house.getHouseID()));
            }

            ItemMeta meta = item.getItemMeta();
            meta.displayName(StringUtilsKt.housingStringFormatter("§a" + house.getHouseName()));
            meta.lore(lore);
            item.setItemMeta(meta);

            addItem(slots[i], item, () -> {
                if (HousePrivacy.valueOf(house.getPrivacy()) != HousePrivacy.PUBLIC) {
                    if (!house.getOwnerID().equals(player.getUniqueId().toString())) {
                        player.sendMessage(colorize("&cThis house is private!"));
                        return;
                    }
                }
                if (world[0] != null) {
                    world[0].sendPlayerToHouse(player);
                } else {
                    world[0] = housesManager.loadHouse(target, house.getHouseID());
                    world[0].sendPlayerToHouse(player);
                }
            }, () -> {
                if (!house.getOwnerID().equals(player.getUniqueId().toString())) {
                    return;
                }
                if (world[0] != null) {
                    new EditHouseMenu(main, player, world[0], house).open();
                } else {
                    player.sendMessage(colorize("&cYou can only edit your house from inside your house, by doing /home"));
                }

            }, () -> {
                if (player.hasPermission("housing.admin")) {
                    player.sendMessage(colorize("&cEntering house in &4Admin Mode&c!"));
                    if (world[0] != null) {
                        world[0].sendPlayerToHouse(player, true);
                    } else {
                        world[0] = housesManager.loadHouse(target, house.getHouseID());
                        world[0].sendPlayerToHouse(player, true);
                    }
                    return;
                }
            });
        }
    }

    private int[] getSlots(int amount) {
        //Basically if there is only 1 house, it will be in the middle, if there are 2, they will be on the sides, if there are 3, they will be in the middle and on the sides, etc.
        int[] slots = new int[amount];
        int middle = 13;
        int offset = 2;

        for (int i = 0; i < amount; i++) {
            slots[i] = middle + (i - amount / 2) * offset;
        }
        return slots;
    }
}

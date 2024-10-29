package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class MyHousesMenu extends Menu {

    private Main main;
    private OfflinePlayer target;
    private Player player;
    private HousesManager housesManager;

    public MyHousesMenu(Main main, Player player, OfflinePlayer target) {
        super(player, colorize((target.getUniqueId().equals(target.getUniqueId()) ? "My Houses" : target.getName() + "'s Houses") ), 27);
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
                }

                player.sendMessage(colorize("&eCreating your house..."));
                HousingWorld newHouse = housesManager.createHouse(player, HouseSize.LARGE);
                player.sendMessage(colorize("&aYour house has been created!"));
                newHouse.sendPlayerToHouse(player);
            });
            return;
        } else if (!housesManager.playerHasHouse(target)) {
            addItem(13, ItemBuilder.create(Material.BARRIER)
                    .name("&c&oThis player does not have a house!")
                    .build(), () -> {
            });
            return;
        }

        List<String> houseIDs = housesManager.getPlayerHouses().get(target.getUniqueId());
        if (houseIDs == null) {
            player.sendMessage(colorize("&cThis player does not have a house!"));
            return;
        }
        int[] slots = getSlots(houseIDs.size());

        for (int i = 0; i < houseIDs.size(); i++) {
            HouseData house = housesManager.getHouseData(houseIDs.get(i));

            if (house == null) {
                continue;
            }

            final HousingWorld[] world = {housesManager.getConcurrentLoadedHouses().get(houseIDs.get(i))};

            long houseCreationDate = house.getTimeCreated();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(houseCreationDate);
            String formattedDate = formatter.format(date);

            addItem(slots[i], ItemBuilder.create(Material.valueOf(house.getIcon()))
                    .name(colorize(house.getHouseName()))
                    .description(colorize("&7" + house.getDescription() +
                            "\n\n&7Created: &a" + formattedDate +
                            "\n\n&7Players: &a" + (world[0] != null ? world[0].getGuests() : 0) +
                            "\n&7Cookies: &6" + house.getCookies() +
                            "\n\n&7Privacy: " + HousePrivacy.valueOf(house.getPrivacy()).asString() +
                            "\n\n&eClick to Join!" + //WHY ARENT YOU USING THE LCLICK AND RCLICK ACTIONS?????
                            (house.getOwnerID().equals(player.getUniqueId().toString()) ? colorize("\n&eRight Click to Manage!") : "")))
                    .punctuation(false)
                    .build(), () -> {
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
                new EditHouseMenu(main, player, main.getHousesManager().getHouse(player)).open();
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

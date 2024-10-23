package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.al3x.housing2.Utils.Color.colorize;

public class MyHousesMenu extends Menu {

    private Main main;
    private Player player;
    private HousesManager housesManager;

    public MyHousesMenu(Main main, Player player) {
        super(player, colorize("&7My Houses"), 27);
        this.main = main;
        this.player = player;
        this.housesManager = main.getHousesManager();
    }

    @Override
    public void setupItems() {
        // only valid slot for now is 13

        HousingWorld house = housesManager.getHouse(player);

        if (house == null) {
            addItem(13, ItemBuilder.create(Material.OAK_BUTTON)
                    .name("&e&oCreate a House!")
                    .build(), () -> {

                // This should never happen and most likely shoudnt even be here, but just in case!
                if (housesManager.playerHasHouse(player)) {
                    player.sendMessage(colorize("&cYou already have a house!"));
                }

                player.sendMessage(colorize("&eCreating your house..."));
                HousingWorld newHouse = housesManager.createHouse(player, HouseSize.LARGE);
                player.sendMessage(colorize("&aYour house has been created!"));
                newHouse.sendPlayerToHouse(player);
            });
        } else {
            long houseCreationDate = house.getTimeCreated();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(houseCreationDate);
            String formattedDate = formatter.format(date);

            addItem(13, ItemBuilder.create(house.getIcon())
                    .name(colorize(house.getName()))
                    .description(colorize("&7" + house.getDescription() +
                            "\n\n&7Created: &a" + formattedDate +
                            "\n\n&7Players: &a" + house.getGuests() +
                            "\n&7Cookies: &6" + house.getCookies() +
                            "\n\n&7Privacy: " + ((house.getPrivacy().equals(HousePrivacy.PUBLIC)) ? "&aPublic" : "&cPrivate" +
                            "\n\n&eClick to Join!") +
                            "\n&eRight Click to Manage!")).punctuation(false)
                    .build(), () -> {
                house.sendPlayerToHouse(player);
            }, () -> {
                new EditHouseMenu(main, player, main.getHousesManager().getHouse(player)).open();
            });
        }
    }
}

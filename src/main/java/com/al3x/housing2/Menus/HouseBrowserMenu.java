package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class HouseBrowserMenu extends Menu{

    private Player player;
    private HousesManager housesManager;

    public HouseBrowserMenu(Player player, HousesManager housesManager) {
        super(player, "&cHousing Browser", 54);
        this.player = player;
        this.housesManager = housesManager;
        setupItems();
    }

    @Override
    public void setupItems() {
        List<HousingWorld> houses = housesManager.getLoadedHouses();

        for (int i = 0; i < (Math.min(houses.size(), 44)); i++) {
            HousingWorld house = houses.get(i);
            ItemStack icon = new ItemStack(house.getIcon());
            ItemMeta meta = icon.getItemMeta();

            meta.setDisplayName(colorize(house.getName()));
            meta.setLore(Arrays.asList(
                    colorize(house.getDescription()),
                    "",
                    colorize("&7Players: &a" + house.getGuests()),
                    colorize("&7Cookies: &6" + house.getCookies()),
                    "",
                    colorize("&eClick to Join!")
            ));

            icon.setItemMeta(meta);

            addItem(i, icon, () -> {
                house.sendPlayerToHouse(player);
            });
        }
    }
}

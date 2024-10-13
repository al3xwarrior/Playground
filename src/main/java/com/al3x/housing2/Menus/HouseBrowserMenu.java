package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setDisplayName(colorize(house.getName()));
            meta.setLore(Arrays.asList(
                    colorize(house.getDescription()),
                    "",
                    colorize("&2Players: &a" + house.getGuests()),
                    colorize("&6Cookies: &e" + house.getCookies()),
                    "",
                    colorize("&eClick to Join")
            ));

            head.setItemMeta(meta);

            addItem(i, head, () -> {
                house.sendPlayerToHouse(player);
            });
        }
    }
}

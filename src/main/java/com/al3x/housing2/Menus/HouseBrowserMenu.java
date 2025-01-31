package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class HouseBrowserMenu extends Menu {

    private Player player;
    private HousesManager housesManager;

    public HouseBrowserMenu(Player player, HousesManager housesManager) {
        super(player, "&7Housing Browser", 54);
        this.player = player;
        this.housesManager = housesManager;
        setupItems();
    }

    @Override
    public void setupItems() {
        List<HouseData> houses = getSortedHouses();
        if (houses.isEmpty()) {
            addItem(22, ItemBuilder.create(Material.BARRIER)
                    .name("&c&oThere are no public houses to display!")
                    .build(), () -> {});
            return;
        }
        houses = houses.stream().filter(house -> Objects.equals(house.getPrivacy(), "PUBLIC")).toList();

        for (int i = 0; i < (Math.min(houses.size(), 44)); i++) {
            HouseData house = houses.get(i);
            HousingWorld housingWorld = housesManager.getHouse(UUID.fromString(house.getHouseID()));

            ItemBuilder icon = ItemBuilder.create(Material.valueOf(house.getIcon() != null ? house.getIcon() : "OAK_DOOR"))
                    .name("&a" + house.getHouseName())
                    .description(colorize(house.getDescription()))
                    .info("&7Players", "&a" + (housingWorld != null ? housingWorld.getWorld().getPlayers().size() : 0))
                    .info("&7Cookies", "&6" + house.getCookies())
                    .lClick(ItemBuilder.ActionType.JOIN_YELLOW);

            addItem(i, icon.build(), () -> {
                if (housingWorld != null) {
                    housingWorld.sendPlayerToHouse(player);
                } else {
                    OfflinePlayer target = Main.getInstance().getServer().getOfflinePlayer(UUID.fromString(house.getOwnerID()));
                    if (target != null) {
                        HousingWorld world = housesManager.loadHouse(target, house.getHouseID());
                        if (world != null) {
                            world.sendPlayerToHouse(player);
                        } else {
                            player.sendMessage(colorize("&cWe couldn't load this house!"));
                        }
                    } else {
                        player.sendMessage(colorize("&cWe aren't sure what happened here!"));
                    }
                }
            });
        }
    }

    public static @NotNull List<HouseData> getSortedHouses() {
        List<HouseData> houses = new ArrayList<>(Main.getInstance().getHousesManager().getAllHouseData());
        houses.sort((house1, house2) -> {
            if (house1 == null || house2 == null) return (house1 == null) ? -1 : 1;
            HousingWorld housingWorld = Main.getInstance().getHousesManager().getHouse(UUID.fromString(house1.getHouseID()));
            HousingWorld housingWorld2 = Main.getInstance().getHousesManager().getHouse(UUID.fromString(house2.getHouseID()));

            if (housingWorld != null && housingWorld2 != null) {
                return Integer.compare(housingWorld.getWorld().getPlayers().size(), housingWorld2.getWorld().getPlayers().size());
            } //else check cookies
            return Integer.compare(house1.getCookies(), house2.getCookies());
        });
        return houses;
    }
}

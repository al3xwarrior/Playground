package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.*;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.ItemBuilder.order;

public class HouseBrowserMenu extends Menu {

    private Player player;
    private HousesManager housesManager;
    private int page;
    private String search = "";
    int[] avaliableSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};

    public HouseBrowserMenu(Player player, HousesManager housesManager) {
        super(player, "&7Housing Browser", 54);
        this.player = player;
        this.housesManager = housesManager;
        this.page = 1;
        setupItems();
    }

    @Override
    public void initItems() {
        PaginationList<HouseData> paginationList = getHouses();
        List<HouseData> houseList = paginationList.getPage(page);

        //houses = houses.stream().filter(house -> Objects.equals(house.getPrivacy(), "PUBLIC")).toList();

        if (houseList == null || houseList.isEmpty()) {
            addItem(22, ItemBuilder.create(Material.BARRIER)
                    .name("&c&oThere are no public houses to display!")
                    .build(), () -> {});
            return;
        }

        for (int i = 0; i < houseList.size(); i++) {
            HouseData house = houseList.get(i);
            HousingWorld housingWorld = housesManager.getHouse(UUID.fromString(house.getHouseID()));

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


            ItemBuilder icon = ItemBuilder.create(item.getType())
                    .name("&a" + house.getHouseName())
                    .description(colorize(house.getDescription()))
                    .info("&7Owner", "&e" + house.getOwnerName())
                    .info("&7Players", "&a" + (housingWorld != null ? housingWorld.getWorld().getPlayers().size() : 0))
                    .info("&7Cookies", "&6" + house.getCookies())
                    .lClick(ItemBuilder.ActionType.JOIN_YELLOW);

            if (player.hasPermission("housing2.admin")) icon.extraLore("&8" + house.getHouseID());

            HashMap<ClickType, ItemBuilder.ActionType> actions = icon.getActions();
            List<String> labels = new ArrayList<>();
            //Action labels
            //<color>Click to <action>!
            if (actions.containsKey(ClickType.LEFT) && actions.size() == 1) {
                labels.add(colorize( actions.get(ClickType.LEFT).getColor() + "Click to " + actions.get(ClickType.LEFT).toString() + "!"));
            } else {
                for (ClickType clickType : order) {
                    if (actions.containsKey(clickType)) {
                        labels.add(colorize(actions.get(clickType).getColor() + StringUtilsKt.formatCapitalize(clickType.name().toLowerCase()) + " Click to " + actions.get(clickType).toString() + "!"));
                    }
                }
            }

            List<Component> lore = new ArrayList<>(HypixelLoreFormatter.hypixelLore(house.getDescription(), icon.getInfo(), labels, false, 28));

            ItemMeta meta = item.getItemMeta();
            meta.displayName(StringUtilsKt.housingStringFormatter("§a" + house.getHouseName()));
            meta.lore(lore);
            item.setItemMeta(meta);

            addItem(i, item, () -> {
                if (housingWorld != null) {
                    housingWorld.sendPlayerToHouse(player);
                } else {
                    OfflinePlayer target = Main.getInstance().getServer().getOfflinePlayer(UUID.fromString(house.getOwnerID()));
                    HousingWorld world = housesManager.loadHouse(target, house.getHouseID());
                    if (world != null) {
                        world.sendPlayerToHouse(player);
                    } else {
                        player.sendMessage(colorize("&cWe couldn't load this house!"));
                    }
                }
            });
        }

        if (paginationList.getPageCount() > 1) {
            ItemBuilder forwardArrow = new ItemBuilder();
            forwardArrow.material(Material.ARROW);
            forwardArrow.name("&aNext Page");
            forwardArrow.description("&ePage " + (page + 1));
            addItem(53, forwardArrow.build(), () -> {
                if (page + 1 > paginationList.getPageCount()) return;
                page++;
                open();
            });
        }

        if (page > 1) {
            ItemBuilder backArrow = new ItemBuilder();
            backArrow.material(Material.ARROW);
            backArrow.name("&aPrevious Page");
            backArrow.description("&ePage " + (page - 1));
            addItem(45, backArrow.build(), () -> {
                if (page - 1 < 1) return;
                page--;
                open();
            });
        }
    }

    private PaginationList<HouseData> getHouses() {
        List<HouseData> housesArray = new ArrayList<>(HouseBrowserMenu.getSortedHouses());
        housesArray = housesArray.reversed();

        housesArray = housesArray.stream().filter(houseData -> Objects.equals(houseData.getPrivacy(), "PUBLIC")).toList();

        if (search != null) {
            housesArray = housesArray.stream().filter(houseData -> StringUtilsKt.removeStringFormatting(houseData.getHouseName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(housesArray, avaliableSlots.length);
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

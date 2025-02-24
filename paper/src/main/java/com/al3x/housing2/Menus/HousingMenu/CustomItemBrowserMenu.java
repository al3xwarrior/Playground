package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomItemBrowserMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;

    private int page = 1;

    private String search = "";

    public CustomItemBrowserMenu(Main main, Player player, HousingWorld house) {
        super(player, "&aCustom Items", 54);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    @Override
    public void setupItems() {

        PaginationList<CustomItem> paginationList = getItems();
        List<CustomItem> itemList = paginationList.getPage(page);

        addItem(49,
                ItemBuilder.create(Material.ARROW)
                        .name("&cGo Back")
                        .build()
                , () -> {
                    new HousingMenu(main, player, house).open();
                });

        if (page < paginationList.getPageCount()) {
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

        if (!Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
            addItem(22,
                    ItemBuilder.create(Material.BEDROCK)
                            .name("&cItemsAdder is not enabled")
                            .build());
            return;
        }

        if (itemList == null) {
            addItem(22,
                    ItemBuilder.create(Material.BEDROCK)
                            .name("&cNo items found")
                            .build());
            return;
        }

        for (int i = 0; i < itemList.size(); i++) {
            CustomItem item = itemList.get(i);
            ItemStack itemStack = item.getItem();
            addItem(i, itemStack, e -> {
                player.getInventory().addItem(itemStack);
            });
        }

    }

    private PaginationList<CustomItem> getItems() {
        List<CustomItem> itemsArray = new ArrayList<>();

        Plugin itemsAdder = Bukkit.getPluginManager().getPlugin("ItemsAdder");
        File[] files = new File(itemsAdder.getDataFolder() + "/contents/neighborhood/textures/items").listFiles();
        if (files == null) return new PaginationList<>(itemsArray, 45);
        List<File> filesList = new ArrayList<>(List.of(files));
        filesList.sort(Comparator.comparing(File::getName));
        for (int i = 0; i < filesList.size(); i++) {
            if (filesList.get(i) == null) break;

            File file = filesList.get(i);
            String name = file.getName().replace(".png", "");

            CustomStack customStack = CustomStack.getInstance(name);
            if (customStack == null) continue;
            ItemStack item = customStack.getItemStack();

            itemsArray.add(new CustomItem(name, item));
        }

        if (search != null) {
            itemsArray = itemsArray.stream().filter(i -> Color.removeColor(i.getName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(itemsArray, 45);
    }

    private class CustomItem {
        private String name;
        private ItemStack item;

        public CustomItem(String name, ItemStack item) {
            this.name = name;
            this.item = item;
        }

        public String getName() {
            return name;
        }

        public ItemStack getItem() {
            return item;
        }
    }

}
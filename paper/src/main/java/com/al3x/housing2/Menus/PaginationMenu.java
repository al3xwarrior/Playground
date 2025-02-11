package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.ANVIL;

public class PaginationMenu<E> extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private List<Duple<E, ItemBuilder>> items;
    private BiConsumer<InventoryClickEvent, E> con;
    private Menu backMenu;

    private int currentPage = 1;
    private String search;

    public PaginationMenu(Main main, String title, List<Duple<E, ItemBuilder>> items, Player player, HousingWorld house, Menu backMenu, BiConsumer<InventoryClickEvent, E> consumer) {
        super(player, colorize(title), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.items = items;
        this.con = consumer;
        this.backMenu = backMenu;
    }

    public PaginationMenu(Main main, String title, List<Duple<E, ItemBuilder>> items, Player player, HousingWorld house, Menu backMenu, Consumer<E> consumer) {
        super(player, colorize(title), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.items = items;
        this.con = (event, e) -> consumer.accept(e);
        this.backMenu = backMenu;
    }

    public PaginationMenu(Main main, String title, Player player, HousingWorld house) {
        super(player, colorize(title), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
    }

    public void setItems(List<Duple<E, ItemBuilder>> items) {
        this.items = items;
    }

    public void setConsumer(BiConsumer<InventoryClickEvent, E> consumer) {
        this.con = consumer;
    }

    public void setBackMenu(Menu backMenu) {
        this.backMenu = backMenu;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setHouse(HousingWorld house) {
        this.house = house;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        List<Duple<E, ItemBuilder>> newItems = items;
        if (search != null) {
            newItems = newItems.stream().filter(i -> Color.removeColor(i.getSecond().getName().toLowerCase()).contains(search.toLowerCase())).toList();
        }

        PaginationList<Duple<E, ItemBuilder>> paginationList = new PaginationList<>(newItems, slots.length);
        List<Duple<E, ItemBuilder>> pageItems = paginationList.getPage(currentPage);

        if (pageItems != null) {
            //I really shouldn't have made this, but I did :)
            for (int i = 0; i < pageItems.size(); i++) {
                Duple<E, ItemBuilder> something = pageItems.get(i);
                addItem(slots[i], something.getSecond().build(), (event) -> {
                    con.accept(event, something.getFirst());
                });
            }
        } else {
            addItem(22, new ItemBuilder().material(Material.BARRIER).name("&cNo Items Found").build());
        }

        // Search
        addItem(48, new ItemBuilder().material(ANVIL).name("&eSearch").punctuation(false)
                .description("&7Search for an option.\n\n&eCurrent Value:\n&7" + search)
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .rClick(ItemBuilder.ActionType.REMOVE_YELLOW)
                .build(), (e) -> {
            if (e.getClick().isRightClick()) {
                search = "";
                currentPage = 1;
                Bukkit.getScheduler().runTaskLater(main, this::open, 1L);
                return;
            }
            player.sendMessage(colorize("&ePlease enter the search term:"));
            openChat(main, search == null ? "" : search, (search) -> {
                this.search = search;
                currentPage = 1;
                Bukkit.getScheduler().runTaskLater(main, this::open, 1L);
            });
        });

        //Previous Page
        if (currentPage > 1) {
            addItem(45, ItemBuilder.create(Material.ARROW)
                    .name(colorize("&ePrevious Page"))
                    .description("&ePage " + (currentPage - 1))
                    .build(), () -> {
                if (currentPage < 1) return;
                currentPage--;
                open();
            });
        }

        //Next Page
        if (currentPage < paginationList.getPageCount()) {
            addItem(53, ItemBuilder.create(Material.ARROW)
                    .name(colorize("&eNext Page"))
                    .description("&ePage " + (currentPage + 1))
                    .build(), () -> {
                if (currentPage > paginationList.getPageCount()) return;
                currentPage++;
                open();
            });
        }

        //Back Button
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&cBack"))
                .description("To " + backMenu.getTitle())
                .build(), () -> {
            backMenu.open();
        });

    }
}

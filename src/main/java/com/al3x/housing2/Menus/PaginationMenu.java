package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

public class PaginationMenu<E> extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private List<Duple<E, ItemBuilder>> items;
    private Consumer<E> con;
    private Menu backMenu;

    private int currentPage = 1;

    public PaginationMenu(Main main, String title, List<Duple<E, ItemBuilder>> items, Player player, HousingWorld house, Menu backMenu, Consumer<E> consumer) {
        super(player, colorize(title), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.items = items;
        this.con = consumer;
        this.backMenu = backMenu;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<Duple<E, ItemBuilder>> paginationList = new PaginationList<>(items, slots.length);
        List<Duple<E, ItemBuilder>> page = paginationList.getPage(currentPage);

        //I really shouldn't have made this, but I did :)
        for (int i = 0; i < page.size(); i++) {
            Duple<E, ItemBuilder> something = page.get(i);
            addItem(slots[i], something.getSecond().build(), (event) -> {
                con.accept(something.getFirst());
                backMenu.open();
            });
        }

        //Previous Page
        if (currentPage > 1) {
            addItem(45, ItemBuilder.create(Material.ARROW)
                    .name(colorize("&ePrevious Page"))
                    .description("&ePage " + (currentPage - 1))
                    .build(), (event) -> {
                currentPage--;
                open();
            });
        }

        //Next Page
        if (currentPage < paginationList.getPageCount() - 1) {
            addItem(53, ItemBuilder.create(Material.ARROW)
                    .name(colorize("&eNext Page"))
                    .description("&ePage " + (currentPage + 1))
                    .build(), (event) -> {
                currentPage++;
                open();
            });
        }

        //Back Button
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&cBack"))
                .description("To " + backMenu.getTitle())
                .build(), (event) -> {
            backMenu.open();
        });

    }
}

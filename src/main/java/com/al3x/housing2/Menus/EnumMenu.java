package com.al3x.housing2.Menus;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.al3x.housing2.Utils.Color.colorize;

public class EnumMenu<E extends Enum<E>> extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private E[] enums;
    private Material material;
    private Consumer<E> con;
    private Menu backMenu;

    private int currentPage = 1;

    public EnumMenu(Main main, String title, E[] enums, Material material, Player player, HousingWorld house, Menu backMenu, Consumer<E> consumer) {
        super(player, colorize(title), 9 * 6);
        this.main = main;
        this.player = player;
        this.house = house;
        this.enums = enums;
        this.material = material;
        this.con = consumer;
        this.backMenu = backMenu;
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        PaginationList<E> paginationList = new PaginationList<>(Arrays.stream(enums).toList(), slots.length);
        List<E> page = paginationList.getPage(currentPage);

        //I really shouldn't have made this, but I did :)
        for (int i = 0; i < page.size(); i++) {
            E e = page.get(i);
            if (e instanceof Material) material = (Material) e;
            addItem(slots[i], ItemBuilder.create((material))
                    .name(colorize("&a" + e.name()))
                    .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                    .build(), (event) -> {
                con.accept(e);
            });
        }

        //Previous Page
        if (currentPage > 0) {
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
        addItem(49, ItemBuilder.create(Material.BARRIER)
                .name(colorize("&cBack"))
                .description("To " + backMenu.getTitle())
                .build(), (event) -> {
            backMenu.open();
        });

    }
}

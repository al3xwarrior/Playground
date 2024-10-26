package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.ANVIL;

public class EnumMenu<E extends Enum<E>> extends Menu {
    private Main main;
    private Player player;
    private HousingWorld house;
    private E[] enums;
    private Material material;
    private Consumer<E> con;
    private Menu backMenu;

    private int currentPage = 1;
    private String search;

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
        List<E> pageItems = paginationList.getPage(currentPage);

        if (search != null) {
            pageItems = pageItems.stream().filter(i -> Color.removeColor(i.name().toLowerCase()).contains(search.toLowerCase())).toList();
        }

        //I really shouldn't have made this, but I did :)
        if (pageItems != null) {
            for (int i = 0; i < pageItems.size(); i++) {
                E e = pageItems.get(i);
                if (e instanceof Material) material = (Material) e;
                if (e instanceof EnumMaterial) material = ((EnumMaterial) e).getMaterial();
                if (material == null) continue;
                if (material.equals(Material.AIR)) continue;
                addItem(slots[i], ItemBuilder.create((material))
                        .name(colorize("&a" + e.name()))
                        .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                        .build(), (event) -> {
                    con.accept(e);
                });
            }
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
            openChat(main, search, (search) -> {
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

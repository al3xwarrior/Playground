package com.al3x.housing2.Menus;

import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.ANVIL;

public class EnumMenu<E extends Enum<E>> extends Menu {
    private String title;
    private Main main;
    private Player player;
    private HousingWorld house;
    private E[] enums;
    private Material material;
    private Consumer<E> con;
    private Menu backMenu;

    private int currentPage = 1;
    private static HashMap<UUID, String> searchMap = new HashMap<>();

    public EnumMenu(Main main, String title, E[] enums, Material material, Player player, HousingWorld house, Menu backMenu, Consumer<E> consumer) {
        super(player, colorize(title + "(1/" + (getItems(enums, player, material).getPageCount()) + ")"), 9 * 6);
        this.title = title;
        this.main = main;
        this.player = player;
        this.house = house;
        this.enums = enums;
        this.material = material;
        this.con = consumer;
        this.backMenu = backMenu;
        searchMap.put(player.getUniqueId(), "");
    }

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(null, 9 * 6, colorize(title + " (" + currentPage + "/" + (getItems(enums, player, material).getPageCount()) + ")"));
        setupItems();
        if (MenuManager.getPlayerMenu(player) != null && MenuManager.getListener(player) != null) {
            AsyncPlayerChatEvent.getHandlerList().unregister(MenuManager.getListener(player));
        }
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void initItems() {
        clearItems();

        PaginationList<E> paginationList = getItems(enums, player, material);
        List<E> pageItems = paginationList.getPage(currentPage);

        //I really shouldn't have made this, but I did :)
        if (pageItems != null) {
            for (int i = 0; i < pageItems.size(); i++) {
                E e = pageItems.get(i);
                if (e instanceof Material) material = (Material) e;
                if (e instanceof EnumMaterial) material = ((EnumMaterial) e).getMaterial();
                if (material == null) continue;
                if (material.equals(Material.AIR)) continue;
                String name = e.name().replace("minecraft:", "");
                addItem(i, ItemBuilder.create((material))
                        .name(colorize("&a" + StringUtilsKt.formatCapitalize(name)))
                        .lClick(ItemBuilder.ActionType.SELECT_YELLOW)
                        .build(), () -> {
                    con.accept(e);
                });
            }
        }

        String search = searchMap.get(player.getUniqueId());
        // Search
        addItem(48, new ItemBuilder().material(ANVIL).name("&eSearch").punctuation(false)
                .description("&7Search for an option.\n\n&eCurrent Value:\n&7" + search)
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .rClick(ItemBuilder.ActionType.REMOVE_YELLOW)
                .build(), (e) -> {
            if (e.getClick().isRightClick()) {
                searchMap.put(player.getUniqueId(), "");
                currentPage = 1;
                Bukkit.getScheduler().runTaskLater(main, this::open, 1L);
                return;
            }
            player.sendMessage(colorize("&ePlease enter the search term:"));
            openChat(main, search, (s) -> {
                searchMap.put(player.getUniqueId(), s);
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
                if (currentPage <= 1) return;
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
                if (currentPage >= paginationList.getPageCount()) return;
                currentPage++;
                open();
            });
        }

        //Back Button
        addItem(49, ItemBuilder.create(Material.BARRIER)
                .name(colorize("&cBack"))
                .description("To " + backMenu.getTitle())
                .build(), () -> {
            backMenu.open();
        });

    }

    private static <T> PaginationList<T> getItems(T[] enums, Player player, Material defaultMaterial) {
        List<T> items = new ArrayList<>();
        Material material = defaultMaterial;
        for (T item: enums) {
            if (item instanceof Material) material = (Material) item;
            if (item instanceof EnumMaterial) material = ((EnumMaterial) item).getMaterial();
            if (material == null) continue;
            if (!material.isItem()) continue;
            if (material.equals(Material.AIR)) continue;
            items.add(item);
        }

        String search = searchMap.get(player.getUniqueId());
        if (search != null && !search.isEmpty()) {
            items = items.stream().filter(e -> Color.removeColor(e.toString().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        PaginationList<T> paginationList = new PaginationList<>(items, 36);

        return paginationList;
    }
}

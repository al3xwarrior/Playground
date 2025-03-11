package com.al3x.housing2.Menus.HousingMenu;

import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.MyHousesMenu;
import com.al3x.housing2.MineSkin.SkinData;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import me.arcaniax.hdb.object.head.Head;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.al3x.housing2.Utils.Color.colorize;

public class IndivisualHeadsMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private String category;
    private List<Head> heads;
    private int currentPage = 1;
    private final int[] slots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private String search = "";

    public IndivisualHeadsMenu(Main main, Player player, String categoryName, List<Head> heads, HousingWorld house) {
        super(player, "&8" + categoryName + " Heads", 54);
        this.main = main;
        this.player = player;
        this.house = house;
        this.category = categoryName;
        this.heads = heads;
        setupItems();
    }

    @Override
    public void initItems() {
        if (search.isEmpty()) {
            setTitle(colorize("&8" + category + " Heads (" + currentPage + "/" + (heads.size() / slots.length + 1) + ")"));
        } else {
            setTitle(colorize("&8" + category + " Heads (Search: " + search + ")"));
        }

        PaginationList<Head> paginationList = getHeads();
        List<Head> thisHeads = paginationList.getPage(currentPage);

        if (thisHeads != null) {
            for (int i = 0; i < thisHeads.size(); i++) {
                Head head = thisHeads.get(i);
                addItem(slots[i], head.getHead(), () -> {
                    player.getInventory().addItem(head.getHead());
                });
            }
        }

        if (currentPage < 1) currentPage = 1;
        if (currentPage > paginationList.getPageCount()) currentPage = paginationList.getPageCount();

        if (currentPage < paginationList.getPageCount()) {
            // Next Page Arrow
            addItem(53, ItemBuilder.create(Material.ARROW)
                    .name(colorize("&7Next Page"))
                    .build(), () -> {
                currentPage++;
                setupItems();
                open();
            });
        }

        if (currentPage > 1) {
            // Previous Page Arrow
            addItem(45, ItemBuilder.create(Material.ARROW)
                    .name(colorize("&7Previous Page"))
                    .build(), () -> {
                currentPage--;
                setupItems();
                open();
            });
        }

        //search
        ItemBuilder search = ItemBuilder.create(Material.COMPASS)
                .name("&7Search for a Head")
                .info("&eCurrent Value", "")
                .info(null, this.search)
                .description("&7Click to search for a head.")
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .rClick(ItemBuilder.ActionType.CLEAR_SEARCH);

        addItem(48, search.build(), (e) -> {
            if (e.getClick().isRightClick()) {
                this.search = "";
                currentPage = 1;
                setupItems();
                open();
                return;
            }

            player.sendMessage(colorize("&eEnter a search term for the head."));
            openChat(main, this.search, (message) -> {
                this.search = message;
                currentPage = 1;
                Bukkit.getScheduler().runTask(main, this::open);
            });
        });

        // Go Back Arrow
        addItem(49, ItemBuilder.create(Material.ARROW)
                .name(colorize("&cGo Back"))
                .build(), () -> {
            new HeadsMenu(main, player, house).open();
        });

    }

    private PaginationList<Head> getHeads() {
        List<Head> newHeads = new ArrayList<>(heads);
        if (!search.isEmpty()) {
            newHeads.removeIf(head -> !head.name.toLowerCase().contains(search.toLowerCase()));
        }
        return new PaginationList<>(newHeads, slots.length);
    }
}

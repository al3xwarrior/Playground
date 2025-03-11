package com.al3x.housing2.Menus.NPC;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.NPC.NPCMenu;
import com.al3x.housing2.MineSkin.SkinData;
import com.al3x.housing2.MineSkin.SkinResponse;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class SkinSelectorMenu extends Menu {
    private static Gson gson = new Gson();
    private static String LOADING = "eyJ0aW1lc3RhbXAiOjE1ODc4MjU0NzgwNDcsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1ODg4YWEyZDdlMTk5MTczYmEzN2NhNzVjNjhkZTdkN2Y4NjJiMzRhMTNiZTMyNDViZTQ0N2UyZjIyYjI3ZSJ9fX0=";
    private Main main;
    private Player player;
    private HousesManager housesManager;
    private HousingNPC housingNPC;

    private List<SkinData> previousSkins;
    private int page = 1;
    private String search = "";

    public SkinSelectorMenu(Main main, Player player, HousesManager housesManager, HousingNPC housingNPC) {
        super(player, "&7Change Skin (Page 1/1)", 54);
        this.main = main;
        this.player = player;
        this.housesManager = housesManager;
        this.housingNPC = housingNPC;
        this.previousSkins = HousingNPC.loadedSkins;
        setupItems();
    }

    @Override
    public String getTitle() {
        return "&7Change Skin (Page " + page + "/" + (getPage().getPageCount()) + ")";
    }

    @Override
    public void initItems() {
        clearItems();
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};
        PaginationList<SkinData> skins = getPage();
        List<SkinData> currentSkins = skins.getPage(page);

        if (currentSkins == null) {
            ItemBuilder loading = ItemBuilder.create(Material.BARRIER)
                    .name("&cNo skins found.")
                    .description("&7Please try a different search term.");
            addItem(22, loading.build());
        } else {
            for (int i = 0; i < currentSkins.size(); i++) {
                SkinData skin = currentSkins.get(i);
                ItemBuilder skinItem = ItemBuilder.create(Material.PLAYER_HEAD)
                        .skullTexture(skin.getTexture())
                        .name("&7" + (skin.getName() == null ? skin.getUuid() : skin.getName()))
                        .description("&7Click to select this skin.");
                addItem(slots[i] - 1, skinItem.build(), () -> {
                    housingNPC.setSkin(skin.getUuid());
                    new NPCMenu(main, player, housingNPC).open();
                });
            }
        }

        //Custom UUID
        ItemBuilder custom = ItemBuilder.create(Material.NAME_TAG)
                .name("&7Custom Skin")
                .info("&eCurrent Value", "")
                .info(null, housingNPC.getSkinUUID() == null ? "&cNONE" : housingNPC.getSkinUUID())
                .description("&7Click to enter a custom skin UUID.");
        addItem(47, custom.build(), () -> {
            player.sendMessage(colorize("&eEnter the UUID of the skin you want to use. You can find this at https://mineskin.org."));
            openChat(main, housingNPC.getSkinUUID() == null ? "" : housingNPC.getSkinUUID(), (message) -> {
                housingNPC.setSkin(message);
                Bukkit.getScheduler().runTask(main, () -> new NPCMenu(main, player, housingNPC).open());
            });
        });

        //search
        ItemBuilder search = ItemBuilder.create(Material.COMPASS)
                .name("&7Search for a skin")
                .info("&eCurrent Value", "")
                .info(null, this.search)
                .description("&7Click to search for a skin.")
                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                .rClick(ItemBuilder.ActionType.CLEAR_SEARCH);

        addItem(48, search.build(), (e) ->

        {
            if (e.getClick().isRightClick()) {
                this.search = "";
                page = 1;
                open();
                return;
            }

            player.sendMessage(colorize("&eEnter the name or uuid of the skin you want to search for."));
            openChat(main, this.search, (message) -> {
                this.search = message;
                page = 1;
                Bukkit.getScheduler().runTask(main, this::open);
            });
        });

        // Previous page
        if (page > 1) {
            ItemStack previousPage = new ItemStack(Material.ARROW);
            ItemMeta previousPageMeta = previousPage.getItemMeta();
            previousPageMeta.setDisplayName(colorize("&7Previous Page"));
            previousPage.setItemMeta(previousPageMeta);
            addItem(45, previousPage, () -> {
                if (page > 1) {
                    page--;
                    open();
                }
            });
        }

        // Next page
        if (page < skins.getPageCount()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            nextPageMeta.setDisplayName(colorize("&7Next Page"));
            nextPage.setItemMeta(nextPageMeta);
            addItem(53, nextPage, () -> {
                if (page < skins.getPageCount()) {
                    page++;
                    open();
                }
            });
        }

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(

                colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);

        addItem(49, backArrow, () ->

        {
            new NPCMenu(main, player, housingNPC).open();
        });
    }

    private PaginationList<SkinData> getPage() {
        List<SkinData> newSkins = new ArrayList<>(previousSkins);
        if (!search.isEmpty()) {
            String search = this.search.toLowerCase();
            newSkins = newSkins.stream().filter(i -> (i.getName() == null ? i.getUuid() : i.getName()).toLowerCase().contains(search)).toList();
        }
        return new PaginationList<>(newSkins, 21);
    }
}

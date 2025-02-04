package com.al3x.housing2.Menus.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.EnumMaterial;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.MenuManager;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.*;

public class ActionEnumMenu extends Menu {
    private Main main;
    private Action action;
    private Condition condition;
    private ActionEditor.ActionItem item;
    private Player player;
    private HousingWorld house;
    private HousingNPC housingNPC;
    private EventType event;
    private Menu backMenu;

    //1 is the new 0
    private int currentPage = 1;
    private String search = "";

    // Events
    public ActionEnumMenu(Action action, ActionEditor.ActionItem item, Main main, Player player, HousingWorld house, EventType event, Menu backMenu) {
        super(player, "§eSelect Option (" + 1 + "/" + getItems(item, "").getPageCount() + ")", 54);
        this.main = main;
        this.action = action;
        this.item = item;
        this.player = player;
        this.house = house;
        this.event = event;
        this.backMenu = backMenu;
    }

    // Events conditional
    public ActionEnumMenu(Condition condition, ActionEditor.ActionItem item, Main main, Player player, HousingWorld house, EventType event, Menu backMenu) {
        super(player, "§eSelect Option (" + 1 + "/" + getItems(item, "").getPageCount() + ")", 54);
        this.main = main;
        this.condition = condition;
        this.item = item;
        this.player = player;
        this.house = house;
        this.event = event;
        this.backMenu = backMenu;
    }

    private static Material[] STAT_OPERATION_MATERIALS = new Material[]{
            GREEN_STAINED_GLASS, // INCREASE
            RED_STAINED_GLASS, // DECREASE
            YELLOW_STAINED_GLASS,// SET
            ORANGE_STAINED_GLASS, // MULTIPLY
            BLUE_STAINED_GLASS, // DIVIDE
            MAGENTA_STAINED_GLASS, // MODULUS
            WHITE_STAINED_GLASS, // FLOOR
            BROWN_STAINED_GLASS, // ROUND
            FEATHER, // GET STAT
            BOOK, // CONCAT
            CHAIN, // INDEX_OF
            PAPER, // SET_STRING
            BREEZE_ROD, // LENGTH_OF
            SPYGLASS, // CHAT_AT
    };

    private static Material[] STRING_OPERATION_MATERIALS = new Material[]{
            GREEN_STAINED_GLASS, // CONCAT
            BROWN_STAINED_GLASS, // INDEX_OF
            YELLOW_STAINED_GLASS, //SET STRING
//            Material.RED_STAINED_GLASS, // REPLACE
//            Material.YELLOW_STAINED_GLASS, // SUBSTRING
//            Material.ORANGE_STAINED_GLASS, // TO_LOWER
//            Material.BLUE_STAINED_GLASS, // TO_UPPER
//            Material.MAGENTA_STAINED_GLASS, // TRIM
//            Material.WHITE_STAINED_GLASS, // LENGTH
//            Material.BOOK, // CHAR_AT
//            Material.CHAIN // SPLIT
    };

    @Override
    public void open() {
        this.inventory = Bukkit.createInventory(null, 54, "§eSelect Option (" + currentPage + "/" + getItems(item, search).getPageCount() + ")");
        setupItems();
        MenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void setupItems() {
        clearItems();
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        PaginationList<ItemBuilder> paginationList = getItems(item, search);
        List<ItemBuilder> pageItems = paginationList.getPage(currentPage);


        if (pageItems != null) {
            for (int i = 0; i < pageItems.size(); i++) {
                ItemBuilder itemBuilder = pageItems.get(i);
                if (item.getEnumClass() instanceof Sound[]) {
                    itemBuilder.rClick(ItemBuilder.ActionType.PLAY_SOUND);
                }
                int slot = slots[i] - 1;
                int finalI = i;
                addItem(slot, itemBuilder.build(), (e) -> {
                    if (e.getCurrentItem() == null) return;
                    if (!e.getCurrentItem().hasItemMeta()) return;
                    String name = e.getCurrentItem().getItemMeta().getDisplayName().replace(" ", "_").toUpperCase().replaceAll("§[A-F0-9]", "");

                    if (item.getEnumClass() instanceof Sound[] && e.getClick().isRightClick()) {
                        player.playSound(player.getLocation(), Sound.valueOf(name), 1, 1);
                        return;
                    }

                    try {
                        Field field;
                        if (action != null) {
                            field = action.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            if (item.getCustomRunnable() != null) {
                                if (!item.getCustomRunnable().apply(e, Enum.valueOf(field.getType().asSubclass(Enum.class), name))) return;
                            }
                            field.set(action, Enum.valueOf(field.getType().asSubclass(Enum.class), name));
                        } else if (condition != null) {
                            field = condition.getClass().getDeclaredField(item.getVarName());
                            field.setAccessible(true);
                            if (item.getCustomRunnable() != null) {
                                if (!item.getCustomRunnable().apply(e, Enum.valueOf(field.getType().asSubclass(Enum.class), name))) return;
                            }
                            field.set(condition, Enum.valueOf(field.getType().asSubclass(Enum.class), name));
                        }

                        player.sendMessage(colorize("&aUpdated " + item.getBuilder().getName() + " to " + pageItems.get(finalI).getName()));
                        if (backMenu != null) {
                            backMenu.open();
                            return;
                        }
                        player.sendMessage(colorize("&cError: No back menu found"));
                        player.closeInventory();
                    } catch (NoSuchFieldException | IllegalAccessException ex) {
                        if (action != null) {
                            Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                            player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                        } else {
                            Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + condition.getName());
                            player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + condition.getName()));
                        }
                    }
                });
            }
        }

        if (paginationList.getPageCount() > 1) {
            addItem(53, new ItemBuilder().material(ARROW)
                    .name("&eNext Page")
                    .description("&ePage " + (currentPage + 1))
                    .build(), () -> {
                if (currentPage >= paginationList.getPageCount()) return;
                currentPage++;
                open();
            });
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(ARROW)
                    .name("&ePrevious Page")
                    .description("&ePage " + (currentPage - 1))
                    .build(), () -> {
                if (currentPage <= 1) return;
                currentPage--;
                open();
            });
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

        addItem(49, new ItemBuilder().material(ARROW).name("&cGo Back")
                .description((backMenu != null) ? "&7Back to " + backMenu.getTitle() : "&7Back to previous menu")
                .build(), () -> {
            // not sure why this wasnt here? (prob bald) si
            if (backMenu != null) {
                backMenu.open();
                return;
            }
            player.sendMessage(colorize("&cError: No back menu found"));
            player.closeInventory();
        });
    }

    private static PaginationList<ItemBuilder> getItems(ActionEditor.ActionItem item, String search) {
        List<ItemBuilder> items = new ArrayList<>();
        Enum[] enumClass = item.getEnumClass();

        for (int i = 0; i < enumClass.length; i++) {
            Enum value = enumClass[i];
            String name = StringUtilsKt.formatCapitalize(value.toString());
            if (item.getEnumMaterial() != null && item.getEnumMaterial() != LEGACY_FEATHER) {
                items.add(new ItemBuilder().material(item.getEnumMaterial()).name("&e" + name));
            } else {
                //Are there better ways to do this? Probably, do I care? No
                if (value instanceof BossBar.Color)
                    items.add(new ItemBuilder().material(Color.fromColor((BossBar.Color) value)).name("&e" + name));

                if (value instanceof Material)
                    items.add(new ItemBuilder().material((Material) value).name("&e" + name));

                if (value instanceof EnumMaterial enumMaterial) {
                    items.add(new ItemBuilder().material(enumMaterial.getMaterial()).name("&e" + name));
                }
            }
        }

        if (item.getEnumMaterial() == LEGACY_FEATHER) {
            items.clear();
            for (int i = 0; i < STRING_OPERATION_MATERIALS.length; i++) {
                items.add(new ItemBuilder().material(STRING_OPERATION_MATERIALS[i]).name("&e" + StringUtilsKt.formatCapitalize(item.getEnumClass()[9 + i].toString())));
            }
        }

        if (search != null) {
            items = items.stream().filter(i -> Color.removeColor(i.getName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        return new PaginationList<>(items, 21);
    }
}

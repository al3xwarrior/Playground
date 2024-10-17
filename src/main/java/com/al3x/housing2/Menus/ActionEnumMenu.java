package com.al3x.housing2.Menus;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.PaginationList;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static org.bukkit.Material.ARROW;

public class ActionEnumMenu extends Menu {
    private Main main;
    private Action action;
    private ActionEditor.ActionItem item;
    private Player player;
    private HousingWorld house;
    private HousingNPC housingNPC;
    private EventType event;
    private Menu backMenu;

    //1 is the new 0
    private int currentPage = 1;

    // NPC
    public ActionEnumMenu(Action action, ActionEditor.ActionItem item, Main main, Player player, HousingWorld house, HousingNPC housingNPC) {
        super(player, "Select Option", 54);
        this.main = main;
        this.action = action;
        this.item = item;
        this.player = player;
        this.house = house;
        this.housingNPC = housingNPC;
    }

    // Events
    public ActionEnumMenu(Action action, ActionEditor.ActionItem item, Main main, Player player, HousingWorld house, EventType event) {
        super(player, "Select Option", 54);
        this.main = main;
        this.action = action;
        this.item = item;
        this.player = player;
        this.house = house;
        this.event = event;
    }

    @Override
    public void setupItems() {
        clearItems();
        List<ItemBuilder> items = new ArrayList<>();
        for (Enum value : item.getEnumClass()) {
            String name = StringUtilsKt.formatCapitalize(value.toString());
            if (item.getEnumMaterial() != null) {
                items.add(new ItemBuilder().material(item.getEnumMaterial()).name("&e" + name));
            } else {
                if (value instanceof BarColor) items.add(new ItemBuilder().material(Color.fromColor((BarColor) value)).name("&e" + name));
            }
        }
        int[] slots = new int[]{11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35};

        PaginationList<ItemBuilder> paginationList = new PaginationList<>(items, 21);
        List<ItemBuilder> pageItems = paginationList.getPage(currentPage);

        for (int i = 0; i < pageItems.size(); i++) {
            ItemBuilder itemBuilder = items.get(i);
            int slot = slots[i] - 1;
            int finalI = i;
            addItem(slot, itemBuilder.build(), (e) -> {
                if (e.getCurrentItem() == null) return;
                if (!e.getCurrentItem().hasItemMeta()) return;
                String name = e.getCurrentItem().getItemMeta().getDisplayName().replace(" ", "_").toUpperCase();
                try {
                    Field field = action.getClass().getDeclaredField(item.getVarName());
                    field.setAccessible(true);
                    field.set(action, Enum.valueOf(field.getType().asSubclass(Enum.class), name));

                    player.sendMessage(colorize("&aUpdated " + item.getBuilder().getName() + " to " + items.get(finalI).getName()));
                    if (housingNPC != null) {
                        new ActionEditMenu(action, main, player, house, housingNPC).open();
                        return;
                    }
                    if (event != null) {
                        new ActionEditMenu(action, main, player, house, event).open();
                        return;
                    }
                    player.sendMessage(colorize("&cError: No back menu found"));
                    player.closeInventory();
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    Bukkit.getLogger().warning("Failed to set field " + item.getVarName() + " in " + action.getName());
                    player.sendMessage(colorize("&cFailed to set field " + item.getBuilder().getName() + " in " + action.getName()));
                }
            });
        }

        if (paginationList.getPageCount() > 1) {
            addItem(53, new ItemBuilder().material(ARROW).name("&eNext Page").build(), (e) -> {
                currentPage++;
                setupItems();
            });
        }

        if (currentPage > 1) {
            addItem(45, new ItemBuilder().material(ARROW).name("&ePrevious Page").build(), (e) -> {
                currentPage--;
                setupItems();
            });
        }

        addItem(49, new ItemBuilder().material(ARROW).name("&cGo Back").build(), (e) -> {
            if (housingNPC != null) {
                new ActionEditMenu(action, main, player, house, housingNPC).open();
                return;
            }
            if (event != null) {
                new ActionEditMenu(action, main, player, house, event).open();
                return;
            }
            player.sendMessage(colorize("&cError: No back menu found"));
            player.closeInventory();
        });
    }
}

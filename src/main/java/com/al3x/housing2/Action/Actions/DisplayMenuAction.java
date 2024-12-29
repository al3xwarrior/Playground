package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.CustomMenuViewer;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class DisplayMenuAction extends Action {
    String menu;
    public DisplayMenuAction() {
        super("Display Menu Action");
    }

    public DisplayMenuAction(String menu) {
        super("Display Menu Action");
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "DisplayMenu Action{" +
                "layout=" + menu +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&eDisplay Menu");
        builder.info("&eSettings", "");
        builder.info("Custom Menu", (menu == null ? "&cNone" : "&6" + menu));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CHEST);
        builder.name("&aDisplay Menu");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("menu", ItemBuilder.create(Material.FILLED_MAP)
                        .name("&aMenu")
                        .info("&7Current Value", "")
                        .info(null, (menu == null ? "&cNone" : "&6" + menu)),
                        ActionEditor.ActionItem.ActionType.MENU
                )
        );
        return new ActionEditor(4, "&eDisplay Menu Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (menu == null) {
            return false;
        }
        house.getCustomMenus().stream().filter(customMenu -> customMenu.getTitle().equals(menu)).findFirst().ifPresent(customMenu -> {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> { //Make sure it runs on the main thread
                new CustomMenuViewer(player, customMenu).open();
            });
        });
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("menu", menu);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

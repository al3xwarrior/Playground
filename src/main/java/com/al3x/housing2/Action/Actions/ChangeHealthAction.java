package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingData.MoreStatData;
import com.al3x.housing2.Instances.HousingData.StatActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.*;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ChangeHealthAction extends HTSLImpl {
    private StatOperation mode;
    private String value;

    public ChangeHealthAction() {
        super("Change Health Action");
        mode = StatOperation.SET;
        value = "20";
    }

    @Override
    public String toString() {
        return "PlayerStatAction (mode: " + mode + ", value: " + value + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.APPLE);
        builder.name("&eChange Health");
        builder.info("&eSettings", "");
        builder.info("Mode", mode.name());
        builder.info("Value", value);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.APPLE);
        builder.name("&eChange Health");
        builder.description("Change the player's health by a specified amount.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&eChange Health Action Settings", new ArrayList<>());
        }
        List<ActionEditor.ActionItem> items = new ArrayList<>();

        items.add(new ActionEditor.ActionItem("mode",
                ItemBuilder.create(Material.APPLE)
                        .name("&eMode")
                        .info("&7Current Value", "")
                        .info(null, "&a" + mode.name())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, StatOperation.values(), null));
        items.add(new ActionEditor.ActionItem("value",
                ItemBuilder.create(Material.APPLE)
                        .name("&eValue")
                        .info("&7Current Value", "")
                        .info(null, "&a" + value.toString())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING
        ));
        return new ActionEditor(6, "&eChange Health Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value)) {
            return true;
        }
        double result = Double.parseDouble(value);
        player.setHealth(result);
        return true;
    }

    public StatOperation getMode() {
        return mode;
    }

    public void setMode(StatOperation mode) {
        this.mode = mode;
    }

    public String getValue() {
        return value;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("mode", mode.name());
        data.put("value", value);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = StatOperation.valueOf((String) data.get("mode"));
        value = (String) data.get("value");
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + keyword() + " " + mode.getAlternative() + " " + Color.removeColor(value.toString());
    }

    @Override
    public String keyword() {
        return "changeHealth";
    }
}

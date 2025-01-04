package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
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
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ChangeHealthAction extends Action {
    private static Gson gson = new Gson();

    private StatOperation mode;
    private StatValue value;

    public ChangeHealthAction() {
        super("Change Health Action");
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
        builder.info("Value", value.toString());

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
                (event, obj) -> {
                    if (event.getClick() == ClickType.MIDDLE) {
                        value.setExpression(!value.isExpression());
                        backMenu.open();
                        return true;
                    }

                    if (value.isExpression()) {
                        new ActionEditMenu(value, Main.getInstance(), backMenu.getOwner(), house, backMenu).open();
                    } else {
                        backMenu.getOwner().sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
                        backMenu.openChat(Main.getInstance(), value.getLiteralValue(), (value) -> {
                            this.value.setLiteralValue(value);
                            backMenu.getOwner().sendMessage(colorize("&aValue set to: &e" + value));
                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), backMenu::open, 1L);
                        });
                    }
                    return true;
                })
        );
        return new ActionEditor(6, "&eChange Health Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Double result = Stat.modifyDoubleIfDouble(mode, player.getHealth() + "", value.calculate(player, house));
        if (result != null) {
            player.setHealth(result);
        }
        return true;
    }

    public StatOperation getMode() {
        return mode;
    }

    public void setMode(StatOperation mode) {
        this.mode = mode;
    }

    public StatValue getValue() {
        return value;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("mode", mode.name());
        data.put("value", StatActionData.Companion.fromStatValue(value));
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = StatOperation.valueOf((String) data.get("mode"));
        value = gson.fromJson((String) data.get("value"), MoreStatData.class).toStatValue();
    }
}

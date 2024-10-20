package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.ExpressionOperation;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.google.gson.Gson;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatValue extends Action {

    private static final Gson gson = new Gson();
    private ExpressionOperation mode;
    private String literalValue;
    private StatValue value1;
    private StatValue value2;
    private boolean isExpression;

    public StatValue() {
        super("StatValue");
        this.literalValue = "1.0";
//            this.value1 = "Kills";
        this.mode = ExpressionOperation.INCREASE;
//            this.value2 = "1.0";
        this.isExpression = false;
    }


    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items;
        if (isExpression) {
            if (value1 == null) value1 = new StatValue();
            if (value2 == null) value2 = new StatValue();
            literalValue = null;
            items = Arrays.asList(
                    new ActionEditor.ActionItem("isExpression",
                            ItemBuilder.create((isExpression ? Material.LIME_DYE : Material.RED_DYE))
                                    .name("&aIs expression")
                                    .description("Stat set to expression or literal")
                                    .info("&7Current Value", "")
                                    .info(null, isExpression ? "&aExpression" : "&cLiteral"),
                            ActionEditor.ActionItem.ActionType.BOOLEAN
                    ),
                    new ActionEditor.ActionItem("value1",
                            ItemBuilder.create(Material.BOOK)
                                    .name("&eAmount 1")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + value1)
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.ACTION_SETTING
                    ),
                    new ActionEditor.ActionItem("mode",
                            ItemBuilder.create(Material.COMPASS)
                                    .name("&eMode")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + mode)
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.ENUM, ExpressionOperation.values(), null
                    ),
                    new ActionEditor.ActionItem("value2",
                            ItemBuilder.create(Material.BOOK)
                                    .name("&eAmount 2")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + value2)
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.ACTION_SETTING
                    )
            );
        } else {
            value1 = null;
            value2 = null;
            if(literalValue == null) literalValue = "1.0";
            items = Arrays.asList(
                    new ActionEditor.ActionItem("isExpression",
                            ItemBuilder.create((isExpression ? Material.LIME_DYE : Material.RED_DYE))
                                    .name("&aIs expression")
                                    .description("Stat set to expression or literal")
                                    .info("&7Current Value", "")
                                    .info(null, isExpression ? "&aExpression" : "&cLiteral"),
                            ActionEditor.ActionItem.ActionType.BOOLEAN
                    ),
                    new ActionEditor.ActionItem("literalValue",
                            ItemBuilder.create(Material.BOOK)
                                    .name("&eAmount")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + literalValue)
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.STRING
                    )
            );
        }

        return new ActionEditor(4, "&ePlayer Stat Action Settings", items);
    }

    public double calculate(Player player, HousingWorld world) throws NumberFormatException {
        if (!isExpression) return Double.parseDouble(HandlePlaceholders.parsePlaceholders(player, world, literalValue));
        switch (mode) {
            case INCREASE:
                return value1.calculate(player, world) + value2.calculate(player, world);
            case DECREASE:
                return value1.calculate(player, world) - value2.calculate(player, world);
            case MULTIPLY:
                return value1.calculate(player, world) * value2.calculate(player, world);
            case DIVIDE: default:
                return value1.calculate(player, world) / value2.calculate(player, world);
        }
    }

    public boolean requiresPlayer() {
        return true;
    }

    public String toString() {
        if (isExpression) return "[Expression]";
        else return literalValue;
    }

    public void createDisplayItem(ItemBuilder builder) {} // never in display
    public void createAddDisplayItem(ItemBuilder builder) {} // never created
    public boolean execute(Player player, HousingWorld house) {
        return false;
    } // never "executed"

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("value1", value1);
        data.put("mode", mode);
        data.put("value2", value2);
        return data;
    }

    // i mew over the slain bodies of all who doubted me
    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        isExpression = (boolean) data.get("isExpression");
        if(isExpression) {
            value1 = new StatValue();
            value1.fromData((HashMap<String, Object>) gson.fromJson(data.get("value1").toString(), HashMap.class), null);
            mode = ExpressionOperation.valueOf((String) data.get("mode"));
            value2 = new StatValue();
            value2.fromData((HashMap<String, Object>) gson.fromJson(data.get("value2").toString(), HashMap.class), null);
        } else {
            literalValue = String.valueOf(data.get("literalValue"));
        }

    }
}

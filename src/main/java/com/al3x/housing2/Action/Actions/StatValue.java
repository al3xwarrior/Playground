package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.google.gson.Gson;
import com.al3x.housing2.Utils.ItemBuilder;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatValue extends Action {
    private boolean isGlobal;
    private boolean isExpression;
    private String literalValue;
    private StatOperation mode;
    private StatValue value1;
    private StatValue value2;

    public StatValue(boolean isGlobal) {
        super("StatValue");
        this.literalValue = "1.0";
//            this.value1 = "Kills";
        this.mode = StatOperation.INCREASE;
//            this.value2 = "1.0";
        this.isExpression = false;
        this.isGlobal = isGlobal;
    }

    public StatValue(boolean isGlobal, boolean isExpression, String literalValue, StatOperation mode, StatValue value1, StatValue value2) {
        super("StatValue");
        this.isGlobal = isGlobal;
        this.isExpression = isExpression;
        this.literalValue = literalValue;
        this.mode = mode;
        this.value1 = value1;
        this.value2 = value2;
    }


    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        ArrayList<ActionEditor.ActionItem> items;
        if (isExpression) {
            if (value1 == null && mode != StatOperation.GET_STAT) value1 = new StatValue(isGlobal);
            if (value2 == null) value2 = new StatValue(isGlobal);
            if (mode == StatOperation.GET_STAT || mode == StatOperation.LENGTH_OF) value1 = null;
            literalValue = null;
            items = new ArrayList<>(Arrays.asList(
                    new ActionEditor.ActionItem("isExpression",
                            ItemBuilder.create((isExpression ? Material.LIME_DYE : Material.RED_DYE))
                                    .name("&aIs expression")
                                    .description("Stat set to expression or literal")
                                    .info("&7Current Value", "")
                                    .info(null, isExpression ? "&aExpression" : "&cLiteral"),
                            ActionEditor.ActionItem.ActionType.BOOLEAN
                    ),
                    (value1 == null ? null : new ActionEditor.ActionItem("value1",
                            ItemBuilder.create(Material.BOOK)
                                    .name("&eAmount 1")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + value1.asString())
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.ACTION_SETTING
                    )),
                    new ActionEditor.ActionItem("mode",
                            ItemBuilder.create(Material.COMPASS)
                                    .name("&eMode")
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + mode)
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.ENUM, StatOperation.values(), null
                    ),
                    //This entire thing is kinda scuffed lol :) //told u it was :)
                    new ActionEditor.ActionItem("value2",
                            ItemBuilder.create(Material.BOOK)
                                    .name("&eAmount " + ((value1 == null) ? "" : "2"))
                                    .info("&7Current Value", "")
                                    .info(null, "&a" + value2.asString())
                                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                            ActionEditor.ActionItem.ActionType.ACTION_SETTING
                    )
            ));
        } else {
            value1 = null;
            value2 = null;
            if (literalValue == null) literalValue = "1.0";
            items = new ArrayList<>(Arrays.asList(
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
            ));
        }
        items.remove(null);
        return new ActionEditor(4, "&ePlayer Stat Action Settings", items);
    }

    //Need to move over the changes from Stat.java to here
    public String calculate(Player player, HousingWorld world) throws NumberFormatException {
        if (!isExpression) return HandlePlaceholders.parsePlaceholders(player, world, literalValue);
        //Look for a stat in value1 and modify it with value2
        if (mode == StatOperation.SET) {
            if (value1.isExpression) {
                Stat stat = world.getStatManager().getPlayerStatByName(player, value1.calculate(player, world));
                if (stat != null) return stat.modifyStat(StatOperation.SET, value2.calculate(player, world));
            }

            Regex statPattern = new Regex("(.+|)%stat\\.player/([a-zA-Z0-9_]+)%(.+?|)");
            MatchResult playerMatch = statPattern.find(value1.literalValue, 0);
            if (playerMatch != null && playerMatch.getGroups().size() == 4) {
                String prefix = playerMatch.getGroups().get(1).getValue();
                String statName = playerMatch.getGroups().get(2).getValue();
                String suffix = playerMatch.getGroups().get(3).getValue();
                if (prefix.isEmpty() && suffix.isEmpty()) {
                    Stat stat = world.getStatManager().getPlayerStatByName(player, statName);
                    if (stat != null) return stat.modifyStat(StatOperation.SET, value2.calculate(player, world));
                }
            }
        }

        //Get the stat value of value2
        if (mode == StatOperation.GET_STAT) {
            Stat stat = world.getStatManager().getPlayerStatByName(player, value2.calculate(player, world));
            if (stat != null) return stat.getValue();
        }
        if (mode == StatOperation.CHAR_AT) {
            try {
                Integer.parseInt(value2.calculate(player, world));
            } catch (NumberFormatException ex) {
                return "";
            }
        }

        if (value1 == null || value2 == null) return "0.0";

        return Stat.modifyStat(mode, value1.calculate(player, world), value2.calculate(player, world));
    }

    public boolean requiresPlayer() {
        return true;
    }

    public String toString() {
        if (isExpression) return "&7(&a" + (value1 != null ? value1 + " " : "") + "&6" + mode.asString() + " &a" + value2 + "&7)";
        else return literalValue;
    }

    public String asString() {
        if (isExpression) return (value1 != null ? "&fValue 1: &a" + value1 + "\n" : "") + "&fMode: &6" + mode + "\n&fValue 2: &a" + value2;
        else return literalValue;
    }

    public void createDisplayItem(ItemBuilder builder) {
    } // never in display

    public void createAddDisplayItem(ItemBuilder builder) {
    } // never created

    public boolean execute(Player player, HousingWorld house) {
        return false;
    } // never "executed"

    @Override
    public HashMap<String, Object> data() {
        return new HashMap<>();
    }

    public boolean isExpression() {
        return isExpression;
    }

    public String getLiteralValue() {
        return literalValue;
    }

    public StatOperation getMode() {
        return mode;
    }

    public StatValue getValue1() {
        return value1;
    }

    public StatValue getValue2() {
        return value2;
    }
}

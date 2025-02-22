package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class StatValue extends Action {
    private String statType;
    private boolean isExpression;
    private String literalValue;
    private StatValue value;
    private List<StatInstance> statInstances = new ArrayList<>();

    public StatValue(String statType) {
        super("StatValue");
        this.literalValue = "1.0";
        this.value = null;
        this.isExpression = false;
        this.statType = statType;
    }

    public StatValue(String statType, boolean isExpression, String literalValue, StatValue value, List<StatInstance> statInstances) {
        super("StatValue");
        this.statType = statType == null ? "stat" : statType;
        this.isExpression = isExpression;
        this.literalValue = literalValue;
        this.value = value;
        this.statInstances = statInstances;
    }

    public StatValue(boolean isGlobal, boolean isExpression, String literalValue, StatValue value, List<StatInstance> statInstances) {
        super("StatValue");
        this.statType = isGlobal ? "global" : "player";
        this.isExpression = isExpression;
        this.literalValue = literalValue;
        this.value = value;
        this.statInstances = statInstances;
    }


    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu editMenu) {
        //If they are somehow seeing it then it's an error
        if (editMenu == null)
            return new ActionEditor(6, "&e" + StringUtilsKt.formatCapitalize(statType) + " Stat > Value", new ActionEditor.ActionItem(
                    ItemBuilder.create(Material.BARRIER)
                            .name("&cError")
                            .description("There was an error creating this menu, please contact an admin if this issue persists."),
                    ActionEditor.ActionItem.ActionType.CUSTOM, 5, (event, obj) -> false
            ));
        ArrayList<ActionEditor.ActionItem> items = new ArrayList<>();
        if (isExpression) {
            literalValue = null;
            if (statInstances.isEmpty()) { //contains the mode and second value
                value = new StatValue(statType);
                statInstances.add(new StatInstance(statType));
            }

            StatValue previousValue = value;

            //If the first mode is not a MODE operation, add a value1 item
            if (!statInstances.isEmpty() && statInstances.getFirst().mode.getArgs().indexOf("MODE") != 0) {
                ItemBuilder valueItem = ItemBuilder.create(Material.BOOK)
                        .name("&e" + statInstances.getFirst().mode.getArgs().getFirst())
                        .info("&7Current Value", "")
                        .info(null, "&a" + previousValue.asString())
                        .info(null, "")
                        .info("Expression", (previousValue.isExpression() ? "&aEnabled" : "&cDisabled"))
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                        .mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION);

                items.add(new ActionEditor.ActionItem("value",
                        valueItem,
                        ActionEditor.ActionItem.ActionType.CUSTOM, (event, obj) -> {
                    if (event.getClick() == ClickType.MIDDLE) {
                        previousValue.setExpression(!previousValue.isExpression());
                        editMenu.open();
                        return true;
                    }

                    if (event.getClick() != ClickType.LEFT) return false;

                    if (previousValue.isExpression()) {
                        new ActionEditMenu(previousValue, Main.getInstance(), editMenu.getOwner(), house, editMenu).open();
                    } else {
                        editMenu.getOwner().sendMessage(colorize("&ePlease enter the text you wish to set in chat: "));
                        editMenu.openChat(Main.getInstance(), previousValue.getLiteralValue(), (value) -> {
                            previousValue.setLiteralValue(value);
                            editMenu.getOwner().sendMessage(colorize("&aValue set to: &e" + value));
                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), editMenu::open, 1L);
                        });
                    }

                    return true;
                }
                ));
            }

            for (int i = 0; i < statInstances.size(); i++) {
                StatInstance statInstance = statInstances.get(i);
                StatOperation mode = statInstance.mode;

                int finalI = i;

                ItemBuilder modeItem = ItemBuilder.create(Material.COMPASS)
                        .name("&eMode")
                        .info("&7Current Value", "")
                        .info(null, "&a" + statInstance.mode)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
                if (i > 0) { //Basically if it's not the first item
                    modeItem.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
                }

                items.add(new ActionEditor.ActionItem("",
                        modeItem,
                        ActionEditor.ActionItem.ActionType.CUSTOM,
                        (event, obj) -> {
                            if (event.getClick() == ClickType.RIGHT && finalI > 0) {
                                statInstances.remove(statInstance);
                                editMenu.open();
                                return true;
                            }

                            if (event.getClick() != ClickType.LEFT) return true;

                            List<Duple<StatOperation, ItemBuilder>> modes = new ArrayList<>();
                            for (StatOperation statOperation : StatOperation.values()) {
                                modes.add(new Duple<>(statOperation, ItemBuilder.create(statOperation.getMaterial()).name("&a" + StringUtilsKt.formatCapitalize(statOperation.name()))));
                            }
                            new PaginationMenu<>(Main.getInstance(), "&eSelect a mode", modes, editMenu.getOwner(), house, editMenu, (output) -> {
                                statInstance.mode = output;
                                editMenu.open();
                            }).open();
                            return true;
                        }
                ));

                int argIndex = (mode.getArgs().indexOf("MODE") == 0) ? 1 : 2;
                ItemBuilder valueItem = ItemBuilder.create(Material.BOOK)
                        .name("&e" + mode.getArgs().get(argIndex))
                        .info("&7Current Value", "")
                        .info(null, "&a" + statInstance.value.asString())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);

                if (!mode.expressionOnly()) {
                    valueItem.info(null, "");
                    valueItem.info("Expression", (statInstance.value.isExpression() ? "&aEnabled" : "&cDisabled"));
                    valueItem.mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION);
                }

                if (i > 0) { //Basically if it's not the first item
                    valueItem.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
                }

                items.add(new ActionEditor.ActionItem("value1",
                        valueItem,
                        ActionEditor.ActionItem.ActionType.CUSTOM,
                        (event, obj) -> {
                            if (event.getClick() == ClickType.MIDDLE && !mode.expressionOnly()) {
                                statInstance.value.setExpression(!statInstance.value.isExpression());
                                editMenu.open();
                                return true;
                            }

                            if (event.getClick() == ClickType.RIGHT && finalI > 0) {
                                statInstances.remove(statInstance);
                                editMenu.open();
                                return true;
                            }

                            if (event.getClick() != ClickType.LEFT) return false;

                            if (statInstance.value.isExpression()) {
                                new ActionEditMenu(statInstance.value, Main.getInstance(), editMenu.getOwner(), house, editMenu).open();
                            } else {
                                editMenu.getOwner().sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
                                editMenu.openChat(Main.getInstance(), statInstance.value.getLiteralValue(), (value) -> {
                                    statInstance.value.setLiteralValue(value);
                                    editMenu.getOwner().sendMessage(colorize("&aValue set to: &e" + value));
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), editMenu::open, 1L);
                                });
                            }
                            return true;
                        }
                ));
            }
        } else {
            statInstances.clear();
            value = null;
            if (literalValue == null) literalValue = "1.0";
            items.addAll(Arrays.asList(
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

        items.add(new ActionEditor.ActionItem(
                ItemBuilder.create(Material.PAPER)
                        .name("&aAdd Stat Expression")
                        .description("Add a new stat expression instance.\n\nBasically adds a new mode and value to the expression."),
                ActionEditor.ActionItem.ActionType.CUSTOM, 50, (event, obj) -> {
            if (statInstances.getLast().mode.expressionOnly()) {
                editMenu.getOwner().sendMessage(colorize("&cYou can't add another stat change to this expression."));
                return false;
            }
            if (statInstances.size() >= 6) {
                editMenu.getOwner().sendMessage(colorize("&cYou can only have a maximum of 6 stat instances."));
                return false;
            }
            statInstances.add(new StatInstance(statType));
            editMenu.open();
            return true;
        }
        ));

        items.remove(null);
        return new ActionEditor(6, getTitle((ActionEditMenu) editMenu), items);
    }

    public String calculate(Player player, HousingWorld world) throws NumberFormatException {
        if (!isExpression) return HandlePlaceholders.parsePlaceholders(player, world, literalValue);
        //Look for a stat in value1 and modify it with value2
        StatValue value1 = this.value;

        if (value1 == null) return "0.0";
        String result = value1.calculate(player, world);
        for (StatInstance statInstance : statInstances) {
            StatOperation mode = statInstance.mode;
            StatValue value2 = statInstance.value;

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

            if (mode == StatOperation.PLAYER_STAT) {
                Stat stat = world.getStatManager().getPlayerStatByName(player, value2.calculate(player, world));
                if (stat != null) return stat.getValue();
            }
            if (mode == StatOperation.GLOBAL_STAT) {
                Stat stat = world.getStatManager().getGlobalStatByName(value2.calculate(player, world));
                if (stat != null) return stat.getValue();
            }
            if (mode == StatOperation.CHAR_AT) {
                try {
                    Integer.parseInt(value2.calculate(player, world));
                } catch (NumberFormatException ex) {
                    return "";
                }
            }
            if (result == null || value2 == null) return "0.0";
            result = Stat.modifyStat(mode, result, value2.calculate(player, world));
        }
        return result;
    }

    public boolean requiresPlayer() {
        return true;
    }

    public String toString() {
        if (isExpression) {
            StringBuilder str = new StringBuilder("&7(");

            if (!statInstances.isEmpty() && statInstances.getFirst().mode.getArgs().indexOf("MODE") != 0) {
                str.append("&a").append(value).append(" ");
            }

            for (StatInstance statInstance : statInstances) {
                StatOperation mode = statInstance.mode;
                str.append("&6").append(mode.asString()).append(" &a").append(statInstance.value);

                if (statInstances.indexOf(statInstance) < statInstances.size() - 1) {
                    str.append(" ");
                }
            }
            str.append("&7)");
            return str.toString();
        } else return literalValue;
    }

    //This is used for the display item
    public String asString() {
        if (isExpression) {
            StringBuilder str = new StringBuilder("");
            if (!statInstances.isEmpty() && statInstances.getFirst().mode.getArgs().indexOf("MODE") != 0) {
                str.append("&f").append(statInstances.getFirst().mode.getArgs().getFirst()).append(": &a").append(value).append("\n");
            }
            for (StatInstance statInstance : statInstances) {
                StatOperation mode = statInstance.mode;
                str.append("&fMode: &6").append(mode);
                str.append("\n&f").append(mode.getArgs().get((mode.getArgs().indexOf("MODE") == 0 ? 1 : 2))).append(": &a").append(statInstance.value);
            }
            return str.toString();
        } else return literalValue;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        super.fromData(data, actionClass);
        if (data.get("isGlobal") != null) { //Convert from old format
            statType = (boolean) data.get("isGlobal") ? "global" : "player";
        } else {
            statType = data.get("statType").toString();
        }
    }

    public void createDisplayItem(ItemBuilder builder) {
    } // never in display

    public void createAddDisplayItem(ItemBuilder builder) {
    } // never created

    public boolean execute(Player player, HousingWorld house) {
        return false;
    } // never "executed"

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    public boolean isExpression() {
        return isExpression;
    }

    public void setExpression(boolean expression) {
        isExpression = expression;

        if (isExpression) {
            literalValue = null;
            if (statInstances.isEmpty()) {
                value = new StatValue(statType);
                statInstances.add(new StatInstance(statType));
            }
        } else {
            statInstances.clear();
            value = null;
            if (literalValue == null) literalValue = "1.0";
        }
    }

    public String getLiteralValue() {
        return literalValue;
    }

    public void setLiteralValue(String literalValue) {
        this.literalValue = literalValue;
    }

    public StatValue getValue() {
        return value;
    }

    public List<StatInstance> getStatInstances() {
        return statInstances;
    }

    public String getStatType() {
        return statType;
    }

    public String getTitle(ActionEditMenu currentMenu) {
        StringBuilder title = new StringBuilder("&e" + StringUtilsKt.formatCapitalize(statType) + " Stat > Value");
        int count = 1;
        while (currentMenu.getBackMenu() != null && currentMenu.getBackMenu() instanceof ActionEditMenu back && back.getAction() instanceof StatValue) {
            title.append(" > Value");
            count++;
            currentMenu = back;
        }
        if (count > 2) {
            title = new StringBuilder("&e" + StringUtilsKt.formatCapitalize(statType) + " Stat > Value > " + (count - 1) + " Values");
        }
        return title.toString();
    }

    public String[] importValue(String[] nextParts) {
        if (nextParts.length < 1) return new String[0];
        String[] parts = nextParts;

        if (parts[0].startsWith("(")) {
            setExpression(true);
            parts[0] = parts[0].substring(1);

            List<StatInstance> statInstances = new ArrayList<>();
            StatInstance statInstance = new StatInstance(statType);
            statInstance.mode = null;
            statInstance.value = null;

            value = new StatValue(statType);
            parts = value.importValue(parts);

            while (parts.length > 0) {
                String part = parts[0];
                if (StatOperation.getOperation(part) != null) {
                    statInstance.mode = StatOperation.getOperation(part);
                    parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
                    continue;
                } else {
                    if (part.endsWith(")")) {
                        parts[0] = part.substring(0, part.length() - 1);
                    }
                    StatValue value = new StatValue(statType);
                    parts = value.importValue(parts);
                    statInstance.value = value;
                }

                if (statInstance.mode != null && statInstance.value != null) {
                    statInstances.add(statInstance);
                    statInstance = new StatInstance(statType);
                }
            }

            this.statInstances = statInstances;
        } else {
            setExpression(false);
            literalValue = parts[0];
            if (literalValue.startsWith("\"")) {
                literalValue = literalValue.substring(1);
                while (!literalValue.endsWith("\"")) {
                    literalValue += " " + parts[1];
                    parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
                }
                literalValue = literalValue.substring(0, literalValue.length() - 1);
            }
            parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
        }
        return parts;
    }
}

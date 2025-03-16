package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingData.ActionData;
import com.al3x.housing2.Instances.HousingData.MoreStatData;
import com.al3x.housing2.Instances.HousingData.StatActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Actions.ActionEnumMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.al3x.housing2.Instances.HousingData.ActionData.Companion;
import static com.al3x.housing2.Utils.Color.colorize;

public class PlayerStatAction extends HTSLImpl {

    private static final Gson gson = new Gson();
    private String statName;

    private List<StatInstance> statInstances = new ArrayList<>();

    private StatOperation mode;
    private StatValue value;

    public PlayerStatAction() {
        super("Player Stat Action");
        this.statName = "Kills";

        this.statInstances.add(new StatInstance("player"));
    }

    @Override
    public String toString() {
        // add the first 3 statinances then do ... <number of statinstances - 3> more

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(3, statInstances.size()); i++) {
            sb.append(statInstances.get(i).mode).append(" ").append(Color.removeColor(statInstances.get(i).value.toString()));
            if (i != Math.min(3, statInstances.size()) - 1) {
                sb.append(", ");
            }
        }
        if (statInstances.size() > 3) {
            sb.append("... ").append(statInstances.size() - 3).append(" more");
        }

        return "PlayerStatAction (StatName: " + statName + ", StatInstances: " + sb.toString() + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.FEATHER);
        builder.name("&eChange Player Stat");
        builder.info("&eSettings", "");
        builder.info("Stat", "&a" + statName);
        if (statInstances.size() > 3) {

        } else {
            builder.info("&eStat Changes", "");
            for (int i = 0; i < Math.min(3, statInstances.size()); i++) {
                builder.info("Mode", "&6" + statInstances.get(i).mode);
                builder.info("Value", "&a" + statInstances.get(i).value);
            }
        }


        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.FEATHER);
        builder.name("&aChange Player Stat");
        builder.description("Modify a stat of the player who triggered the action.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&ePlayer Stat Action Settings");
        }
        List<ActionEditor.ActionItem> items = new ArrayList<>();

        items.add(new ActionEditor.ActionItem("statName",
                ItemBuilder.create(Material.BOOK)
                        .name("&eStat")
                        .info("&7Current Value", "")
                        .info(null, "&a" + statName)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING
        ));

        for (int i = 0; i < statInstances.size(); i++) {
            StatInstance instance = statInstances.get(i);
            ItemBuilder modeItem = ItemBuilder.create(Material.COMPASS)
                    .name("&eMode")
                    .info("&7Current Value", "")
                    .info(null, "&a" + instance.mode)
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
            if (i > 0) {
                modeItem.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
            }

            int finalI = i;
            items.add(new ActionEditor.ActionItem("mode",
                    modeItem,
                    (event, obj) -> {
                        if (event.getClick() == ClickType.RIGHT && finalI > 0) {
                            statInstances.remove(instance);
                            backMenu.open();
                            return true;
                        }

                        if (event.getClick() != ClickType.LEFT) return false;

                        List<Duple<StatOperation, ItemBuilder>> modes = new ArrayList<>();
                        for (StatOperation mode : StatOperation.values()) {
                            if (mode.expressionOnly()) continue;
                            modes.add(new Duple<>(mode, ItemBuilder.create(mode.getMaterial()).name("&a" + mode)));
                        }
                        new PaginationMenu<>(Main.getInstance(), "&eSelect a mode", modes, backMenu.getOwner(), house, backMenu, (mode) -> {
                            instance.mode = mode;
                            backMenu.open();
                        }).open();

                        return true;
                    }
            ));

            ItemBuilder valueItem = ItemBuilder.create(Material.BOOK)
                    .name("&eAmount")
                    .info("&7Current Value", "")
                    .info(null, "&a" + instance.value)
                    .info(null, "")
                    .info("Expression", (instance.value.isExpression() ? "&aEnabled" : "&cDisabled"))
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW)
                    .mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION);

            if (i > 0) {
                valueItem.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
            }

            items.add(new ActionEditor.ActionItem("value",
                    valueItem,
                    ActionEditor.ActionItem.ActionType.CUSTOM, (event, obj) -> {
                if (event.getClick() == ClickType.MIDDLE) {
                    instance.value.setExpression(!instance.value.isExpression());
                    backMenu.open();
                    return true;
                }

                if (event.getClick() == ClickType.RIGHT && finalI > 0) {
                    statInstances.remove(instance);
                    backMenu.open();
                    return true;
                }

                if (event.getClick() != ClickType.LEFT) return false;

                if (instance.value.isExpression()) {
                    new ActionEditMenu(instance.value, Main.getInstance(), backMenu.getOwner(), house, backMenu).open();
                } else {
                    backMenu.getOwner().sendMessage(colorize("&ePlease enter the text you wish to set in chat!"));
                    backMenu.openChat(Main.getInstance(), instance.value.getLiteralValue(), (value) -> {
                        instance.value.setLiteralValue(value);
                        backMenu.getOwner().sendMessage(colorize("&aValue set to: &e" + value));
                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), backMenu::open, 1L);
                    });
                }
                return true;
            }
            ));
        }

        items.add(new ActionEditor.ActionItem(
                ItemBuilder.create(Material.PAPER)
                        .name("&aAdd Stat Expression")
                        .description("Add a new stat expression instance.\n\nBasically adds a new mode and value to the expression."),
                ActionEditor.ActionItem.ActionType.CUSTOM, 50, (event, obj) -> {
            if (statInstances.size() >= 6) {
                backMenu.getOwner().sendMessage(colorize("&cYou can only have a maximum of 6 stat instances."));
                return false;
            }
            statInstances.add(new StatInstance("player"));
            backMenu.open();
            return true;
        }
        ));

        return new ActionEditor(6, "&ePlayer Stat Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (player == null) return OutputType.ERROR;
        String name = HandlePlaceholders.parsePlaceholders(player, house, statName);
        Stat stat = house.getStatManager().getPlayerStatByName(player, name);

        for (StatInstance instance : statInstances) {
            if (stat.modifyStat(instance.mode, HandlePlaceholders.parsePlaceholders(player, house, instance.value.calculate(player, house))) == null) {
                player.sendMessage(colorize("&cFailed to modify stat: " + name + " with mode: " + instance.mode + " and value: " + instance.value));
            }
        }

        if (stat.getValue().equals("0") || stat.getValue().equals("0.0") || stat.getValue().equals("&r") || stat.getValue().equals("§r")) {
            if (house.getStatManager().hasStat(player, name)) {
                house.getStatManager().getPlayerStats(player).remove(stat);
            }
            return OutputType.SUCCESS;
        }

        if (!house.getStatManager().hasStat(player, name)) {
            house.getStatManager().addPlayerStat(player, stat);
        }

        return OutputType.SUCCESS;
    }

    public String getStatName() {
        return statName;
    }

    public StatOperation getMode() {
        return mode;
    }

    public void setStatName(String name) {
        this.statName = name;
    }

    public void setMode(StatOperation mode) {
        this.mode = mode;
    }

    public StatValue getValue() {
        return value;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("statName", statName);
        data.put("statInstances", statInstances);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        statName = (String) data.get("statName");

        if (data.containsKey("statInstances")) {
            this.statInstances = dataToList(gson.toJsonTree(data.get("statInstances")).getAsJsonArray(), StatInstance.class);
        } else {
            statInstances = new ArrayList<>();
            StatInstance statInstance = new StatInstance("player");
            mode = StatOperation.valueOf((String) data.get("mode"));
            value = gson.fromJson(gson.toJson(data.get("value")), MoreStatData.class).toStatValue();

            statInstance.value = value;
            statInstance.mode = mode;
        }
    }

    @Override
    public String export(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statInstances.size(); i++) {
            sb.append(statInstances.get(i).mode.getAlternative()).append(" ").append(Color.removeColor(statInstances.get(i).value.toString()));
            if (i != statInstances.size() - 1) {
                sb.append(" ");
            }
        }
        return " ".repeat(indent) + keyword() + " \"" + statName + "\" " + sb.toString();
    }

    @Override
    public String keyword() {
        return "stat";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");

        Duple<String[], String> statArg = handleArg(parts, 0);
        this.statName = statArg.getSecond();
        parts = statArg.getFirst();

        ArrayList<StatInstance> statInstances = new ArrayList<>();

        StatInstance instance = new StatInstance("player");
        while (parts.length > 0) {
            if (StatOperation.getOperation(parts[0]) != null) {
                instance.mode = StatOperation.getOperation(parts[0]);
                parts = Arrays.copyOfRange(parts, 1, parts.length);
                continue;
            } else {
                instance.value = new StatValue("player");
                parts = instance.value.importValue(parts);
            }

            if (instance.mode != null && instance.value != null) {
                statInstances.add(instance);
                instance = new StatInstance("player");
            }
        }

        this.statInstances = statInstances;

        return nextLines;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingData.MoreStatData;
import com.al3x.housing2.Instances.HousingData.StatActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class GlobalStatAction extends Action {
    private static final Gson gson = new Gson();

    private String statName;
    private StatOperation mode;
    private StatValue value;

    public GlobalStatAction() {
        super("Global Stat Action");
        this.statName = "Kills";
        this.mode = StatOperation.INCREASE;
        this.value = new StatValue(true);
    }

    public GlobalStatAction(String statName, StatOperation mode, StatValue value) {
        super("Global Stat Action");
        this.statName = statName;
        this.mode = mode;
        this.value = value;
    }

    @Override
    public String toString() {
        return "GlobalStatAction (StatName: " + statName + ", Mode: " + mode + ", Value: " + value + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1");
        builder.name("&eChange Global Stat");
        builder.info("&eSettings", "");
        builder.info("Stat", "&a" + statName);
        builder.info("Mode", "&a" + mode);
        builder.info("Value", "&a" + value);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("cf40942f364f6cbceffcf1151796410286a48b1aeba77243e218026c09cd1");
        builder.name("&aChange Global Stat");
        builder.description("Modify a stat of the player who triggered the action.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("statName",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eStat")
                                .info("&7Current Value", "")
                                .info(null, "&a" + statName)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.STRING
                ),
                new ActionEditor.ActionItem("mode",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eMode")
                                .info("&7Current Value", "")
                                .info(null, "&a" + mode)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, StatOperation.values(), null
                ),
                new ActionEditor.ActionItem("value",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eAmount")
                                .info("&7Current Value", "")
                                .info(null, "&a" + value)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ACTION_SETTING
                )
        );

        return new ActionEditor(4, "&eGlobal Stat Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Stat stat = house.getStatManager().getGlobalStatByName(statName);

        if (stat.modifyStat(mode, HandlePlaceholders.parsePlaceholders(player, house, String.valueOf(value.calculate(player, house)))) == null) {
            player.sendMessage(colorize("&cFailed to modify stat: " + statName + " with mode: " + mode + " and value: " + value));
        }

        if ((stat.getValue().equals("0") || stat.getValue().equals("0.0")) && house.getStatManager().hasGlobalStat(statName)) {
            house.getStatManager().getGlobalStats().remove(stat);
            return true;
        }

        if (!house.getStatManager().hasGlobalStat(statName)) {
            house.getStatManager().getGlobalStats().add(stat);
        }

        return true;
    }

    public String getStatName() {
        return statName;
    }
    public StatOperation getMode() {
        return mode;
    }
    public StatValue getValue() {
        return value;
    }
    public void setStatName(String name) {
        this.statName = name;
    }
    public void setMode(StatOperation mode) {
        this.mode = mode;
    }
    public void setValue(StatValue value) {
        this.value = value;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("statName", statName);
        data.put("mode", mode);
        data.put("value", StatActionData.Companion.fromStatValue(value));
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        statName = (String) data.get("statName");
        mode = StatOperation.valueOf((String) data.get("mode"));
        value = gson.fromJson(gson.toJson(data.get("value")), MoreStatData.class).toStatValue();
    }
}

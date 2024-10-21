package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GlobalStatAction extends Action {

    private String statName;
    private StatOperation mode;
    private String value;

    public GlobalStatAction() {
        super("Global Stat Action");
        this.statName = "Kills";
        this.mode = StatOperation.INCREASE;
        this.value = "1.0";
    }

    public GlobalStatAction(String statName, StatOperation mode, Double value) {
        super("Global Stat Action");
        this.statName = statName;
        this.mode = mode;
        this.value = String.valueOf(value);
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
                        ActionEditor.ActionItem.ActionType.STRING
                )
        );

        return new ActionEditor(4, "&eGlobal Stat Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Stat stat = house.getStatManager().getGlobalStatByName(statName);

        stat.modifyStat(mode, value);
        return true;
    }

    public String getStatName() {
        return statName;
    }
    public StatOperation getMode() {
        return mode;
    }
    public String getValue() {
        return value;
    }
    public void setStatName(String name) {
        this.statName = name;
    }
    public void setMode(StatOperation mode) {
        this.mode = mode;
    }
    public void setValue(Double value) {
        this.value = String.valueOf(value);
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("statName", statName);
        data.put("mode", mode);
        data.put("value", value);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

//    @Override
//    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
//        if (!data.containsKey("statName") || !data.containsKey("mode") || !data.containsKey("value")) {
//            return;
//        }
//        this.statName = (String) data.get("statName");
//        this.mode = StatOperation.valueOf((String) data.get("mode"));
//        this.value = (String) data.get("value");
//    }
}

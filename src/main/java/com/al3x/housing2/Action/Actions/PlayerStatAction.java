package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class PlayerStatAction extends Action {

    private String statName;
    private StatOperation mode;
    private Double value;

    public PlayerStatAction() {
        super("Player Stat Action");
        this.statName = "Kills";
        this.mode = StatOperation.INCREASE;
        this.value = 1.0;
    }

    public PlayerStatAction(String statName, StatOperation mode, Double value) {
        super("Player Stat Action");
        this.statName = statName;
        this.mode = mode;
        this.value = value;
    }

    @Override
    public String toString() {
        return "PlayerStatAction (StatName: " + statName + ", Mode: " + mode + ", Value: " + value + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.FEATHER);
        builder.name("&eChange Player Stat");
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
        builder.material(Material.FEATHER);
        builder.name("&aChange Player Stat");
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
                        ActionEditor.ActionItem.ActionType.ENUM, StatOperation.values(), Material.WHITE_STAINED_GLASS //eventually will use colors for each enum
                ),
                new ActionEditor.ActionItem("value",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eAmount")
                                .info("&7Current Value", "")
                                .info(null, "&a" + value)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                )
        );

        return new ActionEditor(4, "&ePlayer Stat Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Stat stat = house.getStatManager().getPlayerStatByName(player, statName);
        stat.modifyStat(mode, value);
        return true;
    }

    public String getStatName() {
        return statName;
    }
    public StatOperation getMode() {
        return mode;
    }
    public Double getValue() {
        return value;
    }
    public void setStatName(String name) {
        this.statName = name;
    }
    public void setMode(StatOperation mode) {
        this.mode = mode;
    }
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("statName", statName);
        data.put("mode", mode);
        data.put("value", value);
        return data;
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

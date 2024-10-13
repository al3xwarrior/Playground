package com.al3x.housing2.Actions;

import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class PlayerStatAction extends Action {

    private String statName;
    private StatOperation mode;
    private String value;

    public PlayerStatAction() {
        super("Player Stat Action");
        this.statName = "Kills";
        this.mode = StatOperation.INCREASE;
        this.value = "1";
    }

    public PlayerStatAction(String statName, StatOperation mode, String value) {
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
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&ePlayer Stat Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Alters a player's stat."),
                "",
                colorize("&eSettings:"),
                colorize("&fStat: " + getStatName()),
                colorize("&fMode: " + getMode()),
                colorize("&fValue: " + getValue()),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        Stat stat = house.getStatManager().getPlayerStatByName(player, statName);
        stat.modifyStat(mode, Double.parseDouble(value));
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
    public void setValue(String value) {
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

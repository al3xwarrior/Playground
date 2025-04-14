package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.AddStatInstanceProperty;
import com.al3x.housing2.Action.Properties.StatInstanceProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class PlayerStatAction extends HTSLImpl {
    public PlayerStatAction() {
        super(
                "player_stat_action",
                "Player Stat",
                "Changes the stat of a Player.",
                Material.FEATHER,
                List.of("stat")
        );

        getProperties().addAll(List.of(
                new StringProperty(
                        "statName",
                        "Stat Name",
                        "The name of the stat to modify."
                ).setValue("Kills"),
                new StatInstanceProperty(
                ).setValue(new ArrayList<>(List.of(
                        new StatInstance()
                ))),
                new AddStatInstanceProperty(35)
        ));

    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (player == null) return OutputType.ERROR;
        String name = getProperty("statName", StringProperty.class).parseNoSpace(house, player);
        if (name == null) return OutputType.ERROR;
        Stat stat = house.getStatManager().getPlayerStatByName(player, name);

        List<StatInstance> statInstances = getProperty("statInstances", StatInstanceProperty.class).getValue();
        for (StatInstance instance : statInstances) {
            if (stat.modifyStat(instance.mode, HandlePlaceholders.parsePlaceholders(player, house, instance.value.calculate(player, house))) == null) {
                return OutputType.ERROR;
            }
        }

        if (stat.getValue() == null) {
            stat = new Stat(name, "0.0");
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

    @Override
    public boolean requiresPlayer() {
        return true;
    }
//
//    @Override
//    public String export(int indent) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < statInstances.size(); i++) {
//            sb.append(statInstances.get(i).mode.getAlternative()).append(" ").append(Color.removeColor(statInstances.get(i).value.toString()));
//            if (i != statInstances.size() - 1) {
//                sb.append(" ");
//            }
//        }
//        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " \"" + statName + "\" " + sb.toString();
//    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        String[] parts = action.split(" ");
//
//        Duple<String[], String> statArg = handleArg(parts, 0);
//        this.statName = statArg.getSecond();
//        parts = statArg.getFirst();
//
//        ArrayList<StatInstance> statInstances = new ArrayList<>();
//
//        StatInstance instance = new StatInstance();
//        while (parts.length > 0) {
//            if (StatOperation.getOperation(parts[0]) != null) {
//                instance.mode = StatOperation.getOperation(parts[0]);
//                parts = Arrays.copyOfRange(parts, 1, parts.length);
//                continue;
//            } else {
//                instance.value = new StatValue();
//                parts = instance.value.importValue(parts);
//            }
//
//            if (instance.mode != null && instance.value != null) {
//                statInstances.add(instance);
//                instance = new StatInstance();
//            }
//        }
//
//        this.statInstances = statInstances;
//
//        return nextLines;
//    }
}

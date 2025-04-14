package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.*;
import com.al3x.housing2.Action.Properties.StatValueProperty.StatValueInstance;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.StringUtilsKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.al3x.housing2.Action.HTSLImpl.handleArg;

@Getter
@Setter
public class StatValue extends Action {
    public StatValue() {
        super(
                "stat_value",
                //everything below this doesnt matter
                "Stat Value",
                "A value for a stat.",
                Material.BOOK,
                List.of()
        );

        getProperties().addAll(List.of(
                new StatValueProperty(
                        "value"
                ),
                new StatInstanceProperty()
                        .setValue(new ArrayList<>()),
                new AddStatInstanceProperty(35)
        ));
    }

    public String calculate(Player player, HousingWorld world) throws NumberFormatException {
        StatValueInstance statValueInstance = getValue("value", StatValueInstance.class);
        if (statValueInstance == null) return "0.0";

        //Look for a stat in value1 and modify it with value2
        StatValue value1 = statValueInstance.getExpressionValue();

        if (value1 == null) return "0.0";
        String result = value1.calculate(player, world);

        List<StatInstance> statInstances = getProperty("statInstances", StatInstanceProperty.class).getValue();
        for (StatInstance statInstance : statInstances) {
            StatOperation mode = statInstance.mode;
            StatValueInstance value2 = statInstance.value;

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
        StatValueInstance statValueInstance = getValue("value", StatValueInstance.class);
        if (statValueInstance == null) return "0.0";
        String value = statValueInstance.isExpression() ?
                statValueInstance.getExpressionValue().toString() :
                statValueInstance.getLiteralValue();
        List<StatInstance> statInstances = getProperty("statInstances", StatInstanceProperty.class).getValue();
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

    }

    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS;
    } // never "executed"

//    public String[] importValue(String[] nextParts) {
//        if (nextParts.length < 1) return new String[0];
//        String[] parts = nextParts;
//
//        if (parts[0].startsWith("(")) {
//            setExpression(true);
//            parts[0] = parts[0].substring(1);
//
//            List<StatInstance> statInstances = new ArrayList<>();
//            StatInstance statInstance = new StatInstance(statType);
//            statInstance.mode = null;
//            statInstance.value = null;
//
//            value = new StatValue(statType);
//            parts = value.importValue(parts);
//
//            while (parts.length > 0) {
//                String part = parts[0];
//                if (StatOperation.getOperation(part) != null) {
//                    statInstance.mode = StatOperation.getOperation(part);
//                    parts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length)).toArray(new String[0]);
//                    continue;
//                } else {
//                    if (part.endsWith(")")) {
//                        parts[0] = part.substring(0, part.length() - 1);
//                    }
//                    StatValue value = new StatValue(statType);
//                    parts = value.importValue(parts);
//                    statInstance.value = value;
//                }
//
//                if (statInstance.mode != null && statInstance.value != null) {
//                    statInstances.add(statInstance);
//                    statInstance = new StatInstance(statType);
//                }
//            }
//
//            this.statInstances = statInstances;
//        } else {
//            setExpression(false);
//            Duple<String[], String> literalArg = handleArg(parts, 0);
//            this.literalValue = literalArg.getSecond();
//            parts = literalArg.getFirst();
//        }
//        return parts;
//    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Data.StatActionData;
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

    private static final Gson gson = new Gson();

    @Setter
    private String statName = "kills";
    @Setter
    @Getter
    private StatOperation mode;
    @Getter
    private StatValue value;

    private List<StatInstance> statInstances = new ArrayList<>(List.of(
            new StatInstance("player")
    ));

    public PlayerStatAction() {
        super(
                "player_stat_action",
                "Player Stat",
                "Changes the stat of a Player.",
                Material.FEATHER,
                List.of("stat")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "statName",
                        "Stat Name",
                        "The name of the stat to change.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "statInstances",
                        "Stat Instances",
                        "The instances of the stat to modify.",
                        ActionProperty.PropertyType.STAT_INSTANCE,
                        new ActionProperty.StatProperties()
                ),
                new ActionProperty(
                        "addStatInstance",
                        "Add Stat Instance",
                        "Adds a new stat instance.",
                        ActionProperty.PropertyType.CUSTOM, 50,
                        (house, backMenu, player) -> (event, obj) -> {
                            if (statInstances.size() >= 6) {
                                backMenu.getOwner().sendMessage(colorize("&cYou can only have a maximum of 6 stat instances."));
                                return false;
                            }
                            statInstances.add(new StatInstance("npc"));
                            backMenu.open();
                            return true;
                        }
                )
        ));

    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (player == null) return OutputType.ERROR;
        String name = HandlePlaceholders.parsePlaceholders(player, house, statName);
        Stat stat = house.getStatManager().getPlayerStatByName(player, name);

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
            value = gson.fromJson(gson.toJson(data.get("value")), StatActionData.MoreStatData.class).toStatValue();

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
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " \"" + statName + "\" " + sb.toString();
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

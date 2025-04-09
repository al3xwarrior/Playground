package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Data.StatActionData;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

// TODO: Check out old display item code, see how to implement it in new system
// TODO: Add support for stat instances (check old editorMenu code)
// TODO: Add seperate system for adding clickable buttons like `addStatInstance` (also check out FunctionAction)

@ToString
public class NPCStatAction extends HTSLImpl implements NPCAction {

    private static final Gson gson = new Gson();

    @Setter
    private String statName = "kills";
    @Setter
    @Getter
    private StatOperation mode;
    @Getter
    private StatValue value;

    private List<StatInstance> statInstances = new ArrayList<>(List.of(
            new StatInstance("npc")
    ));

    public NPCStatAction() {
        super(
                "npc_stat_action",
                "NPC Stat",
                "Changes the stat of an NPC.",
                Material.ZOMBIE_SPAWN_EGG,
                List.of("npcStat")
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
        return OutputType.SUCCESS;
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
            StatInstance statInstance = new StatInstance("npc");
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

        StatInstance instance = new StatInstance("npc");
        while (parts.length > 0) {
            if (StatOperation.getOperation(parts[0]) != null) {
                instance.mode = StatOperation.getOperation(parts[0]);
                parts = Arrays.copyOfRange(parts, 1, parts.length);
                continue;
            } else {
                instance.value = new StatValue("npc");
                parts = instance.value.importValue(parts);
            }

            if (instance.mode != null && instance.value != null) {
                statInstances.add(instance);
                instance = new StatInstance("npc");
            }
        }

        this.statInstances = statInstances;

        return nextLines;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (player == null) return;
        String name = HandlePlaceholders.parsePlaceholders(player, house, statName);
        HousingNPC housingNPC = house.getNPCByCitizensID(npc.getId());
        Stat stat = housingNPC.getStats().stream().filter(s -> s.getStatName().equals(name)).findFirst().orElse(new Stat(name, "0"));

        for (StatInstance instance : statInstances) {
            if (stat.modifyStat(instance.mode, HandlePlaceholders.parsePlaceholders(player, house, instance.value.calculate(player, house))) == null) {
                player.sendMessage(colorize("&cFailed to modify stat: " + name + " with mode: " + instance.mode + " and value: " + instance.value));
            }
        }

        if (stat.getValue().equals("0") || stat.getValue().equals("0.0") || stat.getValue().equals("&r") || stat.getValue().equals("§r")) {
            if (housingNPC.getStats().stream().anyMatch(s -> s.getStatName().equals(stat.getStatName()))) {
                housingNPC.getStats().remove(stat);
            }
            return;
        }

        if (housingNPC.getStats().stream().noneMatch(s -> s.getStatName().equals(stat.getStatName()))) {
            housingNPC.getStats().add(stat);
        }
    }

    @Override
    public boolean hide() {
        return true;
    }
}

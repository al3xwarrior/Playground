package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.AttackEntityEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Data.ConditionalData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
public class AttackEntityAction extends HTSLImpl implements NPCAction {
    private static final Gson gson = new Gson();

    @Setter
    @Getter
    private AttackEntityEnum mode;
    private String range;
    private List<Condition> conditions;
    @Getter
    private String value;

    public AttackEntityAction() {
        super(
                "attack_entity_action",
                "Attack Entity",
                "Attacks an entity.",
                Material.IRON_SWORD,
                List.of("attackEntity")
        );

        mode = AttackEntityEnum.NEAREST;
        range = "10";
        conditions = new ArrayList<>();
        value = "2";

        getProperties().addAll(List.of(
                new ActionProperty(
                        "mode",
                        "Mode",
                        "The mode of the attack.",
                        ActionProperty.PropertyType.ENUM,
                        AttackEntityEnum.class
                ),
                new ActionProperty(
                        "range",
                        "Range",
                        "The range of the attack.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "value",
                        "Value",
                        "The value of the attack.",
                        ActionProperty.PropertyType.STRING
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.ERROR; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        String range = HandlePlaceholders.parsePlaceholders(player, house, this.range);
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value) || !NumberUtilsKt.isDouble(range)) {
            return OutputType.ERROR;
        }
        double rangeValue = Double.parseDouble(range);
        double damageValue = Double.parseDouble(value);
        List<Entity> entities = new ArrayList<>(player.getWorld().getEntities());
        switch (mode) {
            case NEAREST:
                entities.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(player.getLocation())));
                break;
            case PLAYER:
                entities.removeIf(entity -> !(entity instanceof Player) || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case NPC:
                entities.removeIf(entity -> !CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case MOB:
                entities.removeIf(entity -> entity instanceof Player || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case ALL:
                break;
            case CONDITION:
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        if (conditions.stream().allMatch(condition -> condition.execute(target, house, null, executor))) {
                            target.damage(damageValue);
                        }
                    } else if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                        NPC target = CitizensAPI.getNPCRegistry().getNPC(entity);
                        if (conditions.stream().filter(condition -> condition instanceof NPCCondition).allMatch(condition -> ((NPCCondition) condition).npcExecute(player, target, house, null, executor))) {
                            if (entity instanceof LivingEntity le) {
                                le.damage(damageValue);
                            }
                        }
                    }
                }
                return OutputType.SUCCESS;
        }

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                le.damage(damageValue);
            }
        }

        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {

        String range = HandlePlaceholders.parsePlaceholders(player, house, this.range);
        String value = HandlePlaceholders.parsePlaceholders(player, house, this.value);
        if (!NumberUtilsKt.isDouble(value) || !NumberUtilsKt.isDouble(range)) {
            return;
        }
        double rangeValue = Double.parseDouble(range);
        double damageValue = Double.parseDouble(value);
        List<Entity> entities = new ArrayList<>(npc.getEntity().getNearbyEntities(rangeValue, rangeValue, rangeValue));
        switch (mode) {
            case NEAREST:
                entities.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(npc.getEntity().getLocation())));

                if (entities.isEmpty()) {
                    return;
                }

                Entity e = entities.getFirst();
                if (e instanceof LivingEntity le) {
                    le.damage(damageValue);
                }

                return;
            case PLAYER:
                entities.removeIf(entity -> !(entity instanceof Player) || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case NPC:
                entities.removeIf(entity -> !CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case MOB:
                entities.removeIf(entity -> entity instanceof Player || CitizensAPI.getNPCRegistry().isNPC(entity));
                break;
            case ALL:
                break;
            case CONDITION:
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;
                        if (conditions.stream().allMatch(condition -> condition.execute(target, house, null, executor))) {
                            target.damage(damageValue);
                        }
                    } else if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                        NPC target = CitizensAPI.getNPCRegistry().getNPC(entity);
                        if (conditions.stream().filter(condition -> condition instanceof NPCCondition).allMatch(condition -> ((NPCCondition) condition).npcExecute(player, target, house, null, executor))) {
                            if (entity instanceof LivingEntity le) {
                                le.damage(damageValue);
                            }
                        }
                    }
                }
                return;
        }

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                le.damage(damageValue);
            }
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        mode = AttackEntityEnum.valueOf((String) data.get("mode"));
        range = (String) data.get("range");
        value = (String) data.get("value");
        if (!data.containsKey("conditions")) return;
        // I don't know how this works lol
        Object subActions = data.get("conditions");
        JsonArray jsonArray = gson.toJsonTree(subActions).getAsJsonArray();
        ArrayList<ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            ActionData action = gson.fromJson(jsonObject, ActionData.class);
            actions.add(action);
        }
    }

    @Override
    public String export(int indent) {
        StringBuilder builder = new StringBuilder();
        for (Condition condition : conditions) {
            if (condition instanceof CHTSLImpl c) {
                builder.append(c.export()).append(", ");
            }
        }
        String conditionString = builder.toString();
        if (!conditionString.isEmpty()) {
            conditionString = conditionString.substring(0, conditionString.length() - 2);
        }
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + mode + " " + range + " " + value + "(" + conditionString + ")";
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        String[] parts = action.split(" ");
        LinkedHashMap<String, Object> actionData = data();

        if (AttackEntityEnum.fromString(parts[0]) != null) {
            actionData.put("mode", AttackEntityEnum.fromString(parts[0]));
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        if (parts.length > 0) {
            actionData.put("range", parts[0]);
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }
        if (parts.length > 0) {
            actionData.put("value", parts[0]);
            parts = Arrays.copyOfRange(parts, 1, parts.length);
        }

        List<Condition> conditions = new ArrayList<>();
        String conditionString = String.join(" ", parts);

        if (!(conditionString.trim().startsWith("(") && conditionString.trim().contains(")") && conditionString.trim().endsWith("{"))) {
            throw new IllegalArgumentException("Invalid conditional action"); //TODO: change this to a proper exception
        }
        conditionString = conditionString.trim().substring(1, conditionString.trim().length() - 1).replace(")", "").trim();
        String[] conditionParts = conditionString.split(",");

        List<CHTSLImpl> defaultConditions = List.of(Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).filter(a -> a instanceof CHTSLImpl).map(a -> (CHTSLImpl) a).toArray(CHTSLImpl[]::new));

        for (String conditionPart : conditionParts) {
            for (CHTSLImpl condition : defaultConditions) {
                if (conditionPart.startsWith(condition.keyword())) {
                    CHTSLImpl c = (CHTSLImpl) condition.clone();
                    c.importCondition(StringUtilsKt.substringAfter(conditionPart, c.keyword() + " "), nextLines);
                    conditions.add(c);
                    break;
                }
            }
        }

        actionData.put("conditions", ConditionalData.fromList(conditions));

        this.conditions = conditions;
        return nextLines;
    }
}

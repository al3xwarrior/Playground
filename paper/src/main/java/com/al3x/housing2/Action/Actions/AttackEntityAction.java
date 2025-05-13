package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.*;
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

@ToString
public class AttackEntityAction extends HTSLImpl implements NPCAction {
    public AttackEntityAction() {
        super(
                ActionEnum.ATTACK_ENTITY,
                "Attack Entity",
                "Attacks an entity.",
                Material.IRON_SWORD,
                List.of("attackEntity")
        );

        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "mode",
                        "Mode",
                        "The mode of the attack.",
                        AttackEntityEnum.class
                ).setValue(AttackEntityEnum.NEAREST),
                new NumberProperty(
                        "range",
                        "Range",
                        "The range of the attack."
                ).setValue("10"),
                new ConditionsProperty(
                        "conditions",
                        "Conditions",
                        "The conditions to check before attacking."
                ).setValue(new ArrayList<>()),
                new NumberProperty(
                        "value",
                        "Value",
                        "The value of the attack."
                ).setValue("1"),
                new BooleanProperty(
                        "includeExecutor",
                        "Include Executor",
                        "Includes the executor in the attack."
                ).setValue(false)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.ERROR; // Not used
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Double range = getProperty("range", NumberProperty.class).parsedValue(house, player);
        List<Entity> entities = new ArrayList<>(player.getNearbyEntities(range, range, range));
        entities.removeIf(entity -> entity.getLocation().distance(player.getLocation()) > range);

        if (entities.isEmpty()) {
            return OutputType.SUCCESS;
        }
        damageEntities(entities, player.getLocation(), house, player, executor);
        return OutputType.SUCCESS;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Double range = getProperty("range", NumberProperty.class).parsedValue(house, player);
        List<Entity> entities = new ArrayList<>(npc.getEntity().getNearbyEntities(range, range, range));
        if (entities.isEmpty()) {
            return;
        }
        damageEntities(entities, player.getLocation(), house, player, executor);
    }

    public void damageEntities(List<Entity> entities, Location location, HousingWorld house, Player player, ActionExecutor executor) {
        AttackEntityEnum mode = getValue("mode", AttackEntityEnum.class);
        List<Condition> conditions = getProperty("conditions", ConditionsProperty.class).getValue();
        Double damage = getProperty("value", NumberProperty.class).parsedValue(house, player);
        switch (mode) {
            case NEAREST:
                entities.sort(Comparator.comparing((Entity entity) -> entity.getLocation().distance(location)));
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
                        if (conditions.stream().allMatch(condition -> condition.execute(target, house, null, executor) == OutputType.TRUE)) {
                            target.damage(damage);
                        }
                    } else if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                        NPC target = CitizensAPI.getNPCRegistry().getNPC(entity);
                        if (conditions.stream().filter(condition -> condition instanceof NPCCondition).allMatch(condition -> ((NPCCondition) condition).npcExecute(player, target, house, null, executor))) {
                            if (entity instanceof LivingEntity le) {
                                le.damage(damage);
                            }
                        }
                    }
                }
                return;
        }

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                le.damage(damage);
            }
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }
//
//    @Override
//    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
//        String[] parts = action.split(" ");
//        LinkedHashMap<String, Object> actionData = data();
//
//        if (AttackEntityEnum.fromString(parts[0]) != null) {
//            actionData.put("mode", AttackEntityEnum.fromString(parts[0]));
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//        if (parts.length > 0) {
//            actionData.put("range", parts[0]);
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//        if (parts.length > 0) {
//            actionData.put("value", parts[0]);
//            parts = Arrays.copyOfRange(parts, 1, parts.length);
//        }
//
//        List<Condition> conditions = new ArrayList<>();
//        String conditionString = String.join(" ", parts);
//
//        if (!(conditionString.trim().startsWith("(") && conditionString.trim().contains(")") && conditionString.trim().endsWith("{"))) {
//            throw new IllegalArgumentException("Invalid conditional action"); //TODO: change this to a proper exception
//        }
//        conditionString = conditionString.trim().substring(1, conditionString.trim().length() - 1).replace(")", "").trim();
//        String[] conditionParts = conditionString.split(",");
//
//        List<CHTSLImpl> defaultConditions = List.of(Arrays.stream(ConditionEnum.values()).map(ConditionEnum::getConditionInstance).filter(a -> a instanceof CHTSLImpl).map(a -> (CHTSLImpl) a).toArray(CHTSLImpl[]::new));
//
//        for (String conditionPart : conditionParts) {
//            for (CHTSLImpl condition : defaultConditions) {
//                if (conditionPart.startsWith(condition.keyword())) {
//                    CHTSLImpl c = (CHTSLImpl) condition.clone();
//                    c.importCondition(StringUtilsKt.substringAfter(conditionPart, c.keyword() + " "), nextLines);
//                    conditions.add(c);
//                    break;
//                }
//            }
//        }
//
//        actionData.put("conditions", ConditionalData.fromList(conditions));
//
//        this.conditions = conditions;
//        return nextLines;
//    }
}

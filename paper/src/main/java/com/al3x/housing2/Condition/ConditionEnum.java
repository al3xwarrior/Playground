package com.al3x.housing2.Condition;

import com.al3x.housing2.Condition.Conditions.*;
import com.al3x.housing2.Condition.Conditions.IsSneakingCondition;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ConditionEnum {
    CLICKTYPE_REQUIREMENT("Click Type Requirement", ClickTypeCondition.class),
    DAMAGE_TYPE("Damage Type", DamageTypeCondition.class),
    DAMAGE_AMOUNT("Damage Amount", DamageAmountCondition.class),
    INTERACTION_TYPE("Interaction Type", InteractionTypeCondition.class),
    BLOCK_TYPE("Block Type", BlockTypeCondition.class),
    REQUIRED_GROUP("Required Group", GroupRequirementCondition.class),
    STAT_REQUIREMENT("Stat Requirement", StatRequirementCondition.class),
    NPC_STAT_REQUIREMENT("NPC Stat Requirement", NPCStatRequirementCondition.class),
    GLOBALSTAT_REQUIREMENT("Global Stat Requirement", GlobalStatRequirementCondition.class),
    HAS_PERMISSION("Has Permission", HasPermissionCondition.class),
    WITHIN_REGION("Within Region", WithinRegionCondition.class),
    HAS_ITEM("Has Item", HasItemCondition.class),
    HAS_POTION_EFFECT("Has Potion Effect", HasPotionEffectCondition.class),
    IS_SNEAKING("Is Sneaking", IsSneakingCondition.class),
    IS_FLYING("Is Flying", IsFlyingCondition.class),
    IS_GLIDING("Is Gliding", IsGlidingCondition.class),
    IS_NPC_HIDDEN("Is NPC Hidden", IsNPCHiddenCondition.class),
    HEALTH_REQUIREMENT("Health Requirement", HealthRequirementCondition.class),
    MAXHEALTH_REQUIREMENT("Max Health Requirement", MaxHealthRequirementCondition.class),
    HUNGER_REQUIREMENT("Hunger Requirement", HungerRequirementCondition.class),
    GAMEMODE_REQUIREMENT("Gamemode Requirement", GamemodeRequirementCondition.class),
    PLACEHOLDER_REQUIREMENT("Placeholder Requirement", PlaceholderRequirementCondition.class),
    IS_ATTACK_COOLDOWN("Is Attack Cooldown", IsAttackCooldownCondition.class),
    ;
    
    private String name;
    private Class<? extends Condition> condition;

    ConditionEnum(String name, Class<? extends Condition> condition) {
        this.condition = condition;
        this.name = name;
    }

    public Class<? extends Condition> getCondition() {
        return condition;
    }

    public String getName() {
        return name;
    }

    public Condition getConditionInstance(HashMap<String, Object> data) {
        try {
            Condition condition = this.condition.getDeclaredConstructor().newInstance();
            condition.fromData(data, this.condition);
            return condition;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Condition getConditionInstance() {
        try {
            return this.condition.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConditionEnum getConditionByName(String name) {
        for (ConditionEnum condition : ConditionEnum.values()) {
            if (condition.name.equals(name)) {
                return condition;
            }
        }
        return null;
    }
}

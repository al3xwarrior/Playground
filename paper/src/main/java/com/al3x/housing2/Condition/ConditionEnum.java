package com.al3x.housing2.Condition;

import com.al3x.housing2.Condition.Conditions.*;
import com.al3x.housing2.Condition.Conditions.IsSneakingCondition;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Getter
public enum ConditionEnum {
    CLICKTYPE_REQUIREMENT("Click Type Requirement", ClickTypeCondition.class),
    DAMAGE_TYPE("Damage Type", DamageTypeCondition.class),
    DAMAGE_AMOUNT("Damage Amount", DamageAmountCondition.class),
    INTERACTION_TYPE("Interaction Type", InteractionTypeCondition.class),
    BLOCK_TYPE("Block Type Requirement", BlockTypeCondition.class),
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
    IS_SHIELDING("Is Shielding", IsShieldingCondition.class),
    IS_NPC_HIDDEN("Is NPC Hidden", IsNPCHiddenCondition.class),
    HEALTH_REQUIREMENT("Health Requirement", HealthRequirementCondition.class),
    MAXHEALTH_REQUIREMENT("Max Health Requirement", MaxHealthRequirementCondition.class),
    HUNGER_REQUIREMENT("Hunger Requirement", HungerRequirementCondition.class),
    GAMEMODE_REQUIREMENT("Gamemode Requirement", GamemodeRequirementCondition.class),
    PLACEHOLDER_REQUIREMENT("Placeholder Requirement", PlaceholderRequirementCondition.class),
    IS_ATTACK_COOLDOWN("Is Attack Cooldown", IsAttackCooldownCondition.class),
    IS_VOICE_CONNECTED("Is Voice Connected", IsVoiceConnectedCondition.class),
    IS_EATING("Is Eating", IsEatingCondition.class),
    IS_SPRINTING("Is Sprinting", IsSprintingCondition.class),
    ;

    private Class<? extends Condition> condition;
    private String id;

    ConditionEnum(String id, Class<? extends Condition> condition) {
        this.id = id;
        this.condition = condition;
    }

    public Condition getConditionInstance(HashMap<String, Object> data, HousingWorld house) {
        try {
            Condition condition = this.condition.getDeclaredConstructor().newInstance();
            condition.fromData(data, house);
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

    public static ConditionEnum getConditionById(String id) {
        for (ConditionEnum condition : ConditionEnum.values()) {
            if (condition.getId().equals(id)) {
                return condition;
            }
        }
        return null;
    }
}

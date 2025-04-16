package com.al3x.housing2.Condition;

import com.al3x.housing2.Condition.Conditions.*;
import com.al3x.housing2.Condition.Conditions.IsSneakingCondition;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum ConditionEnum {
    CLICKTYPE_REQUIREMENT(ClickTypeCondition.class),
    DAMAGE_TYPE(DamageTypeCondition.class),
    DAMAGE_AMOUNT(DamageAmountCondition.class),
    INTERACTION_TYPE(InteractionTypeCondition.class),
    BLOCK_TYPE(BlockTypeCondition.class),
    REQUIRED_GROUP(GroupRequirementCondition.class),
    STAT_REQUIREMENT(StatRequirementCondition.class),
    NPC_STAT_REQUIREMENT(NPCStatRequirementCondition.class),
    GLOBALSTAT_REQUIREMENT(GlobalStatRequirementCondition.class),
    HAS_PERMISSION(HasPermissionCondition.class),
    WITHIN_REGION(WithinRegionCondition.class),
    HAS_ITEM(HasItemCondition.class),
    HAS_POTION_EFFECT(HasPotionEffectCondition.class),
    IS_SNEAKING(IsSneakingCondition.class),
    IS_FLYING(IsFlyingCondition.class),
    IS_GLIDING(IsGlidingCondition.class),
    IS_SHIELDING(IsShieldingCondition.class),
    IS_NPC_HIDDEN(IsNPCHiddenCondition.class),
    HEALTH_REQUIREMENT(HealthRequirementCondition.class),
    MAXHEALTH_REQUIREMENT(MaxHealthRequirementCondition.class),
    HUNGER_REQUIREMENT(HungerRequirementCondition.class),
    GAMEMODE_REQUIREMENT(GamemodeRequirementCondition.class),
    PLACEHOLDER_REQUIREMENT( PlaceholderRequirementCondition.class),
    IS_ATTACK_COOLDOWN(IsAttackCooldownCondition.class),
    IS_VOICE_CONNECTED(IsVoiceConnectedCondition.class),
    IS_EATING(IsEatingCondition.class),
    IS_SPRINTING(IsSprintingCondition.class),
    ;

    private Class<? extends Condition> condition;

    ConditionEnum(Class<? extends Condition> condition) {
        this.condition = condition;
    }

    public Class<? extends Condition> getCondition() {
        return condition;
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

    public static ConditionEnum getConditionById(String id) {
        for (ConditionEnum condition : ConditionEnum.values()) {
            if (condition.name().equals(id)) {
                return condition;
            }
        }
        return null;
    }
}

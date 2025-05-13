package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.PotionProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.PaginationMenu;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class HasPotionEffectCondition extends CHTSLImpl implements NPCCondition {
    public HasPotionEffectCondition() {
        super(ConditionEnum.HAS_POTION_EFFECT,
                "Has Potion Effect",
                "Requires the user to have the specified potion effect.",
                Material.POTION,
                List.of("hasEffect")
        );

        getProperties().add(
                new PotionProperty(
                        "potionEffect",
                        "Potion Effect",
                        "The potion effect to check for."
                ).setValue(PotionEffectType.SPEED)
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return player.hasPotionEffect(getValue("potionEffect", PotionEffectType.class)) ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity le) {
            return le.hasPotionEffect(getValue("potionEffect", PotionEffectType.class));
        } else {
            return false;
        }
    }
}

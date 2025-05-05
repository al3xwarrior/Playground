package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Enums.AttributeType;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.AttributeTrait;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
@ToString
public class ChangeNPCAttributeAction extends HTSLImpl implements NPCAction {
    public ChangeNPCAttributeAction() {
        super(
                ActionEnum.CHANGE_NPC_ATTRIBUTE,
                "Change NPC Attribute",
                "Changes the NPC's attribute.",
                Material.HOPPER,
                List.of("npcAttribute")
        );

        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "attribute",
                        "Attribute",
                        "The attribute to change.",
                        AttributeType.class
                ).setValue(AttributeType.ARMOR),
                new NumberProperty(
                        "value",
                        "Value",
                        "The value to set the attribute to."
                ).setValue("1")
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
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Double value = getProperty("value", NumberProperty.class).parsedValue(house, player);
        AttributeType attribute = getValue("attribute", AttributeType.class);
        try {
            if (attribute.getAttribute().equals(Attribute.FLYING_SPEED)) {
                npc.getNavigator().getDefaultParameters().baseSpeed(value.floatValue());
                return;
            }

            AttributeTrait attributeTrait = npc.getOrAddTrait(AttributeTrait.class);
            attributeTrait.setAttributeValue(attribute.getAttribute(), value.floatValue());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to change NPC attribute: " + e.getMessage());
        }
    }

    @Override
    public boolean hide() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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

    private AttributeType attribute = AttributeType.ARMOR;
    private String value = "1";

    public ChangeNPCAttributeAction() {
        super(
                "change_npc_attribute_action",
                "Change NPC Attribute",
                "Changes the NPC's attribute.",
                Material.HOPPER,
                List.of("npcAttribute")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "attribute",
                        "Attribute",
                        "The attribute to change.",
                        ActionProperty.PropertyType.ENUM,
                        AttributeType.class
                ),
                new ActionProperty(
                        "value",
                        "Value",
                        "The value to set the attribute to.",
                        ActionProperty.PropertyType.STRING
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
    public String export(int indent) {
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + attribute.name() + " " + Color.removeColor(value);
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        try {
            if (attribute.getAttribute().equals(Attribute.FLYING_SPEED)) {
                npc.getNavigator().getDefaultParameters().baseSpeed(Float.parseFloat(Placeholder.handlePlaceholders(value, house, player)));
                return;
            }

            AttributeTrait attributeTrait = npc.getOrAddTrait(AttributeTrait.class);
            attributeTrait.setAttributeValue(attribute.getAttribute(), Double.parseDouble(Placeholder.handlePlaceholders(value, house, player)));
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to change NPC attribute: " + e.getMessage());
        }
    }

    @Override
    public boolean hide() {
        return true;
    }
}

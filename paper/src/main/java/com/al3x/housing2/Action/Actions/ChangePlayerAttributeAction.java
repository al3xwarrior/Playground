package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Enums.AttributeType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@ToString
public class ChangePlayerAttributeAction extends HTSLImpl {


    public ChangePlayerAttributeAction() {
        super(
                ActionEnum.CHANGE_PLAYER_ATTRIBUTE,
                "Change Player Attribute",
                "Changes the player's attribute.",
                Material.HOPPER,
                List.of("playerAttribute")
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
        Double value = getProperty("value", NumberProperty.class).parsedValue(house, player);
        AttributeType attribute = getValue("attribute", AttributeType.class);
        try {
            if (attribute.getAttribute().equals(Attribute.FLYING_SPEED)) {
                player.setFlySpeed(value.floatValue());
                return OutputType.SUCCESS;
            }

            AttributeInstance attributeInstance = player.getAttribute(attribute.getAttribute());
            if (attributeInstance == null) return OutputType.SUCCESS;

            attributeInstance.setBaseValue(value.floatValue());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to change player attribute: " + e.getMessage());
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

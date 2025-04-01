package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
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

    @Setter
    private AttributeType attribute;
    private String value;

    public ChangePlayerAttributeAction() {
        super(
                "change_player_attribute_action",
                "Change Player Attribute",
                "Changes the player's attribute.",
                Material.HOPPER,
                List.of("playerAttribute")
        );

        attribute = AttributeType.ARMOR;
        value = "1";

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
                        "The value to set.",
                        ActionProperty.PropertyType.STRING
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        try {
            if (attribute.getAttribute().equals(Attribute.FLYING_SPEED)) {
                player.setFlySpeed(Float.parseFloat(Placeholder.handlePlaceholders(value, house, player)));
                return OutputType.SUCCESS;
            }

            AttributeInstance attributeInstance = player.getAttribute(attribute.getAttribute());
            if (attributeInstance == null) return OutputType.SUCCESS;

            attributeInstance.setBaseValue(Double.parseDouble(Placeholder.handlePlaceholders(value, house, player)));
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to change player attribute: " + e.getMessage());
        }
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("attribute", attribute == null ? AttributeType.ARMOR.name() : attribute.name());
        data.put("value", value);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        attribute = AttributeType.valueFrom((String) data.get("attribute")) == null ? AttributeType.ARMOR : AttributeType.valueFrom((String) data.get("attribute"));
        value = (String) data.get("value");
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + getScriptingKeywords().getFirst() + " " + attribute.name() + " " + Color.removeColor(value);
    }
}

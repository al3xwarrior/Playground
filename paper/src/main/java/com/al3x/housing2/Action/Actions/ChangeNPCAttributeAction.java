package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.AttributeType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Color;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.AttributeTrait;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChangeNPCAttributeAction extends HTSLImpl implements NPCAction {

    private AttributeType attribute;
    private String value;

    public ChangeNPCAttributeAction() {
        super("Change NPC Attribute Action");
        attribute = AttributeType.ARMOR;
        value = "1";
    }

    @Override
    public String toString() {
        return "ChangeNPCAttributeAction (mode: " + attribute + " value: " + value + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.HOPPER);
        builder.name("&eChange NPC Attribute");
        builder.info("&eSettings", "");
        builder.info("Mode", attribute.name());
        builder.info("Value", value);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.HOPPER);
        builder.name("&aChange NPC Attribute");
        builder.description("Adjust the NPC's attributes.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&eChange NPC Attribute Action Settings", new ArrayList<>());
        }
        List<ActionEditor.ActionItem> items = new ArrayList<>();

        items.add(new ActionEditor.ActionItem("attribute",
                ItemBuilder.create(Material.REPEATING_COMMAND_BLOCK)
                        .name("&eAttribute")
                        .info("&7Current Value", "")
                        .info(null, "&a" + attribute.name())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, AttributeType.values(), null));
        items.add(new ActionEditor.ActionItem("value",
                ItemBuilder.create(Material.HOPPER)
                        .name("&eValue")
                        .info("&7Current Value", "")
                        .info(null, "&a" + value)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING
        ));
        return new ActionEditor(6, "&eChange Attribute Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return true;
    }

    public AttributeType getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeType attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
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
        return " ".repeat(indent) + keyword() + " " + attribute.name() + " " + Color.removeColor(value);
    }

    @Override
    public String keyword() {
        return "npcAttribute";
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
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

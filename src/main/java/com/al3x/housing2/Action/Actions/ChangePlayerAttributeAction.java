package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.AttributeType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.*;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChangePlayerAttributeAction extends HTSLImpl {
    private static Gson gson = new Gson();

    private AttributeType attribute;
    private String value;

    public ChangePlayerAttributeAction() {
        super("Change Player Attribute Action");
        attribute = AttributeType.GENERIC_ARMOR;
        value = "1";
    }

    @Override
    public String toString() {
        return "ChangePlayerAttributeAction (mode: " + attribute + ", value: " + value + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.HOPPER);
        builder.name("&eChange Player Attribute");
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
        builder.name("&aChange Player Attribute");
        builder.description("Adjust the player's attributes.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&eChange Player Attribute Action Settings", new ArrayList<>());
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
        try {
            player.getAttribute(Attribute.valueOf(attribute.name())).setBaseValue(Double.valueOf(Placeholder.handlePlaceholders(value, house, player)));
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to change player attribute: " + e.getMessage());
        }
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
        data.put("attribute", attribute.name());
        data.put("value", value);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        attribute = AttributeType.valueOf((String) data.get("attribute"));
        value = (String) data.get("value");
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + keyword() + " " + attribute.name() + " " + Color.removeColor(value);
    }

    @Override
    public String keyword() {
        return "changePlayerSize";
    }
}

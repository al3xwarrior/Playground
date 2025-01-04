package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Instances.HousingData.PlayerData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GroupRequirementCondition extends Condition {
    private String group = null;
    boolean includeHigherGroups = false;


    public GroupRequirementCondition() {
        super("Required Group");
    }

    @Override
    public String toString() {
        return "GroupRequirementCondition (group: " + (group == null ? "&cNone" : "&6" + group) + ", includeHigherGroups: " + includeHigherGroups + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&aRequired Group");
        builder.description("Requires players to match the required group (or higher if set).");
        builder.info("Group", (group == null ? "&cNone" : "&6" + group));
        builder.info("Include Higher Groups", includeHigherGroups ? "&aYes" : "&cNo");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eRequired Group");
        builder.description("Requires players to match the required group (or higher if set).");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("group",
                        ItemBuilder.create(Material.DIAMOND)
                                .name("&eGroup")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (group == null ? "None" : group))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.GROUP
                ),
                new ActionEditor.ActionItem("includeHigherGroups",
                        ItemBuilder.create((includeHigherGroups) ? Material.LIME_DYE : Material.RED_DYE)
                                .name("&eInclude Higher Groups")
                                .info("&7Current Value", "")
                                .info(null, includeHigherGroups ? "&aYes" : "&cNo")
                                .lClick(ItemBuilder.ActionType.TOGGLE_YELLOW),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        PlayerData playerData = house.loadOrCreatePlayerData(player);
        if (includeHigherGroups) {
            return playerData.getGroupInstance(house).getPriority() >= house.getGroup(group).getPriority();
        }
        return playerData.getGroupInstance(house).getPriority() == house.getGroup(group).getPriority();
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("group", group);
        data.put("includeHigherGroups", includeHigherGroups);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

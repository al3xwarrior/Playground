package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.GenericPagination.GroupProperty;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Data.PlayerData;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class GroupRequirementCondition extends CHTSLImpl {
    public GroupRequirementCondition() {
        super(ConditionEnum.REQUIRED_GROUP,
                "Group Requirement",
                "Checks if the player is in the specified group.",
                Material.DIAMOND_SWORD,
                List.of("group")
        );

        getProperties().add(new GroupProperty(
                "group",
                "Group",
                "The group to check for."
        ));

        getProperties().add(new BooleanProperty(
                "includeHigherGroups",
                "Include Higher Groups",
                "If true, the player will be considered in the group if they are in a higher group."
        ).setValue(false));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Group group = getProperty("group", GroupProperty.class).getValue();
        boolean includeHigherGroups = getValue("includeHigherGroups", Boolean.class);
        PlayerData playerData = house.loadOrCreatePlayerData(player);
        if (includeHigherGroups) {
            return playerData.getGroupInstance(house).getPriority() >= group.getPriority() ? OutputType.TRUE : OutputType.FALSE;
        }
        return playerData.getGroupInstance(house).getPriority() == group.getPriority() ? OutputType.TRUE : OutputType.FALSE;

    }
    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

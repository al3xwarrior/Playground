package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.BooleanProperty;
import com.al3x.housing2.Action.Properties.GenericPagination.GroupProperty;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Group;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@ToString
public class ChangePlayerGroupAction extends HTSLImpl {
    public ChangePlayerGroupAction() {
        super(
                "change_player_group_action",
                "Change Player Group",
                "Changes the player's group.",
                Material.PLAYER_HEAD,
                List.of("group")
        );

        getProperties().addAll(List.of(
                new GroupProperty(
                        "group",
                        "Group",
                        "The group to change the player to."
                ),
                new BooleanProperty(
                        "demotionProtection",
                        "Demotion Protection",
                        "If enabled, the player cannot be demoted if their priority is higher than the group they are being changed to."
                ).setValue(false)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //does nothing
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        Group group = getValue("group", Group.class);
        boolean demotionProtection = getValue("demotionProtection", Boolean.class);
        if (group == null) {
            return OutputType.ERROR;
        }
        if (house.getOwnerUUID() == player.getUniqueId()) {
            return OutputType.ERROR;
        }
        if (house.loadOrCreatePlayerData(player).getGroupInstance(house).getPriority() > group.getPriority() && demotionProtection) {
            return OutputType.ERROR;
        }
        house.loadOrCreatePlayerData(player).setGroup(group.getName());
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

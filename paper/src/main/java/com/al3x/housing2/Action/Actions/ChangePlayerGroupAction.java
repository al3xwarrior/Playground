package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
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
    String group = null;
    boolean demotionProtection = false;

    public ChangePlayerGroupAction() {
        super(
                "change_player_group_action",
                "Change Player Group",
                "Changes the player's group.",
                Material.PLAYER_HEAD,
                List.of("group")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "group",
                        "Group",
                        "The group to change the player to.",
                        ActionProperty.PropertyType.GROUP
                ),
                new ActionProperty(
                        "demotionProtection",
                        "Demotion Protection",
                        "If enabled, the player cannot be demoted.",
                        ActionProperty.PropertyType.BOOLEAN
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        return OutputType.SUCCESS; //does nothing
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (group == null) {
            return OutputType.ERROR;
        }
        if (house.getOwnerUUID() == player.getUniqueId()) {
            return OutputType.ERROR;
        }
        Group group = house.getGroup(this.group);
        if (house.loadOrCreatePlayerData(player).getGroupInstance(house).getPriority() > group.getPriority() && demotionProtection) {
            return OutputType.ERROR;
        }
        house.loadOrCreatePlayerData(player).setGroup(group.getName());
        return OutputType.SUCCESS;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        group = (String) data.get("group");
        demotionProtection = (boolean) data.get("demotionProtection");
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

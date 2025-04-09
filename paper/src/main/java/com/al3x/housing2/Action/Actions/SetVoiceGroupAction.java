package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.VoiceChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class SetVoiceGroupAction extends HTSLImpl {
    private String groupName = "Cool group";

    public SetVoiceGroupAction() {
        super(
                "set_voice_group_action",
                "Set Voice Group",
                "Sets the voice group of a player.",
                Material.JUKEBOX,
                List.of("setGroup")
        );

        getProperties().add(
                new ActionProperty(
                        "groupName",
                        "Group Name",
                        "The name of the voice group.",
                        ActionProperty.PropertyType.STRING
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String groupName = HandlePlaceholders.parsePlaceholders(player, house, this.groupName);
        VoiceChat.setPlayerGroup(player, groupName);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

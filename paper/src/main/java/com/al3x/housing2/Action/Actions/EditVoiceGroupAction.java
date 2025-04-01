package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Enums.VoiceGroupTypes;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.VoiceChat;
import de.maxhenkel.voicechat.api.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class EditVoiceGroupAction extends HTSLImpl {
    @Setter
    private VoiceGroupTypes type = VoiceGroupTypes.NORMAL;
    private String groupName = "Cool group";

    public EditVoiceGroupAction() {
        super(
                "edit_voice_group_action",
                "Edit Voice Group",
                "Edits a voice group's type.",
                Material.MUSIC_DISC_WAIT,
                List.of("editVoiceGroup")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "type",
                        "Type",
                        "The type of the voice group.",
                        ActionProperty.PropertyType.ENUM, VoiceGroupTypes.class
                ),
                new ActionProperty(
                        "groupName",
                        "Group Name",
                        "The name of the voice group.",
                        ActionProperty.PropertyType.STRING
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String groupName = HandlePlaceholders.parsePlaceholders(player, house, this.groupName);
        Group group = VoiceChat.getGroup(house.getWorld(), groupName);

        switch (type) {
            case VoiceGroupTypes.ISOLATED -> VoiceChat.setGroupType(group, Group.Type.ISOLATED);
            case VoiceGroupTypes.NORMAL -> VoiceChat.setGroupType(group, Group.Type.NORMAL);
            case VoiceGroupTypes.OPEN -> VoiceChat.setGroupType(group, Group.Type.OPEN);
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        type = VoiceGroupTypes.valueOf((String) data.get("type"));
        groupName = (String) data.get("groupName");
    }
}

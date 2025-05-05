package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.EnumProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
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
    public EditVoiceGroupAction() {
        super(
                ActionEnum.EDIT_VOICE_GROUP,
                "Edit Voice Group",
                "Edits a voice group's type.",
                Material.MUSIC_DISC_WAIT,
                List.of("editVoiceGroup")
        );

        getProperties().addAll(List.of(
                new EnumProperty<>(
                        "type",
                        "Type",
                        "The type of the voice group.",
                        VoiceGroupTypes.class
                ).setValue(VoiceGroupTypes.NORMAL),
                new StringProperty(
                        "groupName",
                        "Group Name",
                        "The name of the voice group."
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String groupName = getProperty("groupName", StringProperty.class).parsedValue(house, player);
        Group group = VoiceChat.getGroup(house.getWorld(), groupName);

        if (group == null) return OutputType.ERROR;

        switch (getValue("type", VoiceGroupTypes.class)) {
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
}

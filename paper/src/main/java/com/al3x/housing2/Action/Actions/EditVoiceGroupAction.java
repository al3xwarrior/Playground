package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Enums.VoiceGroupTypes;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.*;
import de.maxhenkel.voicechat.api.Group;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class EditVoiceGroupAction extends HTSLImpl {
    private VoiceGroupTypes type;
    private String groupName;

    public EditVoiceGroupAction() {
        super("Edit Voice Group Action");
        type = VoiceGroupTypes.NORMAL;
        groupName = "Cool group";
    }

    @Override
    public String toString() {
        return "EditVoiceGroupAction (type: " + type + ", " + groupName + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.MUSIC_DISC_WAIT);
        builder.name("&eEdit Voice Group");
        builder.info("&eSettings", "");
        builder.info("Type", type.name());
        builder.info("Group Name", groupName);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.MUSIC_DISC_WAIT);
        builder.name("&eEdit Voice Group");
        builder.description("Change a voice group's type.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        if (backMenu == null) {
            return new ActionEditor(6, "&eEdit Voice Group Action Settings", new ArrayList<>());
        }
        List<ActionEditor.ActionItem> items = new ArrayList<>();

        items.add(new ActionEditor.ActionItem("type",
                ItemBuilder.create(Material.NOTE_BLOCK)
                        .name("&eType")
                        .info("&7Current Value", "")
                        .info(null, "&a" + type.name())
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.ENUM, VoiceGroupTypes.values(), null));
        items.add(new ActionEditor.ActionItem("groupName",
                ItemBuilder.create(Material.PAPER)
                        .name("&eGroup Name")
                        .info("&7Current Value", "")
                        .info(null, "&a" + groupName)
                        .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                ActionEditor.ActionItem.ActionType.STRING
        ));
        return new ActionEditor(6, "&eEdit Voice Group Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        String groupName = HandlePlaceholders.parsePlaceholders(player, house, this.groupName);
        Group group = VoiceChat.getGroup(house.getWorld(), groupName);

        switch (type) {
            case VoiceGroupTypes.ISOLATED -> VoiceChat.setGroupType(group, Group.Type.ISOLATED);
            case VoiceGroupTypes.NORMAL -> VoiceChat.setGroupType(group, Group.Type.NORMAL);
            case VoiceGroupTypes.OPEN -> VoiceChat.setGroupType(group, Group.Type.OPEN);
        }
        return true;
    }



    public VoiceGroupTypes getType() {
        return type;
    }

    public void setType(VoiceGroupTypes type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("type", type.name());
        data.put("groupName", groupName);
        return data;
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

    @Override
    public String keyword() {
        return "editVoiceGroup";
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import com.al3x.housing2.Utils.VoiceChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class SetVoiceGroupAction extends HTSLImpl {

    private String groupName;

    public SetVoiceGroupAction() {
        super("Set Voice Group Action");
        this.groupName = "Cool group";
    }

    public SetVoiceGroupAction(String groupName) {
        super("Set Voice Group Action");
        this.groupName = groupName;
    }

    @Override
    public String keyword() {
        return "setGroup";
    }

    @Override
    public String toString() {
        return "SetVoiceGroupAction (Group Name: " + groupName + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.JUKEBOX);
        builder.name("&eSet Voice Group");
        builder.info("&eSettings", "");
        builder.info("Group Name", groupName);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.JUKEBOX);
        builder.name("&aSet Voice Group");
        builder.description("Sets a player's voice group to a named group.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = List.of(
                new ActionItem("groupName",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&aGroup Name")
                                .info("&7Current Value", "")
                                .info(null, groupName)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.STRING
                )
        );
        return new ActionEditor(4, "&eSet Voice Group Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String groupName = HandlePlaceholders.parsePlaceholders(player, house, this.groupName);
        VoiceChat.setPlayerGroup(player, groupName);
        return OutputType.SUCCESS;
    }

    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String name) {
        this.groupName = name;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("groupName", groupName);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class ChatAction extends HTSLImpl {
    private String message = "&eHello World!";

    public ChatAction() {
        super("chat_action");
        displayName("Display Chat Message");
        displayIcon(Material.PAPER);
        description("Sends a chat message to the player.");
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.sendMessage(StringUtilsKt.housingStringFormatter(message, house, player));
        return OutputType.SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.VoiceChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class IsVoiceConnected extends CHTSLImpl {
    public IsVoiceConnected() {
        super("Is Voice Connected");
    }

    @Override
    public String toString() {
        return "IsVoiceConnectedCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.NOTE_BLOCK);
        builder.name("&eIs Voice Connected");
        builder.description("Check if the player is connected to voice chat.");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.NOTE_BLOCK);
        builder.name("&eIs Voice Connected");
        builder.description("Check if the player is connected to voice chat.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return VoiceChat.isPlayerConnected(player);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "isVoiceConnected";
    }
}

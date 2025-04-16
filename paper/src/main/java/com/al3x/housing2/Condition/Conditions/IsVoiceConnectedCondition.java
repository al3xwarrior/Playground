package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.VoiceChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class IsVoiceConnectedCondition extends CHTSLImpl {
    public IsVoiceConnectedCondition() {
        super(ConditionEnum.IS_VOICE_CONNECTED,
                "Is Voice Connected",
                "Check if the player is connected to voice chat.",
                Material.NOTE_BLOCK,
                List.of("isVoiceConnected"));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return VoiceChat.isPlayerConnected(player) ? OutputType.TRUE : OutputType.FALSE;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

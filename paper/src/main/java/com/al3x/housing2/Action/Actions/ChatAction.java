package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;


@Getter
@Setter
@ToString
public class ChatAction extends HTSLImpl {
    private String message = "&eHello world!";

    public ChatAction() {
        super(
                "display_chat_action",
                "Display Chat Message",
                "Displays a chat message to the player.",
                Material.PAPER,
                List.of("chat")
        );

        getProperties().add(new StringProperty(
                "message",
                "Message",
                "The message to display."
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.sendMessage(StringUtilsKt.housingStringFormatter(message, house, player));
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
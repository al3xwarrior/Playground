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
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;


@Getter
@Setter
@ToString
public class ChatAction extends HTSLImpl {

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
        ).setValue("&eHello, world!"));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Component message = getProperty("message", StringProperty.class).component(house, player);
        player.sendMessage(message);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
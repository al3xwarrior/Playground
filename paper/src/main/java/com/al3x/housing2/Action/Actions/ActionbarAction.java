package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@Setter
@Getter
@ToString
public class ActionbarAction extends HTSLImpl {
    private String message = "Hello world!";

    public ActionbarAction() {
        super(
                "actionbar_action",
                "Display Actionbar",
                "Displays a message in the action bar.",
                Material.WRITABLE_BOOK,
                List.of("actionbar")
        );

        getProperties().add(
                new ActionProperty(
                        "message",
                        "Message",
                        "The message to display.",
                        ActionProperty.PropertyType.STRING
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.sendActionBar(StringUtilsKt.housingStringFormatter(message, house, player));
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
@ToString
public class ChangePlayerDisplayNameAction extends HTSLImpl {
    public ChangePlayerDisplayNameAction() {
        super(
                ActionEnum.CHANGE_PLAYER_DISPLAYNAME,
                "Change Player Display Name",
                "Changes the display name of the player.",
                Material.PLAYER_HEAD,
                List.of("displayName")
        );

        getProperties().add(
                new StringProperty("name",
                        "Name",
                        "The name to set."
                ).setValue("%player.name%")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String name = getProperty("name", StringProperty.class).parsedValue(house, player);
        if (house.getWorld().getPlayers().contains(player)) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            if (miniMessage.stripTags(miniMessage.serialize(StringUtilsKt.housingStringFormatter(name, house, player))).length() > 64) {
                return OutputType.ERROR;
            }
            player.playerListName(StringUtilsKt.housingStringFormatter(name, house, player));
            player.displayName(StringUtilsKt.housingStringFormatter(name, house, player));
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

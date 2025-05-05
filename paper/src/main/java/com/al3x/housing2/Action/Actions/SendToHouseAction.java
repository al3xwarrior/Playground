package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;


public class SendToHouseAction extends HTSLImpl {
    public SendToHouseAction(String housingId) {
        this();

        getProperty("housingId", StringProperty.class).setValue(housingId);
    }

    public SendToHouseAction() {
        super(ActionEnum.SEND_TO_HOUSE,
                "Send to House",
                "Sends the player to a housing.",
                Material.SPRUCE_DOOR,
                List.of("sendToHouse")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        String housingId = getProperty("housingId", StringProperty.class).getValue();
        HousesManager housesManager = house.getPlugin().getHousesManager();
        HousingWorld housingWorld = housesManager.getHouse(UUID.fromString(housingId));

        if (housingWorld != null) {
            housingWorld.sendPlayerToHouse(player);
        } else {
            HousingWorld world = housesManager.loadHouse(housingId);
            if (world != null) {
                world.sendPlayerToHouse(player);
            } else {
                player.sendMessage(colorize("&cYou should've been sent to another house, but wasn't because that house couldn't be loaded!"));
            }
         }
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

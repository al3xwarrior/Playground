package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.NumberProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class SetPlayerSlotAction extends HTSLImpl {

    public SetPlayerSlotAction() {
        super(ActionEnum.SET_PLAYER_SLOT,
                "Set Player Slot",
                "Set the player's active hotbar slot.",
                Material.CHEST_MINECART,
                List.of("playerSlot")
        );
        getProperties().addAll(List.of(
                new NumberProperty(
                        "slot",
                        "Slot",
                        "The slot to set the player to.",
                        1, 9
                ).setValue("1")
        ));
    }
    @Override
    public OutputType execute(Player player, HousingWorld house) {
        int slot = getProperty("slot", NumberProperty.class).parsedValue(house, player).intValue();
        player.getInventory().setHeldItemSlot(slot - 1);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

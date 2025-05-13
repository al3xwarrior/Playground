package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class ClearEnderChestAction extends HTSLImpl {

    public ClearEnderChestAction() {
        super(
                ActionEnum.CLEAR_ENDERCHEST,
                "Clear Enderchest",
                "Clears the player's ender chest.",
                Material.ENDER_CHEST,
                List.of("clearEnderChest")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.getEnderChest().clear();
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

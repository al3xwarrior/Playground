package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import lombok.ToString;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@ToString
public class ClearBossbarAction extends HTSLImpl {

    public ClearBossbarAction() {
        super(
                ActionEnum.CLEAR_BOSSBAR,
                "Clear Bossbars",
                "Clears all bossbars from the player's screen.",
                Material.WITHER_SKELETON_SKULL,
                List.of("clearBossbars")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        for (BossBar bossBar : house.bossBars.getOrDefault(player.getUniqueId(), new ArrayList<>())) {
            bossBar.removeViewer(player);
        }
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

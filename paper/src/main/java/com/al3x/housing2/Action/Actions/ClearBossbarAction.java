package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.fromColor;

public class ClearBossbarAction extends HTSLImpl {

    public ClearBossbarAction() {
        super("Clear Bossbars Action");
    }


    @Override
    public String toString() {
        return "ClearBossbarAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.WITHER_SKELETON_SKULL);
        builder.name("&eClear Bossbars");

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.WITHER_SKELETON_SKULL);
        builder.name("&aClear Bossbars");
        builder.description("Clears all bossbars from the player's screen.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        for (BossBar bossBar : house.bossBars.getOrDefault(player.getUniqueId(), new ArrayList<>())) {
            bossBar.removeViewer(player);
        }
        return OutputType.SUCCESS;
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
        return "clearBossbar";
    }
}

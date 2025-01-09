package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class PauseAction extends HTSLImpl {
    double duration = 5.0; // in ticks

    public PauseAction() {
        super("Pause Action");
    }

    @Override
    public String toString() {
        return "PauseAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.CLOCK);
        builder.name("&ePause Execution");
        builder.info("&eSettings", "");
        builder.info("Duration", "&a" + duration + " ticks");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.CLOCK);
        builder.name("&aPause Execution");
        builder.description("Pauses execution of the remaining actions for the specified amount of ticks.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("duration",
                        ItemBuilder.create(Material.CLOCK)
                                .name("&eTicks To Wait")
                                .description("The amount of ticks to wait before continuing. 1 second is 20 ticks.")
                                .info("&7Current Value", "")
                                .info(null, "&a" + duration)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 0.0, 2000.0
                )
        );

        return new ActionEditor(4, "&ePause Action Settings", items);
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        try {
            Thread.sleep((long) duration * 50); // ~50ms per tick
        } catch (InterruptedException e) {
            Bukkit.getLogger().warning("PauseAction was interrupted.");
        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("duration", duration);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "pause";
    }
}

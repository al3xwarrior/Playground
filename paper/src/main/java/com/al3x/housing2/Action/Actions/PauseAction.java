package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class PauseAction extends HTSLImpl implements NPCAction {
    String duration = "5.0"; // in ticks

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
                        ActionEditor.ActionItem.ActionType.STRING
                )
        );

        return new ActionEditor(4, "&ePause Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return true;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("duration", duration);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        if (data.containsKey("duration")) {
            duration = data.get("duration").toString();
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String keyword() {
        return "pause";
    }

    @Override
    public String export(int indent) {
        return "pause " + (duration.contains(" ") ? "\"" + duration + "\"" : duration);
    }

    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        Duple<String[], String> durationArg = handleArg(action.split(" "), 0);
        duration = durationArg.getSecond();
        return nextLines;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {

    }
}

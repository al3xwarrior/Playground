package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetHitDelayAction extends Action {

    private double delay;

    public SetHitDelayAction() {
        super("Set Hit Delay Action");
        this.delay = 10;
    }

    public SetHitDelayAction(int delay) {
        super("Set Hit Delay Action");
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "SetHitDelayAction (Delay: " + this.delay + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("564af0db50a03f5d45a0c6dda47928981bf47ad66dd90a4dd7e92317dbe857d1");
        builder.name("&eSet Hit Delay");
        builder.description("Set the users hit delay. This is the time in ticks between each hit. Default is 10 ticks (0.5 seconds)");
        builder.info("&eSettings", "");
        builder.info("Delay", "&a" + delay);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.skullTexture("564af0db50a03f5d45a0c6dda47928981bf47ad66dd90a4dd7e92317dbe857d1");
        builder.name("&aSet Hit Delay Action");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = Arrays.asList(
                new ActionItem("delay",
                        ItemBuilder.create(Material.BOOK)
                                .name("&aDelay")
                                .info("&7Current Value", "")
                                .info(null, delay)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.INT
                )
        );
        return new ActionEditor(4, "&eHit Delay Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {

        player.setMaximumNoDamageTicks(NumberUtilsKt.toInt(delay));

        //This is not super simple to use lol :)
        player.setNoDamageTicks(NumberUtilsKt.toInt(delay));

        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("delay", delay);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

//    @Override
//    public void fromData(HashMap<String, Object> data) {
//        if (!data.containsKey("message")) return;
//        message = (String) data.get("message");
//    }
}

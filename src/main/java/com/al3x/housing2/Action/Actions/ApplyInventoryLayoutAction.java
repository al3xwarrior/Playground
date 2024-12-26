package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Instances.Function;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Layout;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ApplyInventoryLayoutAction extends Action {
    String layout;
    public ApplyInventoryLayoutAction() {
        super("Apply Inventory Layout Action");
    }

    public ApplyInventoryLayoutAction(String layout, boolean runForAllPlayers) {
        super("Apply Inventory Layout Action");
        this.layout = layout;
    }

    @Override
    public String toString() {
        return "ApplyInventoryLayoutAction (function: " + (layout == null ? "&cNone" : "&6" + layout) + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_AXE);
        builder.name("&eApply Inventory Layout");
        builder.description("Runs a function");
        builder.info("&eSettings", "");
        builder.info("Inventory Layout", (layout == null ? "&cNone" : "&6" + layout));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_AXE);
        builder.name("&aApply Inventory Layout");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("layout", ItemBuilder.create(Material.FILLED_MAP)
                        .name("&aLayout")
                        .info("&7Current Value", "")
                        .info(null, (layout == null ? "&cNone" : "&6" + layout)),
                        ActionEditor.ActionItem.ActionType.LAYOUT
                )
        );
        return new ActionEditor(4, "&eInv Layout Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (layout == null) {
            return false;
        }
        Layout layout = house.getLayout(this.layout);
        if (layout == null) {
            return false;
        }
        layout.apply(player);
        return true;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("layout", layout);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

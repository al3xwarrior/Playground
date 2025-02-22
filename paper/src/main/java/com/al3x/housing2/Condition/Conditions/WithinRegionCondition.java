package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class WithinRegionCondition extends CHTSLImpl {
    private String region = null;

    public WithinRegionCondition() {
        super("Within Region");

    }

    @Override
    public String toString() {
        return "idk";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRASS_BLOCK);
        builder.name("&aWithin Region");
        builder.description("Requires the user to be in the specified region.");
        builder.info("Region", (region == null ? "&aNot Set" : "&6" + region));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GRASS_BLOCK);
        builder.name("&eWithin Region");
        builder.description("Requires the user to be in the specified region.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("region",
                        ItemBuilder.create(Material.GRASS_BLOCK)
                                .name("&eRegion")
                                .info("&7Current Value", "")
                                .info(null, "&a" + (region == null ? "Not Set" : region))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.REGION
                )
        );
        return new ActionEditor(4, "Within Region Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return house.getRegions().stream().filter(region -> region.getPlayersInRegion().contains(player.getUniqueId())).anyMatch(region -> region.getName().equalsIgnoreCase(this.region));
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("region", region);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "inRegion";
    }
}

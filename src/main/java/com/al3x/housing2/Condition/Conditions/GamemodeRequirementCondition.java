package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class GamemodeRequirementCondition extends CHTSLImpl {
    private Gamemodes gamemode;

    public GamemodeRequirementCondition() {
        super("Gamemode Requirement");
        this.gamemode = Gamemodes.SURVIVAL;
    }

    @Override
    public String toString() {
        return "GamemodeRequirementCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.DAYLIGHT_DETECTOR);
        builder.name("&aGamemode Requirement");
        builder.description("Requires the user to be in the specified gamemode.");
        builder.info("Gamemode", gamemode.toString());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DAYLIGHT_DETECTOR);
        builder.name("&eGamemode Requirement");
        builder.description("Requires the user to be in the specified gamemode.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("gamemode",
                        ItemBuilder.create(Material.DAYLIGHT_DETECTOR)
                                .name("&eGamemode")
                                .info("&7Current Value", "")
                                .info(null, "&a" + gamemode)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, Gamemodes.values(), null
                )
        );
        return new ActionEditor(4, "Gamemode Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return (player.getGameMode() == gamemode.getGameMode());
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("gamemode", gamemode);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "gamemode";
    }
}

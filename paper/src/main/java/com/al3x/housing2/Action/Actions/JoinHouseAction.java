package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Data.HouseData;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HTSLHandler;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingNPC;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.Duple;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;


public class JoinHouseAction extends HTSLImpl {
    private String housingId;

    public JoinHouseAction(String housingId) {
        super("Join House Action");

        this.housingId = housingId;
    }

    public JoinHouseAction() {
        super("Join House Action");
    }

    /*private HouseData getTargetHouseData(Main plugin) {
        return plugin.getHousesManager().getHouseData(this.housingId);
    }*/

    @Override
    public String toString() {
        return "JoinHousingAction{" +
                "housingId=" + housingId +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.SPRUCE_DOOR);
        builder.name("&eJoin House Action");
        builder.description("Send the player to a Housing");
        builder.info("&eSettings", "");
        builder.info("Housing ID", housingId);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.SPRUCE_DOOR);
        builder.name("&aJoin House Action");
        builder.description("Send the player to a Housing");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = List.of(
                new ActionEditor.ActionItem("housingId",
                        ItemBuilder.create(Material.SPRUCE_DOOR)
                                .name("&eHousing ID")
                                .info("&7Current Value", "&e" + housingId)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.HOUSE
                )
        );

        return new ActionEditor(4, "&eJoin Housing Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        HousesManager housesManager = house.getPlugin().getHousesManager();
        HousingWorld housingWorld = housesManager.getHouse(UUID.fromString(housingId));

        if (housingWorld != null) {
            housingWorld.sendPlayerToHouse(player);
        } else {
            HousingWorld world = housesManager.loadHouse(housingId);
            if (world != null) {
                world.sendPlayerToHouse(player);
            } else {
                player.sendMessage(colorize("&cWe couldn't load this house!"));
            }
         }
        return OutputType.SUCCESS; // Not used
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("housingId", housingId);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        this.housingId = String.valueOf(data.get("housingId"));
    }

    @Override
    public String syntax() {
        return "joinHouse <houseID>";
    }

    @Override
    public String keyword() {
        return "joinHouse";
    }

    /*
    TODO: check housing id for owner, etc. (similar to how the menu does it)
    @Override
    public ArrayList<String> importAction(String action, String indent, ArrayList<String> nextLines) {
        if (action.contains(" ")) {
            Duple<String[], String> housingIdArg = handleArg(action.split(" "), 0);
            this.housingId = housingIdArg.getSecond();
        }

        return nextLines;
    }

    @Override
    public String export(int indent) {
        return " ".repeat(indent) + keyword() + " \"" + housingId + "\"";
    }
     */
}

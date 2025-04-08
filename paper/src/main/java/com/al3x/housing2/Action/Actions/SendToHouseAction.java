package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;


public class SendToHouseAction extends HTSLImpl {
    private String housingId;

    public SendToHouseAction(String housingId) {
        super("Send to House Action");

        this.housingId = housingId;
    }

    public SendToHouseAction() {
        super("Send to House Action");
    }

    @Override
    public String toString() {
        return "SendToHouseAction{" +
                "housingId=" + housingId +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.SPRUCE_DOOR);
        builder.name("&eSend to House Action");
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
        builder.name("&eSend to House Action");
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

        return new ActionEditor(4, "&eSend to House Action Settings", items);
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
        return "sendToHouse <houseID>";
    }

    @Override
    public String keyword() {
        return "sendToHouse";
    }
}

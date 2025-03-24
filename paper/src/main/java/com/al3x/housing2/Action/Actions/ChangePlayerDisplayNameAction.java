package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.HandlePlaceholders.parsePlaceholders;

public class ChangePlayerDisplayNameAction extends HTSLImpl {

    private String name;

    public ChangePlayerDisplayNameAction() {
        super("Change Player Display Name Action");
        this.name = "%player.name%";
    }

    @Override
    public String keyword() {
        return "displayName";
    }

    @Override
    public String toString() {
        return "ChangePlayerDisplayNameAction (Name: " + name + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eChange Player Display Name");
        builder.info("&eSettings", "");
        builder.info("Name", name);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&aChange Player Display Name");
        builder.description("Changes the display name of the player.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = Arrays.asList(
                new ActionItem("name",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&aDisplay Name")
                                .info("&7Current Value", "")
                                .info(null, name)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.STRING
                )
        );
        return new ActionEditor(4, "&eDisplay Name Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (house.getWorld().getPlayers().contains(player)) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            if (miniMessage.stripTags(miniMessage.serialize(StringUtilsKt.housingStringFormatter(name, house, player))).length() > 64) {
                return OutputType.ERROR;
            }
            player.playerListName(StringUtilsKt.housingStringFormatter(name, house, player));
            player.displayName(StringUtilsKt.housingStringFormatter(name, house, player));
        }
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
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

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.colorizeLegacyText;

public class ActionbarAction extends HTSLImpl {

    private String message;

    public ActionbarAction() {
        super("Actionbar Action");
        this.message = "&aHello World!";
    }

    public ActionbarAction(String message) {
        super("Actionbar Action");
        this.message = message;
    }

    @Override
    public String keyword() {
        return "actionBar";
    }

    @Override
    public String toString() {
        return "ActionBar (Message: " + message + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.WRITABLE_BOOK);
        builder.name("&eDisplay Action Bar");
        builder.info("&eSettings", "").info("Message", message);
        builder.lClick(ActionType.EDIT_YELLOW).rClick(ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.WRITABLE_BOOK);
        builder.name("&aDisplay Action Bar");
        builder.lClick(ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = List.of(
                new ActionItem("message",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&aMessage")
                                .info("&7Current Value", "")
                                .info(null, message)
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.STRING
                )
        );
        return new ActionEditor(4, "&eActionbar Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.sendActionBar(StringUtilsKt.housingStringFormatter(message, house, player));
        return OutputType.SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("message", message);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

//    @Override
//    public void fromData(HashMap<String, Object> data) {
//        if (!data.containsKey("message")) {
//            return;
//        }
//        message = (String) data.get("message");
//    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.HandlePlaceholders.parsePlaceholders;

public class ChatAction extends Action {

    private String message;

    public ChatAction() {
        super("Chat Action");
        this.message = "&eHello World!";
    }

    public ChatAction(String message) {
        super("Chat Action");
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatAction (Message: " + message + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PAPER);
        builder.name("&eSend a chat message");
        builder.description("Sends a chat message");
        builder.info("&eSettings", "");
        builder.info("Message", message);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PAPER);
        builder.name("&aSend a Chat Message");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = Arrays.asList(
                new ActionItem("message",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&aMessage")
                                .info("&7Current Value", "")
                                .info(null, message)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.STRING
                )
        );
        return new ActionEditor(4, "&eChat Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.sendMessage(colorize(parsePlaceholders(player, house, message)));
        return true;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("message", message);
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

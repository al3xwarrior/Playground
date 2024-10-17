package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorizeLegacyText;

public class ActionbarAction extends Action {

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
    public String toString() {
        return "ActionBar (Message: " + message + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.WRITABLE_BOOK);
        builder.name("&eActionbar Action").description("Sends a actionbar message");
        builder.info("Settings", "").info("Message", message);
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
    public boolean execute(Player player, HousingWorld house) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorizeLegacyText(message)));

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

//    @Override
//    public void fromData(HashMap<String, Object> data) {
//        if (!data.containsKey("message")) {
//            return;
//        }
//        message = (String) data.get("message");
//    }
}

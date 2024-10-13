package com.al3x.housing2.Actions;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;

import java.util.Arrays;
import java.util.HashMap;

import static com.al3x.housing2.Utils.Color.colorize;

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
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eChat Message Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Sends a chat message"),
                "",
                colorize("&eSettings:"),
                colorize("&fMessage: " + message),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.sendMessage(colorize(message));
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
//        if (!data.containsKey("message")) return;
//        message = (String) data.get("message");
//    }
}

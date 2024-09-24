package com.al3x.housing2.Actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class ChatAction implements Action{

    private String message;

    public ChatAction() {
        this.message = "&eHello World!";
    }

    public ChatAction(String message) {
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
    public void execute(Player player) {
        player.sendMessage(colorize(message));
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

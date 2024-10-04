package com.al3x.housing2.Actions;

import com.al3x.housing2.Instances.HousingWorld;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;

import java.awt.*;
import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.colorizeLegacyText;

public class ActionbarAction implements Action{

    private String message;

    public ActionbarAction() {
        this.message = "&eHello World!";
    }

    public ActionbarAction(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ActionbarAction (Message: " + message + ")";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eActionbar Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Sends a actionbar message"),
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
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorizeLegacyText(message)));

        return true;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

package com.al3x.housing2.Actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class SendTitleAction implements Action{

    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public SendTitleAction() {
        this.title = "Title";
        this.subtitle = "Subtitle";
        this.fadeIn = 20;
        this.stay = 20;
        this.fadeOut = 20;
    }

    public SendTitleAction(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = 20;
        this.stay = 20;
        this.fadeOut = 20;
    }

    public SendTitleAction(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public String toString() {
        return "SendTitleAction (Title: " + title + ", Subtitle: " + subtitle + ", FadeIn: " + fadeIn + ", Stay: " + stay + ", FadeOut: " + fadeOut + ")";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eSend Title Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Displays a Title and Subtitle to the player with"),
                colorize("&7a defined &fFade In&7, &fStay&7, and &fFade Out"),
                colorize("&7time."),
                "",
                colorize("&eSettings:"),
                colorize("&fTitle: " + title),
                colorize("&fSubtitle: " + subtitle),
                colorize("&fFade In Time: &6" + fadeIn + " ticks"),
                colorize("&fStay Time: &6" + stay + " ticks"),
                colorize("&fFade Out Time: &6" + fadeOut + " ticks"),
                "",
                colorize("&eLeft Click to edit!"),
                colorize("&eRight Click to remove!"),
                colorize("&7Use shift and left/right click to change order.")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public boolean execute(Player player) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        return true;
    }

    public String getTitle() {
        return title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public int getFadeIn() {
        return fadeIn;
    }
    public int getFadeOut() {
        return fadeOut;
    }
    public int getStay() {
        return stay;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }
    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }
    public void setStay(int stay) {
        this.stay = stay;
    }
}

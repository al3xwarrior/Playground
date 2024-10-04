package com.al3x.housing2.Actions;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.BatchUpdateException;
import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.colorizeLegacyText;

public class ShowBossbarAction implements Action{

    private String title;
    private BarColor barColor;
    private BarStyle barStyle;
    private double progress;

    public ShowBossbarAction() {
        this.title = "&eHello World!";
        this.barColor = BarColor.WHITE;
        this.barStyle = BarStyle.SOLID;
        this.progress = 1.0;
    }

    public ShowBossbarAction(String title, BarColor barColor, BarStyle barStyle, double progress) {
        this.title = title;
        this.barColor = barColor;
        this.barStyle = barStyle;
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "ShowBossbarAction (Title: " + title + ", BarColor: " + barColor + ", BarStyle: " + barStyle + ")";
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(colorize("&eShow Bossbar Action"));
        itemMeta.setLore(Arrays.asList(
                colorize("&7Display a bossbar to a player."),
                "",
                colorize("&eSettings:"),
                colorize("&fTitle: " + getTitle()),
                colorize("&fColor: " + getBarColor().name()),
                colorize("&fStyle: " + getBarStyle().name()),
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
        BossBar bossBar = Bukkit.createBossBar(colorize(title), barColor, barStyle);
        bossBar.setProgress(progress);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        return true;
    }

    public String getTitle() {
        return title;
    }
    public BarColor getBarColor() {
        return barColor;
    }
    public BarStyle getBarStyle() {
        return barStyle;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBarColor(BarColor barColor) {
        this.barColor = barColor;
    }
    public void setBarStyle(BarStyle barStyle) {
        this.barStyle = barStyle;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.attribute.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.fromColor;

public class ShowBossbarAction extends HTSLImpl {

    private String title;
    private BarColor barColor;
    private BarStyle barStyle;
    private double progress;

    public ShowBossbarAction() {
        super("Show Bossbar Action");
        this.title = "&eHello World!";
        this.barColor = BarColor.WHITE;
        this.barStyle = BarStyle.SOLID;
        this.progress = 1.0;
    }

    public ShowBossbarAction(String title, BarColor barColor, BarStyle barStyle, double progress) {
        super("Show Bossbar Action");
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
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.WITHER_SKELETON_SKULL);
        builder.name("&eShow Bossbar Action");
        builder.info("&eSettings", "");
        builder.info("Title", title);
        builder.info("Color", barColor.name());
        builder.info("Style", barStyle.name());
        builder.info("Progress", progress);

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.WITHER_SKELETON_SKULL);
        builder.name("&aShow Bossbar Action");
        builder.description("Display a bossbar to a player.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("title",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&eTitle")
                                .info("&7Current Value", "")
                                .info(null, "&a" + title)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.STRING
                ),
                new ActionEditor.ActionItem("barColor",
                        ItemBuilder.create(fromColor(barColor))
                                .name("&eBar Color")
                                .info("&7Current Value", "")
                                .info(null, "&a" + barColor.name())
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, BarColor.values(), null
                ),
                new ActionEditor.ActionItem("barStyle",
                        ItemBuilder.create(Material.FEATHER)
                                .name("&eBar Style")
                                .info("&7Current Value", "")
                                .info(null, "&a" + barStyle.name())
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, BarStyle.values(), Material.FEATHER
                ),
                new ActionEditor.ActionItem("progress",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&eProgress")
                                .info("&7Current Value", "")
                                .info(null, "&a" + progress)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE, 0, 1
                )
        );

        return new ActionEditor(4, "&eSend Title Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        BossBar bossBar = Bukkit.createBossBar(colorize(title), barColor, barStyle);
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;
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

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("title", title);
        data.put("barColor", barColor);
        data.put("barStyle", barStyle);
        data.put("progress", progress);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "bossbar";
    }
}

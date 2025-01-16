package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.attribute.*;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.fromColor;

public class ShowBossbarAction extends HTSLImpl {

    private String title;
    private BossBar.Color barColor;
    private BossBar.Overlay barStyle;
    private double progress;

    public ShowBossbarAction() {
        super("Show Bossbar Action");
        this.title = "&eHello World!";
        this.barColor = BossBar.Color.WHITE;
        this.barStyle = BossBar.Overlay.PROGRESS;
        this.progress = 1.0;
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
                        ActionEditor.ActionItem.ActionType.ENUM, BossBar.Color.values(), null
                ),
                new ActionEditor.ActionItem("barStyle",
                        ItemBuilder.create(Material.FEATHER)
                                .name("&eBar Style")
                                .info("&7Current Value", "")
                                .info(null, "&a" + barStyle.name())
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, BossBar.Overlay.values(), Material.FEATHER
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

        return new ActionEditor(4, "&eShow Bossbar Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        BossBar bossBar = BossBar.bossBar(StringUtilsKt.housingStringFormatter(title, house, player), NumberUtilsKt.toFloat(progress), barColor, barStyle);
        bossBar.addViewer(player);
        if (!house.bossBars.containsKey(player.getUniqueId())) {
            house.bossBars.put(player.getUniqueId(), new ArrayList<>());
            house.bossBars.get(player.getUniqueId()).add(bossBar);
        } else {
            house.bossBars.get(player.getUniqueId()).add(bossBar);
        }
        return true;
    }


    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("title", title);
        data.put("barColor", barColor.name());
        data.put("barStyle", barStyle.name());
        data.put("progress", progress);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        if (!data.containsKey("title")) return;
        title = (String) data.get("title");
        try {
            barColor = BossBar.Color.valueOf((String) data.getOrDefault("barColor", "WHITE"));
            barStyle = BossBar.Overlay.valueOf((String) data.getOrDefault("barStyle", "PROGRESS"));
        } catch (Exception e) {
           barColor = BossBar.Color.WHITE;
           barStyle = BossBar.Overlay.PROGRESS;
        }
        progress = (double) data.get("progress");
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

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.attribute.*;

import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.Color.fromColor;

@ToString
@Getter
@Setter
public class ShowBossbarAction extends HTSLImpl {
    private String title = "&eHello World!";
    private BossBar.Color barColor = BossBar.Color.WHITE;
    private BossBar.Overlay barStyle = BossBar.Overlay.PROGRESS;
    private String progress = "1.0";

    public ShowBossbarAction() {
        super(
                "show_bossbar_action",
                "Show Bossbar",
                "Shows a bossbar to the player.",
                Material.WITHER_SKELETON_SKULL,
                List.of("bossbar")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "title",
                        "Title",
                        "The title of the bossbar.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "barColor",
                        "Bar Color",
                        "The color of the bossbar.",
                        ActionProperty.PropertyType.ENUM, BossBar.Color.class
                ),
                new ActionProperty(
                        "barStyle",
                        "Bar Style",
                        "The style of the bossbar.",
                        ActionProperty.PropertyType.ENUM, BossBar.Overlay.class
                ),
                new ActionProperty(
                        "progress",
                        "Progress",
                        "The progress of the bossbar.",
                        ActionProperty.PropertyType.NUMBER, 0.0, 1.0
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        try {
            float progressFixed = Float.parseFloat(Placeholder.handlePlaceholders(progress, house, player));
            if (progressFixed > 1) {
                progressFixed = 1;
            } else if (progressFixed < 0) {
                progressFixed = 0;
            }
            BossBar bossBar = BossBar.bossBar(StringUtilsKt.housingStringFormatter(title, house, player), progressFixed, barColor, barStyle);
            bossBar.addViewer(player);
            if (!house.bossBars.containsKey(player.getUniqueId())) {
                house.bossBars.put(player.getUniqueId(), new ArrayList<>());
                house.bossBars.get(player.getUniqueId()).add(bossBar);
            } else {
                house.bossBars.get(player.getUniqueId()).add(bossBar);
            }
        } catch (Exception e) {
            return OutputType.ERROR;
        }
        return OutputType.SUCCESS;
    }

    @Override
    public List<EventType> disallowedEvents() {
        return Arrays.asList(EventType.PLAYER_QUIT);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

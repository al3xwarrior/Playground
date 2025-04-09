package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import com.al3x.housing2.Utils.StringUtilsKt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

@ToString
@Getter
@Setter
public class SendTitleAction extends HTSLImpl {

    private String title = "Title";
    private String subtitle = "Subtitle";
    private double fadeIn = 20;
    private double stay = 20;
    private double fadeOut = 20;

    public SendTitleAction() {
        super("send_title_action",
                "Send Title",
                "Sends a title and subtitle to the player.",
                Material.BOOK,
                List.of("title")
        );

        getProperties().addAll(List.of(
                new ActionProperty(
                        "title",
                        "Title",
                        "The title to send.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "subtitle",
                        "Subtitle",
                        "The subtitle to send.",
                        ActionProperty.PropertyType.STRING
                ),
                new ActionProperty(
                        "fadeIn",
                        "Fade In Time",
                        "The time it takes for the title to fade in.",
                        ActionProperty.PropertyType.INT, 0.0, 100.0
                ),
                new ActionProperty(
                        "stay",
                        "Stay Time",
                        "The time the title stays on screen.",
                        ActionProperty.PropertyType.INT, 0.0, 100.0
                ),
                new ActionProperty(
                        "fadeOut",
                        "Fade Out Time",
                        "The time it takes for the title to fade out.",
                        ActionProperty.PropertyType.INT, 0.0, 100.0
                )
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Component title = StringUtilsKt.housingStringFormatter(this.title, house, player);
        Component subtitle = StringUtilsKt.housingStringFormatter(this.subtitle, house, player);
        player.showTitle(Title.title(title, subtitle, Title.Times.times(Duration.ofMillis((long) fadeIn * 50), Duration.ofMillis((long) stay * 50), Duration.ofMillis((long) fadeOut * 50))));
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

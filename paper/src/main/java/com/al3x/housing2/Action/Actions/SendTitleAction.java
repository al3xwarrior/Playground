package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.IntegerProperty;
import com.al3x.housing2.Action.Properties.StringProperty;
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
    public SendTitleAction() {
        super("send_title_action",
                "Send Title",
                "Sends a title and subtitle to the player.",
                Material.BOOK,
                List.of("title")
        );

        getProperties().addAll(List.of(
                new StringProperty(
                        "title",
                        "Title",
                        "The title to send."
                ).setValue("Title"),
                new StringProperty(
                        "subtitle",
                        "Subtitle",
                        "The subtitle to send."
                ).setValue("Subtitle"),
                new IntegerProperty(
                        "fadeIn",
                        "Fade In Time",
                        "The time it takes for the title to fade in.",
                        0, 100
                ).setValue(20),
                new IntegerProperty(
                        "stay",
                        "Stay Time",
                        "The time the title stays on screen.",
                        0, 100
                ).setValue(20),
                new IntegerProperty(
                        "fadeOut",
                        "Fade Out Time",
                        "The time it takes for the title to fade out.",
                        0, 100
                ).setValue(20)
        ));
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Component title = getValue("title", StringProperty.class).component(house, player);
        Component subtitle = getValue("subtitle", StringProperty.class).component(house, player);
        player.showTitle(Title.title(title, subtitle, Title.Times.times(
                Duration.ofMillis(getValue("fadeIn", IntegerProperty.class).getValue() * 50),
                Duration.ofMillis(getValue("stay", IntegerProperty.class).getValue() * 50),
                Duration.ofMillis(getValue("fadeOut", IntegerProperty.class).getValue() * 50))));
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class SendTitleAction extends Action {

    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public SendTitleAction() {
        super("Send Title Action");
        this.title = "Title";
        this.subtitle = "Subtitle";
        this.fadeIn = 20;
        this.stay = 20;
        this.fadeOut = 20;
    }

    public SendTitleAction(String title, String subtitle) {
        super("Send Title Action");
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = 20;
        this.stay = 20;
        this.fadeOut = 20;
    }

    public SendTitleAction(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super("Send Title Action");
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
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.BOOK);
        builder.name("&eSend Title Action");
        builder.info("&eSettings", "");
        builder.info("Title", title);
        builder.info("Subtitle", subtitle);
        builder.info("Fade In Time", "&6" + fadeIn + " ticks");
        builder.info("Stay Time", "&6" + stay + " ticks");
        builder.info("Fade Out Time", "&6" + fadeOut + " ticks");

        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.BOOK);
        builder.name("&aSend Title Action");
        builder.description("Displays a Title and Subtitle to the player with a defined Fade In, Stay, and Fade Out time.");
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
                new ActionEditor.ActionItem("subtitle",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&eSubtitle")
                                .info("&7Current Value", "")
                                .info(null, "&a" + subtitle)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.STRING
                ),
                new ActionEditor.ActionItem("fadeIn",
                        ItemBuilder.create(Material.CLOCK)
                                .name("&eFade In Time")
                                .info("&7Current Value", "")
                                .info(null, "&a" + fadeIn)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT
                ),
                new ActionEditor.ActionItem("stay",
                        ItemBuilder.create(Material.CLOCK)
                                .name("&eStay Time")
                                .info("&7Current Value", "")
                                .info(null, "&a" + stay)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT
                ),
                new ActionEditor.ActionItem("fadeOut",
                        ItemBuilder.create(Material.CLOCK)
                                .name("&eFade Out Time")
                                .info("&7Current Value", "")
                                .info(null, "&a" + fadeOut)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.INT
                )
        );

        return new ActionEditor(4, "&eSend Title Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.sendTitle(colorize(title), colorize(subtitle), fadeIn, stay, fadeOut);
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

    @Override
    public HashMap<String, Object> data() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("subtitle", subtitle);
        data.put("fadeIn", fadeIn);
        data.put("stay", stay);
        data.put("fadeOut", fadeOut);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

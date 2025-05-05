package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.HypixelLoreFormatter;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.ItemBuilder.ActionType;
import com.al3x.housing2.Utils.NameTag;
import com.al3x.housing2.Utils.StringUtilsKt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.LinkedHashMap;
import java.util.List;

public class NameTagAction extends HTSLImpl {

    private String nametag;

    public NameTagAction() {
        super("NameTag Action");
        this.nametag = "%player.name%";
    }

    @Override
    public String keyword() {
        return "nametag";
    }

    @Override
    public String toString() {
        return "NameTag (Message: " + nametag + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.NAME_TAG);
        builder.name("&eChange Name Tag");
        builder.info("&eSettings", "").info("Name Tag", nametag);
        builder.lClick(ActionType.EDIT_YELLOW).rClick(ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.NAME_TAG);
        builder.name("&aChange Name Tag");
        builder.lClick(ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = List.of(
                new ActionItem("nametag",
                        ItemBuilder.create(Material.WRITTEN_BOOK)
                                .name("&aName Tag")
                                .info("&7Current Value", "")
                                .info(null, nametag)
                                .lClick(ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.STRING
                )
        );
        return new ActionEditor(4, "&eNametag Action Settings", items);
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(player.getName() + "_nametag");
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName() + "_nametag");
        }
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        new NameTag(player, StringUtilsKt.housingStringFormatter(nametag, house, player));

        team.addPlayer(player);
        return OutputType.SUCCESS;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("nametag", nametag);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.StringProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.NameTag;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.LinkedHashMap;
import java.util.List;

//Todo: Ability to set multiple lines, and if its visible in third person or not (aka visible to the player or not)
public class NameTagAction extends HTSLImpl {
    public NameTagAction() {
        super(
                ActionEnum.CHANGE_PLAYER_NAMETAG,
                "Change Player NameTag",
                "Changes the player's name tag.",
                Material.NAME_TAG,
                List.of("nametag")
        );
        getProperties().add(
                new StringProperty(
                        "nametag",
                        "Name Tag",
                        "The name tag to set."
                ).setValue("%player.name%")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(player.getName() + "_nametag");
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName() + "_nametag");
        }
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        new NameTag(player, getProperty("nametag", StringProperty.class).component(house, player));

        team.addPlayer(player);
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

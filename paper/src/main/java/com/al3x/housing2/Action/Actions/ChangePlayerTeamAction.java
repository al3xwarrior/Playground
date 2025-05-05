package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.Properties.GenericPagination.TeamProperty;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Team;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@ToString
public class ChangePlayerTeamAction extends HTSLImpl {
    public ChangePlayerTeamAction() {
        super(
                ActionEnum.CHANGE_PLAYER_TEAM,
                "Change Player Team",
                "Changes the player's team.",
                Material.PLAYER_HEAD,
                List.of("team")
        );

        getProperties().add(
                new TeamProperty(
                        "team",
                        "Team",
                        "The team to change the player to."
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        Team team = getValue("team", Team.class);
        if (team == null) {
            return OutputType.ERROR;
        }
        house.loadOrCreatePlayerData(player).setTeam(team.getName());
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Team;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@ToString
public class ChangePlayerTeamAction extends HTSLImpl {
    String team = null;
    public ChangePlayerTeamAction() {
        super(
                "change_player_team_action",
                "Change Player Team",
                "Changes the player's team.",
                Material.PLAYER_HEAD,
                List.of("team")
        );

        getProperties().add(
                new ActionProperty(
                        "team",
                        "Team",
                        "The team to change the player to.",
                        ActionProperty.PropertyType.TEAM
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (team == null) {
            return OutputType.ERROR;
        }
        Team team = house.getTeam(this.team);
        house.loadOrCreatePlayerData(player).setTeam(team.getName());
        return OutputType.SUCCESS;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        team = (String) data.get(getId());
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}

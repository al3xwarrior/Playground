package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.Team;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Menus.SlotSelectMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChangePlayerTeamAction extends HTSLImpl {
    String team = null;
    public ChangePlayerTeamAction() {
        super("Change Player Team");
    }

    @Override
    public String toString() {
        return "ChangePlayerTeam{" +
                "team=" + team +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eChange Player Team");
        builder.description("Change the player's team.");
        builder.info("&eSettings", "");
        builder.info("Team", (team == null ? "&aNot Set" : "&6" + team));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eChange Player Team");
        builder.description("Change the player's team.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("team", ItemBuilder.create(Material.PLAYER_HEAD)
                        .name("&aTeam")
                        .description("The team to change the player to.")
                        .info("&7Current Value", "")
                        .info(null, (team == null ? "&aNot Set" : "&6" + team)),
                        ActionEditor.ActionItem.ActionType.TEAM
                )
        );
        return new ActionEditor(4, "&eChange Player Team", items);
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
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("team", team);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        team = (String) data.get("team");
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "changePlayerTeam";
    }
}

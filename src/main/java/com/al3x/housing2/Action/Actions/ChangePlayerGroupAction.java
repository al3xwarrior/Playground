package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Instances.Group;
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

public class ChangePlayerGroupAction extends HTSLImpl {
    String group = null;
    boolean demotionProtection = false;
    public ChangePlayerGroupAction() {
        super("Change Player Group");
    }

    @Override
    public String toString() {
        return "ChangePlayerGroup{" +
                "group=" + group +
                ", demotionProtection=" + demotionProtection +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eChange Player Group");
        builder.info("&eSettings", "");
        builder.info("Group", (group == null ? "&aNot Set" : "&6" + group));
        builder.info("Demotion Protection", (demotionProtection ? "&aEnabled" : "&cDisabled"));
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.PLAYER_HEAD);
        builder.name("&eChange Player Group");
        builder.description("Change the player's group.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        List<ActionEditor.ActionItem> items =  List.of(
                new ActionEditor.ActionItem("group", ItemBuilder.create(Material.PLAYER_HEAD)
                        .name("&aGroup")
                        .description("The group to change the player to.")
                        .info("&7Current Value", "")
                        .info(null, (group == null ? "&aNot Set" : "&6" + group)),
                        ActionEditor.ActionItem.ActionType.GROUP
                ),
                new ActionEditor.ActionItem("demotionProtection", ItemBuilder.create((demotionProtection ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aDemotion Protection")
                        .info("&7Current Value", "")
                        .info(null, demotionProtection ? "&aEnabled" : "&cDisabled"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&eChange Player Group", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (group == null) {
            return true;
        }
        if (house.getOwnerUUID() == player.getUniqueId()) {
            return false;
        }
        Group group = house.getGroup(this.group);
        if (house.loadOrCreatePlayerData(player).getGroupInstance(house).getPriority() > group.getPriority() && demotionProtection) {
            return true;
        }
        house.loadOrCreatePlayerData(player).setGroup(group.getName());
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("group", group);
        data.put("demotionProtection", demotionProtection);
        return data;
    }

    @Override
    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        group = (String) data.get("group");
        demotionProtection = (boolean) data.get("demotionProtection");
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "changePlayerGroup";
    }
}

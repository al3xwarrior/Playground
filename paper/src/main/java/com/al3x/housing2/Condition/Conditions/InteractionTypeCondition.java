package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Gamemodes;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class InteractionTypeCondition extends CHTSLImpl {
    private boolean attack;

    public InteractionTypeCondition() {
        super("Interaction Type");
        this.attack = true;
    }

    @Override
    public String toString() {
        return "InteractionTypeCondition";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.OAK_BUTTON);
        builder.name("&aInteraction Type");
        builder.description("If the player must attack or interact with an npc.");
        builder.info("Attack", "" + attack);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.OAK_BUTTON);
        builder.name("&eInteraction Type");
        builder.description("If the player must attack or interact with an npc.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("attack",
                        ItemBuilder.create(Material.DAYLIGHT_DETECTOR)
                                .name("&e" + (!attack ? "Set Attack" : "Set Interact"))
                                .info("&7Current Value", "")
                                .info(null, "&a" + (attack ? "Attack" : "Interact"))
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "Gamemode Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house, CancellableEvent event) {
        if (event.cancellable() instanceof EntityDamageByEntityEvent) {
            return attack;
        } else if (event.cancellable() instanceof PlayerInteractEntityEvent) {
            return !attack;
        } else {
            return false;
        }
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("attack", attack);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "interactionType";
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.PLAYER_DAMAGE, EventType.PLAYER_ATTACK);
    }
}

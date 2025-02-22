package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Action.ActionEditor.ActionItem;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ScaledMaxHealthTrait;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ChangeMaxHealthAction extends HTSLImpl implements NPCAction {

    private double health;
    private StatOperation mode;
    private boolean healOnChange;

    public ChangeMaxHealthAction() {
        super("Change Max Health Action");
        this.health = 20.0;
        this.mode = StatOperation.INCREASE;
        this.healOnChange = true;
    }

    public ChangeMaxHealthAction(double health, StatOperation operation, boolean healOnChange) {
        super("Change Max Health Action");
        this.health = health;
        this.mode = operation;
        this.healOnChange = healOnChange;
    }

    @Override
    public String toString() {
        return "ChangeMaxHealthAction (Health: " + this.health + " Mode: " + this.mode + " HealOnChange: " + this.healOnChange + ")";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.DANDELION);
        builder.name("&eChange Max Health");
        builder.info("&eSettings", "");
        builder.info("Health", "&a" + health);
        builder.info("Operation", "&a" + mode.toString());
        builder.info("Heal On Change", (healOnChange) ? "&aYes" : "&cNo");
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.DANDELION);
        builder.name("&aChange Max Health Action");
        builder.description("Set the user's maximum health.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionItem> items = Arrays.asList(
                new ActionItem("health",
                        ItemBuilder.create(Material.BOOK)
                                .name("&aHealth")
                                .info("&7Current Value", "")
                                .info(null, health)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionItem.ActionType.DOUBLE
                ),
                new ActionItem("mode",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eMode")
                                .info("&7Current Value", "")
                                .info(null, "&a" + mode)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, StatOperation.values(), null
                ),
                new ActionItem("healOnChange",
                        ItemBuilder.create((this.healOnChange ? Material.LIME_DYE : Material.RED_DYE))
                        .name("&aHeal On Change")
                        .description("Should the player be fully healed when this action is ran")
                        .info("&7Current Value", "")
                        .info(null, healOnChange ? "&aYes" : "&cNo"),
                        ActionEditor.ActionItem.ActionType.BOOLEAN
                )
        );
        return new ActionEditor(4, "&eMax Health Action Settings", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        if (player != null) {
            double currentMax = player.getMaxHealth();

            switch (mode) {
                case INCREASE:
                    currentMax += health;
                    break;
                case DECREASE:
                    currentMax -= health;
                    break;
                case SET:
                    currentMax = health;
                    break;
            }

            // Ensure CurrentMax is a value between 1 and 2000
            currentMax = Math.min(2000, Math.max(1, currentMax));
            player.setMaxHealth(currentMax);
            if (healOnChange) {
                player.setHealth(currentMax);
            }
        }
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("health", health);
        data.put("mode", mode);
        data.put("healOnChange", healOnChange);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "maxHealth";
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        if (npc != null) {
            double currentMax = npc.getOrAddTrait(ScaledMaxHealthTrait.class).getMaxHealth();

            switch (mode) {
                case INCREASE:
                    currentMax += health;
                    break;
                case DECREASE:
                    currentMax -= health;
                    break;
                case SET:
                    currentMax = health;
                    break;
            }

            // Ensure CurrentMax is a value between 1 and 2000
            currentMax = Math.min(2000, Math.max(1, currentMax));
            npc.getOrAddTrait(ScaledMaxHealthTrait.class).setMaxHealth(currentMax);
            if (healOnChange && npc.getEntity() instanceof LivingEntity le) {
                le.setHealth(currentMax);
            }
        }
    }

//    @Override
//    public void fromData(HashMap<String, Object> data) {
//        if (!data.containsKey("message")) return;
//        message = (String) data.get("message");
//    }
}

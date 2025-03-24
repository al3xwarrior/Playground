package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class DamageAmountCondition extends CHTSLImpl implements NPCCondition {
    private StatComparator comparator;
    private Double compareValue;

    public DamageAmountCondition() {
        super("Damage Amount");
        this.comparator = StatComparator.GREATER_THAN_OR_EQUAL;
        this.compareValue = 1.0;
    }

    @Override
    public String toString() {
        return "DamageAmount{" +
                "comparator=" + comparator +
                ", compareValue=" + compareValue +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.WOODEN_SWORD);
        builder.name("&eDamage Amount Requirement");
        builder.description("Requires the users damage amount to match the provided condition.");
        builder.info("Comparator", comparator.name());
        builder.info("Value", compareValue);
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.WOODEN_SWORD);
        builder.name("&eDamage Amount Requirement");
        builder.description("Requires the users damage amount to match the provided condition.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("comparator",
                        ItemBuilder.create(Material.COMPASS)
                                .name("&eMode")
                                .info("&7Current Value", "")
                                .info(null, "&a" + comparator)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, StatComparator.values(), null
                ),
                new ActionEditor.ActionItem("compareValue",
                        ItemBuilder.create(Material.BOOK)
                                .name("&eAmount")
                                .info("&7Current Value", "")
                                .info(null, "&a" + compareValue)
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.DOUBLE
                )
        );
        return new ActionEditor(4, "Damage Amount Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house, CancellableEvent event) {
        if (event.cancellable() instanceof EntityDamageEvent e) {
            return Comparator.compare(comparator, e.getDamage(), compareValue);
        }
        return false;
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        return false;
    }

    @Override
    public List<EventType> allowedEvents() {
        return Arrays.asList(EventType.PLAYER_DAMAGE, EventType.PLAYER_ATTACK, EventType.NPC_DAMAGE);
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("comparator", comparator.name());
        data.put("compareValue", compareValue);
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "damageAmount";
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (event.cancellable() instanceof EntityDamageByEntityEvent e) {
            return Comparator.compare(comparator, e.getDamage(), compareValue);
        }
        return false;
    }
}

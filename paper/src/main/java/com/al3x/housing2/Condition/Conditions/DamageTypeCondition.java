package com.al3x.housing2.Condition.Conditions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Condition.CHTSLImpl;
import com.al3x.housing2.Condition.NPCCondition;
import com.al3x.housing2.Enums.DamageTypes;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Instances.Comparator;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class DamageTypeCondition extends CHTSLImpl implements NPCCondition {
    private DamageTypes damageType;

    public DamageTypeCondition() {
        super("Damage Type");
        this.damageType = DamageTypes.PLAYER_ATTACK;
    }

    @Override
    public String toString() {
        return "DamageType{" +
                "damageType=" + damageType +
                '}';
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(damageType.getMaterial());
        builder.name("&eDamage Type Requirement");
        builder.description("Requires the users damage type to match the provided condition.");
        builder.info("Damage Type", damageType.getTranslation());
        builder.lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_SWORD);
        builder.name("&eDamage Type Requirement");
        builder.description("Requires the users damage type to match the provided condition.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public ActionEditor editorMenu(HousingWorld house) {
        List<ActionEditor.ActionItem> items = Arrays.asList(
                new ActionEditor.ActionItem("damageType",
                        ItemBuilder.create(Material.WOODEN_SWORD)
                                .name("&eDamage Type")
                                .info("&7Current Value", "")
                                .info(null, damageType.getTranslation())
                                .lClick(ItemBuilder.ActionType.CHANGE_YELLOW),
                        ActionEditor.ActionItem.ActionType.ENUM, DamageTypes.values(), null
                )
        );
        return new ActionEditor(4, "Damage Type Requirement", items);
    }

    @Override
    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        if (event instanceof EntityDamageEvent e) {
            return e.getDamageSource().getDamageType() == damageType.getDamageType();
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
        data.put("damageType", damageType.name());
        return data;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "damageType";
    }

    @Override
    public boolean npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        if (event instanceof EntityDamageEvent e) {
            return e.getDamageSource().getDamageType() == damageType.getDamageType();
        }
        return false;
    }
}

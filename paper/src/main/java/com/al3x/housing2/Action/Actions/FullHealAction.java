package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.al3x.housing2.Utils.Color.colorize;

public class FullHealAction extends HTSLImpl implements NPCAction {

    public FullHealAction() {
        super("Full Heal Action");
    }

    @Override
    public String toString() {
        return "FullHealAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.GOLDEN_APPLE);
        builder.name("&eFull Heal");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.GOLDEN_APPLE);
        builder.name("&aFull Heal");
        builder.description("Fully heals the player.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.setHealth(player.getMaxHealth());
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        if (!(npc.getEntity() instanceof LivingEntity le)) return;
        le.setHealth(le.getMaxHealth());
    }

    @Override
    public LinkedHashMap<String, Object> data() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public String keyword() {
        return "fullHeal";
    }
}

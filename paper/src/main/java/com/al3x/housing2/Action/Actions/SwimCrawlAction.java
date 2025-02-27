package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEditor;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.NPCAction;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;

public class SwimCrawlAction extends HTSLImpl implements NPCAction {

    public SwimCrawlAction() {
        super("Swim/Crawl Action");
    }

    @Override
    public String toString() {
        return "SwimCrawlAction";
    }

    @Override
    public void createDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_BARS);
        builder.name("&eSwim/Crawl Action");
        builder.rClick(ItemBuilder.ActionType.REMOVE_YELLOW);
        builder.shiftClick();
    }

    @Override
    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(Material.IRON_BARS);
        builder.name("&aSwim/Crawl Action");
        builder.description("Toggles if the player/npc swim or crawl depending on if they are in water or not.");
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    @Override
    public boolean execute(Player player, HousingWorld house) {
        player.setSwimming(!player.isSwimming());
        return true;
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
        return "swimCrawl";
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, Cancellable event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) npc.getEntity();
            entity.setSwimming(!entity.isSwimming());
        }
    }
}

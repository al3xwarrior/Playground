package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.*;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.ToString;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.LinkedHashMap;
import java.util.List;

@ToString
public class SwimCrawlAction extends HTSLImpl implements NPCAction {
    public SwimCrawlAction() {
        super(
                ActionEnum.SWIM_CRAWL,
                "Swim/Crawl Action",
                "Toggles if the player/npc swim or crawl depending on if they are in water or not.",
                Material.IRON_BARS,
                List.of("swimCrawl")
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        player.setSwimming(!player.isSwimming());
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public void npcExecute(Player player, NPC npc, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        if (npc.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) npc.getEntity();
            entity.setSwimming(!entity.isSwimming());
        }
    }
}

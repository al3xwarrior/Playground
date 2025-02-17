package com.al3x.housing2.Listeners;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class ActionButtonListener implements Listener {
    @EventHandler
    public void onBreakButton(BlockBreakEvent event) {
        Player player = event.getPlayer();
        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        if (!event.getBlock().getType().name().contains("BUTTON")) return;

    }

    @EventHandler
    public void onInteractButton(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        HousingWorld house = Main.getInstance().getHousesManager().getHouse(player.getWorld());
        if (house == null) return;
        if (event.getClickedBlock() == null) return;

        if (!event.getClickedBlock().getType().name().contains("BUTTON")) return;

        if (!event.getAction().isRightClick()) {
            if (house.removeActionButton(event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
                player.sendMessage("§aSuccessfully removed action button!");
                event.getClickedBlock().setType(Material.AIR);
            }
            return;
        }


        List<Action> actions = house.getActionButton(event.getClickedBlock().getLocation());
        if (actions == null) return;

        if (player.isSneaking()) {
            ActionsMenu menu = new ActionsMenu(Main.getInstance(), player, house, actions, null, "button");
            menu.setUpdate(() -> {
                house.setActionButton(event.getClickedBlock().getLocation(), menu.getActions());
            });
            menu.open();
            return;
        }

        System.out.println(actions.stream().map(Action::getName).toList());
        ActionExecutor executor = new ActionExecutor("button", actions);
        executor.execute(player, house, event);
    }
}

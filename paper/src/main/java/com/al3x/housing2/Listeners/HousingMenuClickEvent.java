package com.al3x.housing2.Listeners;

import com.al3x.housing2.Data.PlayerData;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.HousingMenu.HousingMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.EditPlayerMenu;
import com.al3x.housing2.Menus.HousingMenu.PlayerListing.PlayerListingMenu;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import static com.al3x.housing2.Utils.Color.colorize;

public class HousingMenuClickEvent implements Listener {

    private Main main;
    private HousesManager housesManager;

    public HousingMenuClickEvent(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        // Ensure the player is holding the correct item and has permission to open the housing menu.
        if (item == null ||
            !item.getType().equals(Material.NETHER_STAR) ||
            !item.hasItemMeta() ||
            !item.getItemMeta().hasDisplayName() ||
            !item.getItemMeta().getDisplayName().equals("§dHousing Menu §7(Right-Click)") ||
            !housesManager.hasPermissionInHouse(player, Permissions.HOUSING_MENU)) {
            return;
        }

        HousingWorld house = housesManager.getHouse(player.getWorld());

        // Use ray tracing to check if a player is being looked at.
        RayTraceResult result = player.getWorld().rayTraceEntities(
            player.getEyeLocation(),
            player.getLocation().getDirection(),
            5,
            entity -> entity instanceof Player && !entity.equals(player)
        );

        if (house.hasPlayerListing(player) && result != null && result.getHitEntity() != null) {
            // A player is being targeted.
            Player target = (Player) result.getHitEntity();

            PlayerData playerData = house.getPlayersData().get(player.getUniqueId().toString());
            PlayerData targetPlayerData = house.getPlayersData().get(target.getUniqueId().toString());

            // Determine if the target has a higher priority than the player.
            boolean higherPriority = playerData.getGroupInstance(house).getPriority() <= targetPlayerData.getGroupInstance(house).getPriority() &&
                                     !player.getUniqueId().equals(target.getUniqueId());

            if (higherPriority) {
                player.sendMessage(colorize("&cYou can't edit this player, they have a higher priority than you!"));
                return;
            }

            new EditPlayerMenu(main, player, house, target).open();

        } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // No player in sight; handle block/air interaction.
            new HousingMenu(main, player, house).open();
        }
    }
}

package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.al3x.housing2.Utils.Color.colorize;
import static com.al3x.housing2.Utils.HandlePlaceholders.parsePlaceholders;

public class JoinLeaveHouse implements Listener {

    private Main main;
    private HousesManager housesManager;

    public JoinLeaveHouse(Main main, HousesManager housesManager) {
        this.main = main;
        this.housesManager = housesManager;
    }

    // Actions that modify the player's "profile" need to be reset.
    private void resetPlayer(Player player) {
        player.setMaximumAir(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setNoDamageTicks(10);
    }

    private void updateScoreboard(Player player, HousingWorld house) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("houseboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(colorize("&e&lHOUSING V2"));

        List<String> scoreboard = new ArrayList<>(house.getScoreboard());
        Collections.reverse(scoreboard);
        for (int i = 0; i < scoreboard.size(); i++) {
            String line = scoreboard.get(i) + "&" + (i % 10);
            objective.getScore(colorize(parsePlaceholders(player, house, line))).setScore(i);
        }
        player.setScoreboard(board);
    }

    private void joinHouse(Player player) {
        HousingWorld house = housesManager.getHouse(UUID.fromString(player.getWorld().getName()));
        if (house == null) { return; }

        house.incGuests();
        player.teleport(house.getSpawn());

        resetPlayer(player);

        // If the person joining is the owner
        if (house.getOwnerUUID().equals(player.getUniqueId())) {
            player.setGameMode(GameMode.CREATIVE);
            ItemStack menu = new ItemStack(Material.NETHER_STAR);
            ItemMeta menuMeta = menu.getItemMeta();
            menuMeta.setDisplayName(colorize("&dHousing Menu &7(Right-Click"));
            menu.setItemMeta(menuMeta);
            player.getInventory().setItem(8, menu);
        }
        // Normal player joins
        else {
            player.setGameMode(GameMode.SURVIVAL);
            ItemStack menu = new ItemStack(Material.DARK_OAK_DOOR);
            ItemMeta menuMeta = menu.getItemMeta();
            menuMeta.setDisplayName(colorize("&aHousing Menu &7(Right-Click"));
            menu.setItemMeta(menuMeta);
            player.getInventory().setItem(8, menu);
        }

        BukkitTask[] task = new BukkitTask[1];
        task[0] = Bukkit.getScheduler().runTaskTimer(main, () -> {
            if (!player.getWorld().getName().equals(house.getHouseUUID().toString())) {
                task[0].cancel();
                return;
            }
            updateScoreboard(player, house);
        }, 0, 5);
    }

    private void leaveHouse(Player player, HousingWorld from) {
        if (from == null) return;
        from.decGuests();
        from.broadcast(colorize(player.getDisplayName() + " &eleft the world."));
    }

    @EventHandler
    public void enterWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();

        // They are not leaving the hub so that means they are leaving another house
        if (!e.getFrom().getName().equals("world")) {
            leaveHouse(player, housesManager.getHouse(UUID.fromString(e.getFrom().getName())));
        }

        // They are entering a house, not the hub
        if (!player.getWorld().getName().equals("world")) {
            joinHouse(player);
        } else {
            resetPlayer(player);

            // Scoreboard
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective("houseboard", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(colorize("&e&lHOUSING V2"));
            objective.getScore(colorize("&fTemp Hub Scoreboard")).setScore(1);
            player.setScoreboard(board);
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {

        Player player = e.getPlayer();
        String worldName = player.getWorld().getName();

        HousingWorld house = null;
        try {
            UUID worldUUID = UUID.fromString(worldName);
            house = housesManager.getHouse(worldUUID);
        } catch (IllegalArgumentException ignored) {}

        // If what they are leaving is indeed a house, then we trigger the leave house method
        if (house != null) {
            leaveHouse(player, house);
        }

        e.setQuitMessage(colorize( "&7&o" + player.getName() + " left the server."));
    }

}

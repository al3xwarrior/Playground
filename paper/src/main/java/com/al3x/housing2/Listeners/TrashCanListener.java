package com.al3x.housing2.Listeners;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class TrashCanListener implements Listener {

    private Main main;
    private static ItemStack head = ItemBuilder.create(Material.PLAYER_HEAD)
            .skullTexture("441f4edbc68c9061355242bd73effc9299a3252b9f11e82b5f1ec7b3b6ac0")
            .build();
    private static ItemStack easterEggHead = ItemBuilder.create(Material.PLAYER_HEAD)
            .skullTexture("3cea77e0532b330f6d554eb7c0f4274bf0365c35eab2ed7f39997ad1370942bb")
            .build();

    public TrashCanListener(Main main) {
        this.main = main;
    }

    private static void createArmorStands(Block block) {
        ArmorStand top = block.getWorld().spawn(block.getLocation().add(0.5, -0.5, 0.5), ArmorStand.class);
        top.setGravity(false); top.setVisible(false); top.setSmall(true);
        ArmorStand bottom = block.getWorld().spawn(block.getLocation().add(0.5, -0.7, 0.5), ArmorStand.class);
        bottom.setGravity(false); bottom.setVisible(false); bottom.setSmall(true);
        top.getEquipment().setHelmet((Math.random() < 0.01) ? easterEggHead : head);
        bottom.getEquipment().setHelmet(head);
    }

    public static void initTrashCans(List<Location> locations) {
        for (Location location : locations) {
            Block block = location.getBlock();
            block.setType(Material.BARRIER);
            createArmorStands(block);
        }
    }

    @EventHandler
    public void clickTrashCan(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        if (house.trashCanAtLocation(block.getLocation()) && action.equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BARRIER)) {
            player.openInventory(Bukkit.createInventory(player, 27, colorize("&cTrash Can")));
        }
    }

    @EventHandler
    public void placeTrashCan(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        ItemStack item = e.getItemInHand();
        if (item == null) return;

        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        boolean ownerOfHouse = house.getOwnerUUID().equals(player.getUniqueId());

        String name = item.getItemMeta().getDisplayName();
        Material itemType = item.getType();

        if (name.equals("§aTrash Can") && itemType.equals(Material.CAULDRON) && ownerOfHouse) {
            Block blockPlaced = e.getBlockPlaced();
            blockPlaced.setType(Material.BARRIER);
            house.addTrashCan(blockPlaced.getLocation());

            // Armor Stand
            createArmorStands(blockPlaced);
        }
    }


    @EventHandler
    public void breakTrashCan(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        HousingWorld house = main.getHousesManager().getHouse(player.getWorld());
        if (house == null) return;

        if (house.trashCanAtLocation(block.getLocation())) {
            house.removeTrashCan(block.getLocation());

            int stands = 0;
            for (ArmorStand armorStand : block.getWorld().getEntitiesByClass(ArmorStand.class)) {
                if (stands >= 2) return;
                if (armorStand.getLocation().add(0, 1, 0).getBlock().equals(block)) {
                    stands++;
                    armorStand.remove();
                }
            }
        }
    }

}

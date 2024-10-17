package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Action.Actions.PushPlayerAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class DirectionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PushPlayerAction action;
    private EventType event;

    public DirectionMenu(Main main, Player player, HousingWorld house, PushPlayerAction action, EventType event) {
        super(player, colorize("&7Select Option"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
    }

    @Override
    public void setupItems() {
        ItemStack forwardItem = new ItemStack(Material.LIGHT_BLUE_DYE);
        ItemMeta forwardItemMeta = forwardItem.getItemMeta();
        forwardItemMeta.setDisplayName(colorize("&aForward"));
        forwardItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.FORWARD)) {
            forwardItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        forwardItem.setItemMeta(forwardItemMeta);
        addItem(10, forwardItem, () -> {
            action.setDirection(PushDirection.FORWARD);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack backwardItem = new ItemStack(Material.RED_DYE);
        ItemMeta backwardItemMeta = backwardItem.getItemMeta();
        backwardItemMeta.setDisplayName(colorize("&cBackward"));
        backwardItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.BACKWARD)) {
            backwardItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        backwardItem.setItemMeta(backwardItemMeta);
        addItem(11, backwardItem, () -> {
            action.setDirection(PushDirection.BACKWARD);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack leftItem = new ItemStack(Material.GREEN_DYE);
        ItemMeta leftItemMeta = leftItem.getItemMeta();
        leftItemMeta.setDisplayName(colorize("&aLeft"));
        leftItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.LEFT)) {
            leftItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        leftItem.setItemMeta(leftItemMeta);
        addItem(12, leftItem, () -> {
            action.setDirection(PushDirection.LEFT);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack rightItem = new ItemStack(Material.ORANGE_DYE);
        ItemMeta rightItemMeta = rightItem.getItemMeta();
        rightItemMeta.setDisplayName(colorize("&6Right"));
        rightItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.RIGHT)) {
            rightItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        rightItem.setItemMeta(rightItemMeta);
        addItem(13, rightItem, () -> {
            action.setDirection(PushDirection.RIGHT);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack upItem = new ItemStack(Material.YELLOW_DYE);
        ItemMeta upItemMeta = upItem.getItemMeta();
        upItemMeta.setDisplayName(colorize("&eUp"));
        upItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.UP)) {
            upItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        upItem.setItemMeta(upItemMeta);
        addItem(14, upItem, () -> {
            action.setDirection(PushDirection.UP);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack downItem = new ItemStack(Material.GRAY_DYE);
        ItemMeta downItemMeta = downItem.getItemMeta();
        downItemMeta.setDisplayName(colorize("&7Down"));
        downItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.DOWN)) {
            downItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        downItem.setItemMeta(downItemMeta);
        addItem(15, downItem, () -> {
            action.setDirection(PushDirection.DOWN);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack northItem = new ItemStack(Material.WHITE_DYE);
        ItemMeta northItemMeta = northItem.getItemMeta();
        northItemMeta.setDisplayName(colorize("&fNorth"));
        northItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.NORTH)) {
            northItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        northItem.setItemMeta(northItemMeta);
        addItem(16, northItem, () -> {
            action.setDirection(PushDirection.NORTH);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack southItem = new ItemStack(Material.PINK_DYE);
        ItemMeta southItemMeta = southItem.getItemMeta();
        southItemMeta.setDisplayName(colorize("&dSouth"));
        southItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.SOUTH)) {
            southItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        southItem.setItemMeta(southItemMeta);
        addItem(19, southItem, () -> {
            action.setDirection(PushDirection.SOUTH);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack eastItem = new ItemStack(Material.CYAN_DYE);
        ItemMeta eastItemMeta = eastItem.getItemMeta();
        eastItemMeta.setDisplayName(colorize("&3East"));
        eastItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.EAST)) {
            eastItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        eastItem.setItemMeta(eastItemMeta);
        addItem(20, eastItem, () -> {
            action.setDirection(PushDirection.EAST);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });

        ItemStack westItem = new ItemStack(Material.PURPLE_DYE);
        ItemMeta westItemMeta = westItem.getItemMeta();
        westItemMeta.setDisplayName(colorize("&5West"));
        westItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getDirection().equals(PushDirection.WEST)) {
            westItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        westItem.setItemMeta(westItemMeta);
        addItem(21, westItem, () -> {
            action.setDirection(PushDirection.WEST);
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });


        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new PushPlayerActionMenu(main, player, house, action, event).open();
        });
    }
}

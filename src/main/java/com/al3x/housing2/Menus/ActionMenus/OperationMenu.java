package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Actions.PlayerStatAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class OperationMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private PlayerStatAction action;
    private EventType event;

    public OperationMenu(Main main, Player player, HousingWorld house, PlayerStatAction action, EventType event) {
        super(player, colorize("&7Select Option"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
    }

    @Override
    public void setupItems() {
        ItemStack increaseItem = new ItemStack(Material.GREEN_STAINED_GLASS);
        ItemMeta increaseItemMeta = increaseItem.getItemMeta();
        increaseItemMeta.setDisplayName(colorize("&aIncrement"));
        increaseItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.INCREASE)) {
            increaseItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        increaseItem.setItemMeta(increaseItemMeta);
        addItem(10, increaseItem, () -> {
            action.setMode(StatOperation.INCREASE);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack decreaseItem = new ItemStack(Material.RED_STAINED_GLASS);
        ItemMeta decreaseItemMeta = decreaseItem.getItemMeta();
        decreaseItemMeta.setDisplayName(colorize("&aDecrement"));
        decreaseItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.DECREASE)) {
            decreaseItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        decreaseItem.setItemMeta(decreaseItemMeta);
        addItem(11, decreaseItem, () -> {
            action.setMode(StatOperation.DECREASE);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack setItem = new ItemStack(Material.YELLOW_STAINED_GLASS);
        ItemMeta setItemMeta = setItem.getItemMeta();
        setItemMeta.setDisplayName(colorize("&aSet"));
        setItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.SET)) {
            setItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        setItem.setItemMeta(setItemMeta);
        addItem(12, setItem, () -> {
            action.setMode(StatOperation.SET);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack multItem = new ItemStack(Material.ORANGE_STAINED_GLASS);
        ItemMeta multItemMeta = multItem.getItemMeta();
        multItemMeta.setDisplayName(colorize("&aMultiply"));
        multItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.MULTIPLY)) {
            multItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        multItem.setItemMeta(multItemMeta);
        addItem(13, multItem, () -> {
            action.setMode(StatOperation.MULTIPLY);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack divItem = new ItemStack(Material.BLUE_STAINED_GLASS);
        ItemMeta divItemMeta = divItem.getItemMeta();
        divItemMeta.setDisplayName(colorize("&aDivide"));
        divItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.DIVIDE)) {
            divItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        divItem.setItemMeta(divItemMeta);
        addItem(14, divItem, () -> {
            action.setMode(StatOperation.DIVIDE);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack modItem = new ItemStack(Material.MAGENTA_STAINED_GLASS);
        ItemMeta modItemMeta = modItem.getItemMeta();
        modItemMeta.setDisplayName(colorize("&aMod"));
        modItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.MOD)) {
            modItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        modItem.setItemMeta(modItemMeta);
        addItem(15, modItem, () -> {
            action.setMode(StatOperation.MOD);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack floorItem = new ItemStack(Material.WHITE_STAINED_GLASS);
        ItemMeta floorItemMeta = floorItem.getItemMeta();
        floorItemMeta.setDisplayName(colorize("&aFloor"));
        floorItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.FLOOR)) {
            floorItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        floorItem.setItemMeta(floorItemMeta);
        addItem(16, floorItem, () -> {
            action.setMode(StatOperation.FLOOR);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack roundItem = new ItemStack(Material.BROWN_STAINED_GLASS);
        ItemMeta roundItemMeta = roundItem.getItemMeta();
        roundItemMeta.setDisplayName(colorize("&aRound"));
        roundItemMeta.setLore(Arrays.asList(
                colorize("&eClick to select!")
        ));
        if (action.getMode().equals(StatOperation.ROUND)) {
            roundItemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }
        roundItem.setItemMeta(roundItemMeta);
        addItem(19, roundItem, () -> {
            action.setMode(StatOperation.ROUND);
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(40, backArrow, () -> {
            new PlayerStatActionMenu(main, player, house, action, event).open();
        });
    }
}

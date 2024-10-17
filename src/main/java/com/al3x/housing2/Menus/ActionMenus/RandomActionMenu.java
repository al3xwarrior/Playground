package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Action.Actions.RandomAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static com.al3x.housing2.Utils.Color.colorize;

public class RandomActionMenu extends Menu {

    private Main main;
    private Player player;
    private HousingWorld house;
    private RandomAction action;
    private EventType event;

    public RandomActionMenu(Main main, Player player, HousingWorld house, RandomAction action, EventType event) {
        super(player, colorize("&eRandom Action Settings"), 45);
        this.main = main;
        this.player = player;
        this.house = house;
        this.action = action;
        this.event = event;
        setupItems();
    }

    @Override
    public void setupItems() {
        ItemStack messageItem = new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        ItemMeta messageItemMeta = messageItem.getItemMeta();
        messageItemMeta.setDisplayName(colorize("&aActions"));
        messageItemMeta.setLore(Arrays.asList(
                colorize(""),
                colorize("&7Current Value: " +
                        (action.getSubActions().isEmpty() ? "&cNo Actions" : "&a" + action.getSubActions().size() + " Action")
                )
        ));
        messageItem.setItemMeta(messageItemMeta);

        addItem(10, messageItem, () -> {
            // Close the menu and prompt the player to enter a new message
            new ActionsMenu(main, player, house, event, action.getSubActions(), this).open();
        });

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backArrowMeta = backArrow.getItemMeta();
        backArrowMeta.setDisplayName(colorize("&cGo Back"));
        backArrow.setItemMeta(backArrowMeta);
        addItem(31, backArrow, () -> {
            if (event != null) {
                new ActionsMenu(main, player, house, event).open();
                return;
            }
        });
    }
}

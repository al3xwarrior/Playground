package com.al3x.housing2.Menus;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.al3x.housing2.Utils.Color.colorize;

public class ActionsMenu extends Menu {

    private Player player;
    private HousingWorld house;
    private List<Action> actions;
    private EventType event;

    private int currentPage = 0;
    private final int itemsPerPage = 45; // Leave room for navigation buttons

    public ActionsMenu(Player player, HousingWorld house, String title, EventType event) {
        super(player, colorize(title), 54);
        this.player = player;
        this.house = house;
        this.event = event;
        this.actions = house.getEventActions(event);
        setupItems();
    }

    private void removeAction(Action action) {
        actions.remove(action);
        if (event != null) {
            house.setEventActions(event, actions);
        }
        setupItems();
    }

    @Override
    public void setupItems() {
        clearItems()

        int start = currentPage * itemsPerPage;
        int end = Math.min((actions != null) ? actions.size() : 0, start + itemsPerPage);

        // Display the actions for the current page
        for (int i = start; i < end; i++) {
            Action action = actions.get(i);
            addItem(i - start, action.getDisplayItem(), () -> {
                player.sendMessage("Clicked action");
            }, () -> {
                removeAction(event, action);
            });
        }

        // Add navigation buttons
        if (currentPage > 0) {
            ItemStack previous = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = previous.getItemMeta();
            prevMeta.setDisplayName(colorize("&aPrevious Page"));
            previous.setItemMeta(prevMeta);
            addItem(48, previous, () -> {
                currentPage--;
                setupItems(); // Refresh the menu with the new page
            });
        }

        if (end < ((actions != null) ? actions.size() : 0)) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(colorize("&aNext Page"));
            next.setItemMeta(nextMeta);
            addItem(50, next, () -> {
                currentPage++;
                setupItems(); // Refresh the menu with the new page
            });
        }

        ItemStack addAction = new ItemStack(Material.PAPER);
        ItemMeta addActionMeta = addAction.getItemMeta();
        addActionMeta.setDisplayName(colorize("&aAdd Action"));
        addAction.setItemMeta(addActionMeta);
        addItem(49, addAction, () -> {
            player.sendMessage("adding action");
        });
    }

}

package com.al3x.housing2.Menus.ItemEditor;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Item;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionsMenu;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;

public class EditActionTypesMenu extends Menu {
    Item item;
    public EditActionTypesMenu(Player player, Item item) {
        super(player, "&7Edit Action Types", 9*4);
        this.item = item;
    }

    @Override
    public void initItems() {
        Main main = Main.getInstance();
        HousingWorld housingWorld = main.getHousesManager().getHouse(player.getWorld());
        addItem(11, ItemBuilder.create(Material.IRON_SWORD)
                .name("&aLeft Click Actions")
                .description("&7Click to edit the actions that happen when you left click this item.")
                .info("&eActions", "")
                .info(null, item.getActions().get(ClickType.LEFT).size() + " actions")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            openActionsMenu(ClickType.LEFT);
        });
        addItem(12, ItemBuilder.create(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .name("&aRight Click Actions")
                .description("&7Click to edit the actions that happen when you right click this item.")
                .info("&eActions", "")
                .info(null, item.getActions().get(ClickType.RIGHT).size() + " actions")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            openActionsMenu(ClickType.RIGHT);
        });
        addItem(13, ItemBuilder.create(Material.DROPPER)
                .name("&aDrop Actions")
                .description("&7Click to edit the actions that happen when you drop this item.")
                .info("&eActions", "")
                .info(null, item.getActions().get(ClickType.DROP).size() + " actions")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            openActionsMenu(ClickType.DROP);
        });
        addItem(14, ItemBuilder.create(Material.DROPPER)
                .glow(true)
                .punctuation(false)
                .name("&aDrop Stack Actions")
                .description("&7Click to edit the actions that happen when you control drop/drop the stack of items." + (item.getBase().getMaxStackSize() == 1 ? "\n\n&cWILL NOT WORK WITH THIS ITEM." : ""))
                .info("&eActions", "")
                .info(null, item.getActions().get(ClickType.CONTROL_DROP).size() + " actions")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            if (item.getBase().getMaxStackSize() == 1) {
                return;
            }
            openActionsMenu(ClickType.CONTROL_DROP);
        });
        addItem(15, ItemBuilder.create(Material.SHIELD)
                .name("&aSwap Offhand Actions")
                .description("&7Click to edit the actions that happen when you swap this item to your offhand.")
                .info("&eActions", "")
                .info(null, item.getActions().get(ClickType.SWAP_OFFHAND).size() + " actions")
                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .build(), () -> {
            openActionsMenu(ClickType.SWAP_OFFHAND);
        });

        //back
        addItem(31, ItemBuilder.create(Material.ARROW)
                .name("&cBack")
                .build(), () -> {
            new EditItemMainMenu(player).open();
        });
    }

    private void openActionsMenu(ClickType clickType) {
        Main main = Main.getInstance();
        HousingWorld housingWorld = main.getHousesManager().getHouse(player.getWorld());
        ArrayList<Action> actions = item.getActions().get(clickType);
        ActionsMenu menu = new ActionsMenu(main, player, housingWorld, actions, this, item.getBase().displayName().toString());
        if (clickType == ClickType.SWAP_OFFHAND) {
            menu.setEvent(EventType.PLAYER_SWAP_TO_OFFHAND);
        } else if (clickType == ClickType.DROP) {
            menu.setEvent(EventType.PLAYER_DROP_ITEM);
        } else {
            menu.setEvent(EventType.PLAYER_SWAP_TO_OFFHAND);
        }
        menu.setUpdate(() -> {
            item.getActions().put(clickType, actions);
            player.getInventory().setItemInMainHand(item.build());
            setupItems();
        });
        menu.open();
    }
}

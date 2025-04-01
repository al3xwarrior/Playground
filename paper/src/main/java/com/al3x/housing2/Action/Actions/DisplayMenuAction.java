package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.CustomMenuViewer;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@ToString
public class DisplayMenuAction extends HTSLImpl {

    String menu;

    public DisplayMenuAction() {
        super(
                "display_menu_action",
                "Display Menu",
                "Displays a custom menu to the player.",
                Material.CHEST,
                List.of("displayMenu")
        );

        getProperties().add(
                new ActionProperty(
                        "menu",
                        "Menu",
                        "The menu to display.",
                        ActionProperty.PropertyType.MENU
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (menu == null) {
            return OutputType.ERROR;
        }
        house.getCustomMenus().stream().filter(customMenu -> customMenu.getTitle().equals(menu)).findFirst().ifPresent(customMenu -> {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> { //Make sure it runs on the main thread
                new CustomMenuViewer(player, customMenu).open();
            });
        });
        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

}

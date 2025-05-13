package com.al3x.housing2.Action.Actions;

import com.al3x.housing2.Action.ActionEnum;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.HTSLImpl;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Action.Properties.GenericPagination.MenuProperty;
import com.al3x.housing2.Instances.CustomMenu;
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
    public DisplayMenuAction() {
        super(
                ActionEnum.DISPLAY_MENU,
                "Display Menu",
                "Displays a custom menu to the player.",
                Material.CHEST,
                List.of("displayMenu")
        );

        getProperties().add(
                new MenuProperty(
                        "menu",
                        "Menu",
                        "The menu to display."
                )
        );
    }

    @Override
    public OutputType execute(Player player, HousingWorld house) {
        if (getValue("menu", CustomMenu.class) != null) return OutputType.ERROR;

        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            // this might cause an error if it doesn't exist?
            new CustomMenuViewer(player, getValue("menu", CustomMenu.class)).open();
        });

        return OutputType.SUCCESS;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

}

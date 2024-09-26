package com.al3x.housing2.Menus.ActionMenus;

import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;

public abstract class ActionSettingsMenu extends Menu {

    public ActionSettingsMenu(Player player, String title, int size) {
        super(player, title, size);
    }
}

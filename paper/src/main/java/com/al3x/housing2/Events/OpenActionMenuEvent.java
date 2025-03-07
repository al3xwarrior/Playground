package com.al3x.housing2.Events;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class OpenActionMenuEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ActionEditMenu menu;
    private final Action action;
    private final Main main;
    private final Player player;
    private final HousingWorld house;
    private final Menu backMenu;
    private boolean cancelled;

    public OpenActionMenuEvent(ActionEditMenu menu, Action action, Main main, Player player, HousingWorld house, Menu backMenu) {
        this.menu = menu;
        this.action = action;
        this.main = main;
        this.player = player;
        this.house = house;
        this.backMenu = backMenu;
    }

    public ActionEditMenu getMenu() {
        return menu;
    }

    public Action getAction() {
        return action;
    }

    public Main getMain() {
        return main;
    }

    public Player getPlayer() {
        return player;
    }

    public HousingWorld getHouse() {
        return house;
    }

    public Menu getBackMenu() {
        return backMenu;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

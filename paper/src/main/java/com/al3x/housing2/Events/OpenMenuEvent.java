package com.al3x.housing2.Events;

import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OpenMenuEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Menu menu;
    private final Player player;
    private final Main main;

    private boolean cancelled;

    public OpenMenuEvent(Menu menu, Player player, Main main) {
        this.menu = menu;
        this.player = player;
        this.main = main;
    }

    public Menu getMenu() {
        return menu;
    }

    public Player getPlayer() {
        return player;
    }

    public Main getMain() {
        return main;
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

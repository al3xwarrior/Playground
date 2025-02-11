package com.al3x.housing2.Utils;

import org.bukkit.event.Cancellable;

public class AbstractCancellable implements Cancellable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}

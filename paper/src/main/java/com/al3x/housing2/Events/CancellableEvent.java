package com.al3x.housing2.Events;

import de.maxhenkel.voicechat.api.events.Event;
import org.bukkit.event.Cancellable;

/**
 * @param event       Voicechat cancellable event
 * @param cancellable Bukkit cancellable event
 */
public record CancellableEvent(Event event, Cancellable cancellable) {

    public void setCancelled(boolean cancelled) {
        if (cancellable != null) {
            cancellable.setCancelled(cancelled);
        } else if (event != null) {
            if (event.isCancelled()) {
                event.cancel();
            }
        }
    }

    public boolean isCancelled() {
        if (cancellable != null) {
            return cancellable.isCancelled();
        } else if (event != null) {
            return event.isCancelled();
        }
        return false;
    }
}

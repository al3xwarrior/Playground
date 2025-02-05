package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.ExitAction;
import com.al3x.housing2.Action.Actions.PauseAction;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ActionExecutor {
    private List<Action> queue = new ArrayList<>();
    double pause = 0;
    ParentActionExecutor parent;
    boolean isPaused = false;

    public ActionExecutor(@Nullable ParentActionExecutor parent) {
        this.parent = parent;

        if (parent != null) this.parent.addExecutor(this);
    }

    public ActionExecutor(List<Action> action) {
        queue.addAll(action);
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void addActions(List<Action> actions) {
        queue.addAll(actions);
    }

    public ParentActionExecutor getParent() {
        return parent;
    }

    public void setParent(ParentActionExecutor parent) {
        this.parent = parent;
    }

    public List<Action> getQueue() {
        return queue;
    }

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        if (house.isAdminMode(player)) return false;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        AtomicInteger waited = new AtomicInteger();

        if (!otherExecute(player, house, event)) return false;

        if (queue.isEmpty()) return true;

        scheduler.runTaskTimer(Main.getInstance(), (t) -> {
            if (waited.get() < pause) {
                waited.getAndIncrement();
                return;
            }
            waited.set(0);

            if (!otherExecute(player, house, event)) {
                t.cancel();
            }

            if (queue.isEmpty()) t.cancel();
        }, 0, 1);

        return true;
    }

    public boolean otherExecute(Player player, HousingWorld house, Cancellable event) {
        boolean actionReturn = true;
        while (!queue.isEmpty() && !isPaused) {
            Action action = queue.removeFirst();

            if (action instanceof PauseAction pauseAction) { //Add time every time a new one comes in
                String dur = Placeholder.handlePlaceholders(pauseAction.getDuration(), house, player);
                if (!NumberUtilsKt.isDouble(dur)) {
                    return true;
                }
                double duration = Double.parseDouble(dur);
                pause += duration;
                return true;
            }

            if (action instanceof ExitAction || !player.getWorld().equals(house.getWorld())) {
                return false;
            }

            actionReturn = action.execute(player, house, event, this);
        }

        if (parent != null && !parent.running && !parent.executors.isEmpty()) {
            parent.nano = System.nanoTime();
            parent.execute(player, house, event);
        }

        return actionReturn;
    }
}

package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.ExitAction;
import com.al3x.housing2.Action.Actions.PauseAction;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.AsyncTask;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionExecutor {
    private List<Action> queue = new ArrayList<>();
    double pause = 0;
    ParentActionExecutor parent;
    boolean isPaused = false;

    long currentTime = System.currentTimeMillis();

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
        AtomicBoolean returnVal = new AtomicBoolean(true);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        AtomicBoolean canMoveOn = new AtomicBoolean(true);
        house.runInThread(new AsyncTask((task) -> {
            if (!canMoveOn.get() && currentTime + pause * 50 < System.currentTimeMillis()) {
                canMoveOn.set(true);
                pause = 0;
            }

            if (!canMoveOn.get() || isPaused) {
                return;
            }

            if (queue.isEmpty()) {
                task.cancel();
                return;
            }

            Action action = queue.removeFirst();

            if (action instanceof PauseAction pauseAction) { //Add time every time a new one comes in
                String dur = Placeholder.handlePlaceholders(pauseAction.getDuration(), house, player);
                if (!NumberUtilsKt.isDouble(dur)) {
                    return;
                }
                double duration = Double.parseDouble(dur);
                pause += duration;
                currentTime = System.currentTimeMillis();
                canMoveOn.set(false);
                return;
            }

            if (action instanceof ExitAction) {
                returnVal.set(false);
                task.cancel();
                return;
            }

            if (action.mustBeSync()) {
                scheduler.runTask(Main.getInstance(), () -> {
                    returnVal.set(action.execute(player, house, event, this));
                });
            } else {
                returnVal.set(action.execute(player, house, event, this));
            }

            if (parent != null && !parent.running && !parent.executors.isEmpty()) {
                parent.nano = System.nanoTime();
                parent.execute(player, house, event);
            }
        }));

        if (parent != null && !parent.running && !parent.executors.isEmpty()) {
            parent.execute(player, house, event);
        }

        return returnVal.get();
    }
}

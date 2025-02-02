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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionExecutor {
    private List<Action> queue = new ArrayList<>();
    double pause = 0;
    boolean isPaused = false;

    public ActionExecutor() {
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

    public List<Action> getQueue() {
        return queue;
    }

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        AtomicBoolean returnVal = new AtomicBoolean(true);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        Action action;
        while (!queue.isEmpty()) {
            if (isPaused) {
                break;
            }

            action = queue.removeFirst();

            if (action instanceof PauseAction pauseAction) { //Add time every time a new one comes in
                String dur = Placeholder.handlePlaceholders(pauseAction.getDuration(), house, player);
                if (!NumberUtilsKt.isDouble(dur)) {
                    return false;
                }
                double duration = Double.parseDouble(dur);
                pause += duration;
                continue;
            }

            if (action instanceof ExitAction) {
                returnVal.set(false);
                return false;
            }

            if (!returnVal.get()) {
                return false;
            }

            if (pause == 0) {
                returnVal.set(action.execute(player, house, event, this));
                continue;
            }

            if (action.requiresPlayer() && player == null) {
                return true;
            }

            Action finalAction = action;
            if (String.valueOf(pause).equals(String.valueOf(Math.round(pause)))) {
                scheduler.runTaskLater(Main.getInstance(), () -> {
                    returnVal.set(finalAction.execute(player, house, event, this));
                }, (long) pause);
            } else {
                double finalPause = pause;
                house.runInThread(new AsyncTask((millis, task) -> {
                    if (millis % (50 * finalPause) != 0) {
                        return;
                    }

                    if (finalAction.mustBeSync()) {
                        scheduler.runTask(Main.getInstance(), () -> {
                            returnVal.set(finalAction.execute(player, house, event, this));
                        });
                    } else {
                        returnVal.set(finalAction.execute(player, house, event, this));
                    }
                }));
            }
        }

        if (isPaused) { //Start a timer to check if it's unpaused every tick when it gets unpaused, then it will execute any remaining actions
            house.runInThread(new AsyncTask((millis, task) -> {
                if (!isPaused) {
                    scheduler.runTask(Main.getInstance(), () -> {
                        execute(player, house, event);
                    });
                    task.cancel();
                }
            }));
        }

        return returnVal.get();
    }
}

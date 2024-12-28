package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.ExitAction;
import com.al3x.housing2.Action.Actions.PauseAction;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionExecutor {
    private List<Action> queue = new ArrayList<>();
    long pause = 0;

    public ActionExecutor() {
    }

    public ActionExecutor(List<Action> action) {
        queue.addAll(action);
    }

    public void addActions(List<Action> actions) {
        queue.addAll(actions);
    }

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        AtomicBoolean returnVal = new AtomicBoolean(true);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        for (Action action : queue) {
            if (action instanceof PauseAction) { //Add time every time a new one comes in
                pause += (long) ((PauseAction) action).getDuration();
                continue;
            }

            if (action instanceof ExitAction) {
                returnVal.set(false);
                break;
            }

            if (!returnVal.get()) {
                break;
            }

            if (pause == 0) {
                returnVal.set(action.execute(player, house, event));
                continue;
            }

            if (String.valueOf(pause).equals(String.valueOf(Math.round(pause)))) {
                scheduler.runTaskLater(Main.getInstance(), () -> {
                    returnVal.set(action.execute(player, house, event));
                }, pause);
            } else {
                long finalPause = pause;
                scheduler.runTaskAsynchronously(Main.getInstance(), () -> {
                    try {
                        Thread.sleep(finalPause * 50);
                    } catch (InterruptedException ignored) {
                    }

                    if (action.mustBeSync()) {
                        scheduler.runTask(Main.getInstance(), () -> {
                            returnVal.set(action.execute(player, house, event));
                        });
                    } else {
                        returnVal.set(action.execute(player, house, event));
                    }
                });
            }
        }
        queue.clear();
        return returnVal.get();
    }
}

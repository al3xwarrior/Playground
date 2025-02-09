package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.BreakAction;
import com.al3x.housing2.Action.Actions.ContinueAction;
import com.al3x.housing2.Action.Actions.ExitAction;
import com.al3x.housing2.Action.Actions.PauseAction;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import net.minecraft.util.parsing.packrat.Atom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.al3x.housing2.Action.OutputType.*;

public class ActionExecutor {
    private String context;
    private List<Action> queue = new ArrayList<>();
    private ActionExecutor parent;
    private List<ActionExecutor> children = new ArrayList<>();
    double pause = 0;
    boolean isPaused = false;
    boolean isComplete = false;

    int count = 0;

    int workingIndex = 0;

    public ActionExecutor(String context) {
        this.context = context;
    }

    public ActionExecutor(String context, List<Action> action) {
        queue.addAll(action);
        count = action.size();
        this.context = context;
    }

    public void addChild(ActionExecutor executor) {
        children.add(executor);
    }

    public List<ActionExecutor> getChildren() {
        return children;
    }

    public void setParent(ActionExecutor parent) {
        this.parent = parent;
    }

    public ActionExecutor getParent() {
        return parent;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public String getContext() {
        return context;
    }

    public void addActions(List<Action> actions) {
        queue.addAll(actions);
        count += actions.size();
    }

    public List<Action> getQueue() {
        return queue;
    }

    private boolean isAllComplete() {
        for (ActionExecutor executor : children) {
            if (!executor.isComplete()) {
                return false;
            }
        }
        return true;
    }

    int counter = 0;

    public ActionExecutor findHighestParentWithContext(String context) {
        if (parent == null) {
            return null;
        }

        if (parent.getContext().equals(context)) {
            return parent;
        }

        return parent.findHighestParentWithContext(context);
    }

    public OutputType execute(Player player, HousingWorld house, Cancellable event) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        Action action;
        while (!queue.isEmpty()) {
            if (isPaused || !isAllComplete()) {
                break;
            }

            action = queue.removeFirst();

            if (action instanceof PauseAction pauseAction) { //Add time every time a new one comes in
                String dur = Placeholder.handlePlaceholders(pauseAction.getDuration(), house, player);
                if (!NumberUtilsKt.isDouble(dur)) {
                    return ERROR;
                }
                double duration = Double.parseDouble(dur);
                pause += duration;
                continue;
            }

            if (action instanceof ExitAction) {
                exitAllTheWay(this);
                return EXIT;
            }

            if (action instanceof ContinueAction) {
                isComplete = true;
                ActionExecutor parent = findHighestParentWithContext("repeat");
                if (parent != null) {
                    parent.isComplete = true;
                }
                return CONTINUE;
            }

            if (action instanceof BreakAction) {
                isComplete = true;
                ActionExecutor parent = findHighestParentWithContext("repeat");
                if (parent != null && parent.parent != null) {
                    parent.isComplete = true;
                    parent.parent.children.forEach(executor -> executor.setComplete(true));
                }
                workingIndex = 0;
                return SUCCESS;
            }

            if (pause == 0) {
                action.execute(player, house, event, this);
                counter++;

                if (counter >= count - 1) {
                    isComplete = true;
                    counter = 0;
                }

                executeChildren(player, house, event); //Look for children to execute. >:)
                continue;
            }

            if (action.requiresPlayer() && player == null) {
                return ERROR;
            }

            Action finalAction = action;

            scheduler.runTaskLater(Main.getInstance(), () -> {
                if (isComplete || isPaused) return;
                finalAction.execute(player, house, event, this);
                counter++;

                if (counter >= count - 1) {
                    isComplete = true;
                    counter = 0;
                }

                executeChildren(player, house, event);
            }, (long) pause);
        }

        if (isPaused || !isAllComplete()) { //Start a timer to check if it's unpaused every tick when it gets unpaused, then it will execute any remaining actions
            scheduler.runTaskTimer(Main.getInstance(), (t) -> {
                if (!isPaused && isAllComplete()) {
                    execute(player, house, event);
                    t.cancel();
                }
            }, 0, 1);
        }

        return queue.isEmpty() ? SUCCESS : RUNNING;
    }

    private void executeChildren(Player player, HousingWorld house, Cancellable event) {
        if (isAllComplete() || children.isEmpty()) {
            return;
        }

        if (workingIndex >= children.size()) {
            workingIndex = 0;
        }
        AtomicReference<ActionExecutor> executor = new AtomicReference<>(children.get(workingIndex));
        if (executor.get().isComplete()) {
            workingIndex++;
            executeChildren(player, house, event);
            return;
        }
        AtomicReference<OutputType> output = new AtomicReference<>(executor.get().execute(player, house, event));
        workingIndex++;

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), (t) -> {
            if (executor.get().isComplete() && (output.get() != EXIT)) { //Move on to the next child
                executeChildren(player, house, event);
                t.cancel();
            }
        }, 0, 1);
    }

    private void exitAllTheWay(ActionExecutor parent) {
        if (parent == null) {
            return;
        }
        parent.isComplete = true;
        parent.children.forEach((executor) -> executor.setComplete(true));
        if (parent.parent != null) {
            exitAllTheWay(parent.parent);
        }
    }

    @Override
    public String toString() {
        return "ActionExecutor{" +
                "context='" + context + '\'' +
                ", queue=" + queue +
                ", pause=" + pause +
                ", isPaused=" + isPaused +
                ", isComplete=" + isComplete +
                ", count=" + count +
                ", workingIndex=" + workingIndex +
                '}';
    }
}

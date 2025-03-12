package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.BreakAction;
import com.al3x.housing2.Action.Actions.ContinueAction;
import com.al3x.housing2.Action.Actions.ExitAction;
import com.al3x.housing2.Action.Actions.PauseAction;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Instances.Stat;
import com.al3x.housing2.Main;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.NumberUtilsKt;
import de.maxhenkel.voicechat.api.events.Event;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.util.parsing.packrat.Atom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.al3x.housing2.Action.OutputType.*;

public class ActionExecutor {
    private HashMap<String, Integer> limits = new HashMap<>();
    private String context;
    private List<Action> queue = new ArrayList<>();

    private List<Stat> localStats = new ArrayList<>();

    double pause = 0;
    boolean isPaused = false;
    boolean isComplete = false;

    int workingIndex = 0;

    public ActionExecutor(String context) {
        this.context = context;
    }

    public ActionExecutor(String context, List<Action> action) {
        queue.addAll(action);
        this.context = context;
    }

    public void setLimits(HashMap<String, Integer> limits) {
        this.limits = limits;
    }

    public Stat getLocalStat(String name) {
        for (Stat stat : localStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }
        return null;
    }

    public boolean hasLocalStat(String name) {
        for (Stat stat : localStats) {
            if (stat.getStatName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void removeLocalStat(String name) {
        for (Stat stat : localStats) {
            if (stat.getStatName().equals(name)) {
                localStats.remove(stat);
                return;
            }
        }
    }

    public void addLocalStat(Stat stat) {
        localStats.add(stat);
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
    }

    public List<Action> getQueue() {
        return queue;
    }

    public OutputType execute(Player player, HousingWorld house, CancellableEvent event) {
        return execute(player, player, house, event);
    }

    public OutputType execute(NPC npc, Player player, HousingWorld house, CancellableEvent event) {
        return execute(npc.getEntity(), player, house, event);
    }

    public OutputType execute(Entity entity, Player player, HousingWorld house, CancellableEvent event) {
        if (limits == null) {
            limits = new HashMap<>();
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        while (!queue.isEmpty()) {
            if (isPaused) {
                break;
            }

            if (isComplete) {
                return SUCCESS;
            }

            Action action = queue.removeFirst();

            int global = Main.getInstance().getConfig().getInt("globalLimits", 2000);
            int actionLimit = Main.getInstance().getConfig().getInt("actionLimits." + action.name.replace(" ", "_").toLowerCase(), global);
            if (limits.containsKey(action.name) && limits.get(action.name) >= actionLimit) {
                return EXIT;
            }
            limits.put(action.name, limits.getOrDefault(action.name, 0) + 1);

            if (action instanceof PauseAction pauseAction) {
                String dur = Placeholder.handlePlaceholders(pauseAction.getDuration(), house, player);
                if (!NumberUtilsKt.isInt(dur)) {
                    return ERROR;
                }
                double duration = Integer.parseInt(dur);
                scheduler.runTaskLater(Main.getInstance(), () -> {
                    execute(entity, player, house, event);
                }, (long) duration);
                break;
            }

            if (action instanceof ExitAction) {
                return EXIT;
            }

            if (action instanceof ContinueAction) {
                return CONTINUE;
            }

            if (action instanceof BreakAction) {
                return BREAK;
            }

            try {
                if (player == entity) {
                    OutputType ot = action.execute(player, house, event, this);

                    if (ot == PAUSE) {
                        isPaused = true;
                        break;
                    } else if (ot == EXIT) {
                        return EXIT;
                    }
                } else if (entity != null && CitizensAPI.getNPCRegistry().isNPC(entity) && action instanceof NPCAction npcAction) {
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                    npcAction.npcExecute(player, npc, house, event, this);
                }
            } catch (Exception e) {
                player.sendMessage("An error occurred while executing the action: " + action.name);
                player.sendMessage(e.getMessage());
            }

        }

        if (isPaused) { //Start a timer to check if it's unpaused every tick when it gets unpaused, then it will execute any remaining actions
            scheduler.runTaskTimer(Main.getInstance(), (t) -> {
                if (!isPaused) {
                    execute(entity, player, house, event);
                    t.cancel();
                }
            }, 0, 1);
        }

        return (queue.isEmpty()) ? SUCCESS : RUNNING;
    }

    public OutputType execute(Entity entity, Player player, HousingWorld house, Cancellable event) {
        return execute(entity, player, house, new CancellableEvent(null, event));
    }

    public OutputType execute(Entity entity, Player player, HousingWorld house, Event event) {
        return execute(entity, player, house, new CancellableEvent(event, null));
    }

    @Override
    public String toString() {
        return "ActionExecutor{" +
                "context='" + context + '\'' +
                ", queue=" + queue +
                ", pause=" + pause +
                ", isPaused=" + isPaused +
                ", isComplete=" + isComplete +
                ", workingIndex=" + workingIndex +
                '}';
    }

    public HashMap<String, Integer> getLimits() {
        return limits;
    }
}

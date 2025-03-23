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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.minecraft.util.parsing.packrat.Atom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.al3x.housing2.Action.OutputType.*;

public class ActionExecutor {
    private HashMap<String, Integer> limits = new HashMap<>();
    private String context;
    private List<Action> queue = new ArrayList<>();
    double pause = 0;
    boolean isPaused = false;

    private Consumer<ActionExecutor> onComplete = null;
    private Consumer<ActionExecutor> onBreak = null;

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

    public void onComplete(Consumer<ActionExecutor> onComplete) {
        this.onComplete = onComplete;
    }

    public void onBreak(Consumer<ActionExecutor> onBreak) {
        this.onBreak = onBreak;
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

        if (pause > 0) {
            scheduler.runTaskLater(Main.getInstance(), () -> {
                execute(entity, player, house, event);
            }, (long) pause);
            pause = 0;
            return RUNNING;
        }
        while (!queue.isEmpty()) {
            if (isPaused) {
                break;
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
                    continue;
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

            if (action instanceof ContinueAction && context.equals("repeat")) {
                if (onComplete != null) {
                    onComplete.accept(this);
                }
                return CONTINUE;
            }

            if (action instanceof BreakAction && context.equals("repeat")) {
                if (onBreak != null) {
                    onBreak.accept(this);
                }
                return BREAK;
            }

            try {
                if (player == entity) {
                    if (player.getWorld() != house.getWorld()) {
                        return ERROR;
                    }
                    OutputType ot = action.execute(player, house, event, this);

                    if (ot == PAUSE) {
                        isPaused = true;
                        break;
                    } else if (ot == EXIT) {
                        return EXIT;
                    }  else if (ot == RUNNING) {
                        return RUNNING;
                    }
                } else if (entity != null && CitizensAPI.getNPCRegistry().isNPC(entity) && action instanceof NPCAction npcAction) {
                    if (entity.getWorld() != house.getWorld()) {
                        return ERROR;
                    }
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                    npcAction.npcExecute(player, npc, house, event, this);
                }
            } catch (Exception e) {
                player.sendMessage("An error occurred while executing the action: " + action.name + " in the context: " + context);
                //take error and put it in the hover event
                e.printStackTrace();
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

        if (queue.isEmpty()) {
            if (onComplete != null) {
                onComplete.accept(this);
            }
            return SUCCESS;
        }

        return RUNNING;
    }

    @Override
    public String toString() {
        return "ActionExecutor{" +
                "context='" + context + '\'' +
                ", queue=" + queue +
                ", pause=" + pause +
                ", isPaused=" + isPaused +
                '}';
    }

    public HashMap<String, Integer> getLimits() {
        return limits;
    }

    public Consumer<ActionExecutor> getOnBreak() {
        return onBreak;
    }
}

package com.al3x.housing2.Action;

import com.al3x.housing2.Instances.HousingWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class ParentActionExecutor {
    long nano = 0;
    boolean running = false;
    Stack<ActionExecutor> executors;

    public ParentActionExecutor() {
        executors = new Stack<>();
    }

    public void addExecutor(ActionExecutor executor) {
        executors.push(executor);
    }

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        if (nano == 0) {
            nano = System.nanoTime();
        }

        if (executors.isEmpty()) {
            return true;
        }

        running = true;
        while (!executors.isEmpty()) {
            if (System.nanoTime() - nano > 1000000) {
                System.out.println("Took too long");
                return true;
            }
            ActionExecutor executor = executors.pop();
            if (!executor.execute(player, house, event)) {
                return false;
            }
        }
        running = false;

        return true;
    }
}

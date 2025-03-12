package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Action.OutputType;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Function {
    private String name;
    private UUID id;//probably not needed?
    private Integer ticks; //Null is not active, 2-100?
    private Material material;
    private String description;
    private List<Action> actions;
    private boolean global = false;
    private boolean loaded = true;

    private int lastRun = 0;

    public Function(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.description = "";
        this.ticks = null;
        this.material = Material.MAP;
        this.actions = new ArrayList<>();
    }

    public Function(String name, UUID id, Integer ticks, Material material, String description, List<Action> actions, boolean global) {
        this.name = name;
        this.id = id;
        this.ticks = ticks;
        this.material = material;
        this.description = description;
        this.actions = actions;
        this.global = global;
    }

    public OutputType execute(Main main, Entity entity, Player player, HousingWorld house, boolean automatic, ActionExecutor oldExecutor) {
        if (!loaded) return OutputType.ERROR;
        List<Player> players = new ArrayList<>();
        //I dont fliping know anymore lol
        if (global && automatic) {
            players.add(null);
        } else {
            if (player != null) {
                players.add(player);
            } else {
                players = house.getWorld().getPlayers();
            }
        }

        for (Player p : players) {
            ActionExecutor executor = new ActionExecutor("function");
            executor.setLimits(oldExecutor != null ? oldExecutor.getLimits() : new HashMap<>());
            executor.addActions(new ArrayList<>(actions));
            executor.execute(p, p, house, (Cancellable) null);
        }

        return OutputType.SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTicks() {
        return ticks;
    }

    public void setTicks(Integer ticks) {
        this.ticks = ticks;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public UUID getId() {
        return id;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public int getLastRun() {
        return lastRun;
    }
}


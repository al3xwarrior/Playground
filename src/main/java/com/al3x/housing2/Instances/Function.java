package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionExecutor;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

    public void execute(Main main, Player player, HousingWorld house) {
        if (!loaded) return;
        List<Player> players = new ArrayList<>();
        //I dont fliping know anymore lol
        if (global) {
            players.add(null);
        } else {
            if (player != null) {
                players.add(player);
            } else {
                players = house.getWorld().getPlayers();
            }
        }
        ActionExecutor executor = new ActionExecutor();

        for (Player p : players) {
            List<Action> actions = new ArrayList<>(this.actions).stream()
                    .filter((action) -> action.allowedEvents() == null || action.allowedEvents().contains(EventType.FUNCTION))
                    .filter((action) -> !action.requiresPlayer() || p != null)
                    .toList();
            executor.addActions(actions);
            executor.execute(p, house, null);
        }
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


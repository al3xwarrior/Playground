package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Enums.PvpSettings;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Region extends Duple<Location, Location> {
    private boolean loaded;
    private String name;
    private List<Action> exitActions;
    private List<Action> enterActions;
    private HashMap<String, Boolean> pvpSettings;

    private List<UUID> playersInRegion = new ArrayList<>();


    public Region(Location posA, Location posB, String name) {
        super(posA, posB);
        this.loaded = true;
        this.name = name;
        this.exitActions = new ArrayList<>();
        this.enterActions = new ArrayList<>();
        this.pvpSettings = new HashMap<>();
    }

    public Region(boolean loaded, Location posA, Location posB, String name, List<Action> exitActions, List<Action> enterActions, HashMap<String, Boolean> pvpSettings) {
        super(posA, posB);
        this.loaded = loaded;
        this.name = name;
        this.exitActions = exitActions;
        this.enterActions = enterActions;
        this.pvpSettings = pvpSettings;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean b) {
        this.loaded = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getExitActions() {
        return exitActions;
    }

    public List<Action> getEnterActions() {
        return enterActions;
    }

    public HashMap<String, Boolean> getPvpSettings() {
        return pvpSettings;
    }

    public List<UUID> getPlayersInRegion() {
        return playersInRegion;
    }

    @Override
    public String toString() {
        return name;
    }
}



package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Utils.Duple;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Regions extends Duple<Location, Location> {
    private boolean loaded;
    private String name;
    private List<Action> exitActions;
    private List<Action> enterActions;
    private HashMap<PvpSettings, Boolean> pvpSettings;


    public Regions(Location posA, Location posB, String name) {
        super(posA, posB);
        this.loaded = true;
        this.name = name;
        this.exitActions = new ArrayList<>();
        this.enterActions = new ArrayList<>();
        this.pvpSettings = new HashMap<>();
    }

    public Regions(boolean loaded, Location posA, Location posB, String name, List<Action> exitActions, List<Action> enterActions, HashMap<PvpSettings, Boolean> pvpSettings) {
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

    public List<Action> getExitActions() {
        return exitActions;
    }

    public List<Action> getEnterActions() {
        return enterActions;
    }

    public HashMap<PvpSettings, Boolean> getPvpSettings() {
        return pvpSettings;
    }
}



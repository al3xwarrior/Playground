package com.al3x.housing2.Instances;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSpeedManager {

    public static final Map<UUID, Double> playerSpeeds = new HashMap<>();
    public static final Map<UUID, Vector> playerLastLocations = new HashMap<>();

}

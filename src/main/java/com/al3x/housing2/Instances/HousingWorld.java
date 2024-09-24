package com.al3x.housing2.Instances;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Actions.SendTitleAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HouseSize;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

import static com.al3x.housing2.Utils.Color.colorize;

public class HousingWorld {

    // Multiverse Core
    MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    MVWorldManager worldManager = core.getMVWorldManager();
    private World houseWorld;

    // The actual owners UUID
    private UUID ownerUUID;

    // Randomly generated House UUID
    private UUID houseUUID;

    // Stats about the house. More public data
    private String name;
    private int size;
    private int guests;
    private int cookies;
    private long timeCreated;
    private Location spawn;
    private List<String> scoreboard;

    // Action Stuff
    private Map<EventType, List<Action>> eventActions;

    // Stats
    private StatManager statManager;

    public HousingWorld(Player owner, HouseSize size) {
        // Set up the Information
        this.ownerUUID = owner.getUniqueId();
        this.name = owner.getName() + "'s House";
        this.houseUUID = UUID.randomUUID();
        this.guests = 0;
        this.cookies = 0;
        this.timeCreated = System.currentTimeMillis();
        this.statManager = new StatManager(house);
        switch (size) {case MEDIUM -> this.size = 50;case LARGE -> this.size = 75;case XLARGE -> this.size = 100;case MASSIVE -> this.size = 255;default -> this.size = 30;}

        // Create the actual world
        worldManager.addWorld(
                this.houseUUID.toString(),
                World.Environment.NORMAL,
                null,
                WorldType.FLAT,
                false,
                "VoidGen"
        );
        this.houseWorld = Bukkit.getWorld(this.houseUUID.toString());
        this.spawn = new Location(Bukkit.getWorld(this.houseUUID.toString()), 0, 61, 0);
        createTemplatePlatform();

        // Actions, Scoreboard, Default Stuff ya know?
        this.scoreboard = new ArrayList<>();
        this.scoreboard.add(colorize("&fName: %player.name%"));
        this.scoreboard.add(colorize("&fhawk tua! Ping: %player.ping%"));
        this.scoreboard.add(colorize("&fSprinting! %player.isSprinting%"));
        this.scoreboard.add(colorize("&f"));
        this.scoreboard.add(colorize("&aPlayers: &2%house.guests%"));
        this.scoreboard.add(colorize("&eCookies: &6%house.cookies%"));

        eventActions = new HashMap<>();
        addEventAction(EventType.PLAYER_JOIN, new SendTitleAction("test", "shut up"));
    }

    private void createTemplatePlatform() {
        int platformSize = 13; // 13x13 platform
        int startX = -platformSize / 2;
        int startZ = -platformSize / 2;

        for (int x = startX; x <= -startX; x++) {
            for (int z = startZ; z <= -startZ; z++) {
                // Place stone as the base (one block below the grass)
                houseWorld.getBlockAt(x, 59, z).setType(Material.STONE);

                // Place grass on top of the stone
                houseWorld.getBlockAt(x, 60, z).setType(Material.GRASS_BLOCK);
            }
        }
    }

    public void addEventAction(EventType eventType, Action action) {
        // Shoutout to chatgpt cause i have 0 clue what this means
        eventActions.computeIfAbsent(eventType, k -> new ArrayList<>()).add(action);
    }

    public void executeEventActions(EventType eventType, Player player) {
        List<Action> actions = eventActions.get(eventType);
        if (actions != null) {
            for (Action action : actions) {
                action.execute(player);
            }
        }
    }

    // Helper Method for delete()
    private boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (houseWorld.getPlayers().contains(player)) {
                kickPlayerFromHouse(player);
                player.sendMessage(colorize("&e&lThis house has been deleted!"));
            }
        }

        Bukkit.unloadWorld(houseWorld, false);
        deleteWorld(houseWorld.getWorldFolder());
    }

    public void broadcast(String s) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().getName().equals(houseUUID)) {
                player.sendMessage(s);
            }
        }
    }

    public void setEventActions(EventType type, List<Action> actions) {
        eventActions.put(type, actions);
    }
    public List<Action> getEventActions(EventType type) {
        return eventActions.get(type);
    }
    public void sendPlayerToHouse(Player player) {
        player.teleport(spawn);
    }
    public void kickPlayerFromHouse(Player player) {
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }
    public void incGuests() {
        guests++;
    }
    public void decGuests() {
        guests--;
    }
    public World getWorld() {
        return houseWorld;
    }
    public String getName() {
        return name;
    }
    public Long getTimeCreated() {
        return timeCreated;
    }
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
    public UUID getHouseUUID() {
        return houseUUID;
    }
    public Location getSpawn() {
        return spawn;
    }
    public List<String> getScoreboard() {
        return scoreboard;
    }
    public int getGuests() {
        return guests;
    }
    public int getCookies() {
        return cookies;
    }
    public int getSize() {
        return size;
    }
}

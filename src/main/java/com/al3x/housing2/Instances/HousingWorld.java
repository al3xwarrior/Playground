package com.al3x.housing2.Instances;

import com.al3x.housing2.Actions.Action;
import com.al3x.housing2.Actions.SendTitleAction;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.HouseSize;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import org.bukkit.*;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

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
    private String description;
    private int size;
    private int guests;
    private int cookies;
    private long timeCreated;
    private Location spawn;
    private List<String> scoreboard;

    // NPCs
    private List<HousingNPC> housingNPCS;

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
        this.description = "";
        this.timeCreated = System.currentTimeMillis();
        this.housingNPCS = new ArrayList<>();
        this.statManager = new StatManager(this);
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
        this.scoreboard.add("&fName: %player.name%");
        this.scoreboard.add("&fhawk tua! Ping: %player.ping%");
        this.scoreboard.add("&fSprinting! %player.isSprinting%");
        this.scoreboard.add("&f");
        this.scoreboard.add("Twerks: &6%stat.player/twerks%");
        this.scoreboard.add("&r");
        this.scoreboard.add("&aPlayers: &2%house.guests%");
        this.scoreboard.add("&eCookies: &6%house.cookies%");

        eventActions = new HashMap<>();
    }

    private void createTemplatePlatform() {
        int platformSize = 15; // 13x13 platform
        int startX = -platformSize / 2;
        int startZ = -platformSize / 2;

        for (int x = startX; x <= -startX; x++) {
            for (int z = startZ; z <= -startZ; z++) {
                // Place stone as the base (one block below the grass)
                houseWorld.getBlockAt(x, 59, z).setType(Material.STONE);

                // Place grass block on top of the stone
                houseWorld.getBlockAt(x, 60, z).setType((Math.random() > 0.25) ? Material.GRASS_BLOCK : Material.COARSE_DIRT);

                // Place grass on top of grass block
                if (Math.random() < 0.2) houseWorld.getBlockAt(x, 61, z).setType(Material.SHORT_GRASS);
            }
        }
    }

    public void addEventAction(EventType eventType, Action action) {
        // Shoutout to chatgippity cause i have 0 clue what this means
        eventActions.computeIfAbsent(eventType, k -> new ArrayList<>()).add(action);
    }

    public void executeEventActions(EventType eventType, Player player, Cancellable event) {
        List<Action> actions = eventActions.get(eventType);
        if (actions != null) {
            for (Action action : actions) {
                // Check if the action is null or if the event is allowed
                if (action.allowedEvents() != null && !action.allowedEvents().contains(eventType)) return;
                // Execute the action either cancelling the event or not
                if (event != null && !action.execute(player)) event.setCancelled(true);
                if (event == null) action.execute(player);
            }
        }
    }

    public void createNPC(Player player, Location location) {
        HousingNPC npc = new HousingNPC(player, location);
        housingNPCS.add(npc);
    }

    public HousingNPC getNPC(String id) {
        for (HousingNPC npc : housingNPCS) {
            Bukkit.getLogger().info("Does " + npc.getNpcUUID() + " equal " + id + "? " + npc.getNpcUUID().equals(id));
            if (npc.getNpcUUID().equals(id)) {
                return npc;
            }
        }
        return null;
    }

    public void removeNPC(String id) {
        for (HousingNPC npc : housingNPCS) {
            if (npc.getNpcUUID().equals(id)) {
                Npc fancyNPC = FancyNpcsPlugin.get().getNpcManager().getNpcById(id);
                if (fancyNPC == null) {
                    Bukkit.getLogger().info("NPC is null...");
                    return;
                }
                fancyNPC.removeForAll();
                FancyNpcsPlugin.get().getNpcManager().removeNpc(fancyNPC);
                housingNPCS.remove(npc);
                return;
            }
        }
    }

    public List<HousingNPC> getNPCs() {
        return housingNPCS;
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
                player.sendMessage(colorize(s));
            }
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setEventActions(EventType type, List<Action> actions) {
        eventActions.put(type, actions);
    }
    public List<Action> getEventActions(EventType type) {
        return eventActions.get(type);
    }
    public void sendPlayerToHouse(Player player) {
        player.teleport(spawn);
        player.sendMessage(colorize("&aSending you to " + name + "&a..."));
    }
    public StatManager getStatManager() {
        return statManager;
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
    public void setName(String s) {
        name = s;
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
    public void setScoreboard(List<String> scoreboard) {
        this.scoreboard = scoreboard;
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

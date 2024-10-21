package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Main;
import com.google.gson.Gson;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class HousesManager {
    private static final Gson gson = new Gson();
    private Main main;

    // All the loaded houses
    private final List<HousingWorld> loadedHouses;
    private ConcurrentHashMap<String, HousingWorld> concurrentLoadedHouses = new ConcurrentHashMap<>();

    // All the player houses
    private final HashMap<UUID, List<String>> playerHouses;
    private final List<String> housesById = new ArrayList<>();

    public HousesManager(Main main) {
        this.main = main;
        this.loadedHouses = new ArrayList<>();
        this.playerHouses = new HashMap<>();

        loadPlayerHouses();
    }

    private void loadPlayerHouses() {
        File houses = new File(main.getDataFolder(), "houses/");
        if (!houses.exists()) {
            houses.mkdirs();
        }

        for (File file : houses.listFiles()) {
            main.getLogger().info("Loading house " + file.getName());
            try {
                HouseData houseData = gson.fromJson(Files.readString(file.toPath()), HouseData.class);

                List<String> hs = playerHouses.getOrDefault(UUID.fromString(houseData.getOwnerID()), new ArrayList<>());
                hs.add(houseData.getHouseID());
                housesById.add(houseData.getHouseID());
                playerHouses.put(UUID.fromString(houseData.getOwnerID()), hs);
            } catch (Exception e) {
                main.getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        }

        main.getLogger().info("Loaded " + playerHouses.size() + " player houses");
    }

    public HousingWorld createHouse(Player owner, HouseSize size) {
        HousingWorld house = new HousingWorld(main, owner, size);
        loadedHouses.add(house);
        concurrentLoadedHouses.put(house.getHouseUUID().toString(), house);
        playerHouses.getOrDefault(owner.getUniqueId(), new ArrayList<>()).add(house.getHouseUUID().toString());
        return house;
    }



    public List<HousingWorld> getLoadedHouses() {
        return concurrentLoadedHouses.values().stream().toList();
    }

    public ConcurrentHashMap<String, HousingWorld> getConcurrentLoadedHouses() {
        return concurrentLoadedHouses;
    }

    public HashMap<UUID, List<String>> getPlayerHouses() {
        return playerHouses;
    }

    public HousingWorld getHouse(Player owner) {
        for (HousingWorld house : loadedHouses) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                return house;
            }
        }

        if (playerHouses.containsKey(owner.getUniqueId())) {
            for (String houseID : playerHouses.get(owner.getUniqueId())) {
                HousingWorld house = new HousingWorld(main, owner, houseID);
                loadedHouses.add(house);
                concurrentLoadedHouses.put(house.getHouseUUID().toString(), house);
                return house;
            }
        }
        return null;
    }

    public HousingWorld getLoadedHouse(Player owner) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                return house;
            }
        }
        return null;
    }

    public HousingWorld getHouse(UUID houseUUID) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getHouseUUID().equals(houseUUID)) {
                return house;
            }
        }
        return null;
    }

    public HousingWorld getHouse(World world) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getWorld().equals(world)) {
                return house;
            }
        }
        return null;
    }

    public HouseData getHouseData(String houseID) {
        File houseFile = new File(main.getDataFolder(), "houses/" + houseID + ".json");
        if (!houseFile.exists()) {
            return null;
        }
        try {
            return gson.fromJson(Files.readString(houseFile.toPath()), HouseData.class);
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public boolean playerHasHouse(Player player) {
        for (HousingWorld house : loadedHouses) {
            if (house.getOwnerUUID().equals(player.getUniqueId())) return true;
        }
        if (playerHouses.containsKey(player.getUniqueId())) {
            return !playerHouses.get(player.getUniqueId()).isEmpty();
        }
        return false;
    }

    public void deleteHouse(Player owner) {
        for (HousingWorld house : loadedHouses) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                loadedHouses.remove(house);
                concurrentLoadedHouses.remove(house.getHouseUUID().toString());
                house.delete();
                return;
            }
        }

        if (playerHouses.containsKey(owner.getUniqueId())) {
            for (String houseID : playerHouses.get(owner.getUniqueId())) {
                HousingWorld house = new HousingWorld(main, owner, houseID);
                house.delete();
                return;
            }
        }
    }

    public void deleteHouse(UUID houseUUID) {
        for (HousingWorld house : loadedHouses) {
            if (house.getHouseUUID().equals(houseUUID)) {
                house.delete();
                loadedHouses.remove(house);
                return;
            }
        }
    }

    public void saveHouseAndUnload(Player owner) {
        HousingWorld house = getHouse(owner);
        if (house != null) {
            house.save();
            house.unload();
            loadedHouses.remove(house);
            concurrentLoadedHouses.remove(house.getHouseUUID().toString());
        }
    }

    public void saveHouseAndUnload(HousingWorld house) {
        if (house != null) {
            house.save();
            house.unload();
            loadedHouses.remove(house);
            concurrentLoadedHouses.remove(house.getHouseUUID().toString());
        }
    }
}

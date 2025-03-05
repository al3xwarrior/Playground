package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Enums.HouseSize;
import com.al3x.housing2.Enums.permissions.Permissions;
import com.al3x.housing2.Instances.HousingData.HouseData;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class HousesManager {
    public static final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeAdapter()).create();
    private Main main;

    // All the loaded houses
    private ConcurrentHashMap<String, HousingWorld> concurrentLoadedHouses = new ConcurrentHashMap<>();

    // All the player houses
    private final HashMap<UUID, List<String>> playerHouses;
    private final List<String> housesById = new ArrayList<>();

    public HousesManager(Main main) {
        this.main = main;
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
                HouseData houseData = gson.fromJson(Files.readString(file.toPath(), StandardCharsets.UTF_8), HouseData.class);

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

        concurrentLoadedHouses.put(house.getHouseUUID().toString(), house);

        List<String> houses = playerHouses.getOrDefault(owner.getUniqueId(), new ArrayList<>());
        houses.add(house.getHouseUUID().toString());
        playerHouses.put(owner.getUniqueId(), houses);

        housesById.add(house.getHouseUUID().toString());

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

    public List<HouseData> getAllHouseData() {
        List<HouseData> houseData = new ArrayList<>();
        for (String houseID : housesById) {
            houseData.add(getHouseData(houseID));
        }
        return houseData;
    }

    @Deprecated
    public HousingWorld getHouse(Player owner) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                return house;
            }
        }

        if (playerHouses.containsKey(owner.getUniqueId())) {
            for (String houseID : playerHouses.get(owner.getUniqueId())) {
                HousingWorld house = new HousingWorld(main, owner, houseID);
                house.runOnLoadOrNow((h) -> concurrentLoadedHouses.put(house.getHouseUUID().toString(), h));
                return house;
            }
        }
        return null;
    }

    public int getHouseCount(Player owner) {
        int count = 0;
        if (playerHouses.containsKey(owner.getUniqueId())) {
            count += playerHouses.get(owner.getUniqueId()).size();
        }
        return count;
    }

    public boolean playerHasHouse(Player player) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(player.getUniqueId())) return true;
        }
        if (playerHouses.containsKey(player.getUniqueId())) {
            return !playerHouses.get(player.getUniqueId()).isEmpty();
        }
        return false;
    }

    public boolean playerHasHouse(OfflinePlayer player) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(player.getUniqueId())) return true;
        }
        if (playerHouses.containsKey(player.getUniqueId())) {
            return !playerHouses.get(player.getUniqueId()).isEmpty();
        }
        return false;
    }

    public List<HousingWorld> getLoadedHouses(Player owner) {
        List<HousingWorld> houses = new ArrayList<>();
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                houses.add(house);
                if (houses.size() == 3) return houses;
            }
        }
        return houses;
    }

    public List<HousingWorld> getLoadedHouses(OfflinePlayer owner) {
        List<HousingWorld> houses = new ArrayList<>();
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                houses.add(house);
                if (houses.size() == 3) return houses;
            }
        }
        return houses;
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

    public boolean hasPermissionInHouse(Player player, Permissions permission) {
        HousingWorld house = getHouse(player.getWorld());
        if (house == null) return false;
        return house.hasPermission(player, permission);
    }

    public boolean playerHasHouse(String playerName) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwner().getName() == null) continue;
            if (house.getOwner().getName().equalsIgnoreCase(playerName)) return true;
        }

        for (HouseData houseData : getAllHouseData()) {
            if (houseData.getOwnerName() == null) {
                continue;
            }
            if (houseData.getOwnerName().equalsIgnoreCase(playerName)) return true;
        }
        return false;
    }

    public void deleteHouse(Player owner) {
        for (HousingWorld house : getLoadedHouses()) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                concurrentLoadedHouses.remove(house.getHouseUUID().toString());
                house.delete();
                housesById.remove(house.getHouseUUID().toString());
                playerHouses.get(owner.getUniqueId()).remove(house.getHouseUUID().toString());
            }
        }

        if (playerHouses.containsKey(owner.getUniqueId())) {
            for (String houseID : playerHouses.get(owner.getUniqueId())) {
                playerHouses.get(owner.getUniqueId()).remove(houseID);
                HousingWorld house = new HousingWorld(main, owner, houseID);
                house.delete();
                housesById.remove(houseID);
                concurrentLoadedHouses.remove(house.getHouseUUID().toString());
                return;
            }
        }
    }

    public void deleteHouse(UUID houseUUID) {
        HousingWorld house = getHouse(houseUUID);
        if (house != null) {
            concurrentLoadedHouses.remove(houseUUID.toString());
            house.delete();
            housesById.remove(houseUUID.toString());
            playerHouses.get(house.getOwnerUUID()).remove(houseUUID.toString());
        } else {
            HouseData houseData = getHouseData(houseUUID.toString());
            if (houseData != null) {
                playerHouses.get(UUID.fromString(houseData.getOwnerID())).remove(houseUUID.toString());
                HousingWorld h2 = new HousingWorld(main, Bukkit.getOfflinePlayer(houseData.getOwnerID()), houseUUID.toString());
                h2.delete();
                housesById.remove(houseUUID.toString());
                concurrentLoadedHouses.remove(houseUUID.toString());
            }
        }
    }

    public void addSongToHouse(HousingWorld house, Song song) {
        house.addSong(song);
    }
    public void removeSongFromHouse(HousingWorld house, Song song) {
        house.removeSong(song);
    }

    public void saveHouseAndUnload(HousingWorld house) {
        if (house != null) {
            house.save();
            house.unload();
            concurrentLoadedHouses.remove(house.getHouseUUID().toString());
        }
    }

    public HousingWorld loadHouse(OfflinePlayer player, String houseID) {
        HousingWorld house = new HousingWorld(main, player, houseID);
        house.runOnLoadOrNow((h) -> concurrentLoadedHouses.put(house.getHouseUUID().toString(), h));
        return house;
    }

    //Updated to allow for houses that haven't been loaded yet tom be added to the list
    public HousingWorld getRandomPublicHouse() {
        ArrayList<HouseData> publicHouses = new ArrayList<>(getAllHouseData().stream()
                .filter(houseData -> houseData.getPrivacy().equals("PUBLIC"))
                .toList());

        if (publicHouses.isEmpty()) return null;
        HouseData houseData = publicHouses.get((int) (Math.random() * publicHouses.size()));
        if (houseData == null) return null;
        if (concurrentLoadedHouses.containsKey(houseData.getHouseID())) {
            return concurrentLoadedHouses.get(houseData.getHouseID());
        }
        return loadHouse(main.getServer().getOfflinePlayer(UUID.fromString(houseData.getOwnerID())), houseData.getHouseID());
    }

    public HousingWorld getRandomHouse() {
        if (getLoadedHouses().isEmpty()) return null;
        return getLoadedHouses().get((int) (Math.random() * getLoadedHouses().size()));
    }
}

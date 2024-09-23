package com.al3x.housing2.Instances;

import com.al3x.housing2.Commands.Housing;
import com.al3x.housing2.Enums.HouseSize;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HousesManager {

    // All the houses
    private List<HousingWorld> houses;

    public HousesManager() {
        this.houses = new ArrayList<>();
    }

    public HousingWorld createHouse(Player owner, HouseSize size) {
        HousingWorld house = new HousingWorld(owner, size);
        houses.add(house);
        return house;
    }

    public List<HousingWorld> getHouses() {
        return houses;
    }

    public HousingWorld getHouse(Player owner) {
        for (HousingWorld house : houses) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                return house;
            }
        }
        return null;
    }

    public HousingWorld getHouse(UUID houseUUID) {
        for (HousingWorld house : houses) {
            if (house.getHouseUUID().equals(houseUUID)) {
                return house;
            }
        }
        return null;
    }

    public HousingWorld getHouse(World world) {
        for (HousingWorld house : houses) {
            if (house.getWorld().equals(world)) {
                return house;
            }
        }
        return null;
    }

    public boolean playerHasHouse(Player player) {
        for (HousingWorld house : houses) {
            if (house.getOwnerUUID().equals(player.getUniqueId())) return true;
        }
        return false;
    }

    public void deleteHouse(Player owner) {
        for (HousingWorld house : houses) {
            if (house.getOwnerUUID().equals(owner.getUniqueId())) {
                houses.remove(house);
                house.delete();
                return;
            }
        }
    }

    public void deleteHouse(UUID houseUUID) {
        for (HousingWorld house : houses) {
            if (house.getHouseUUID().equals(houseUUID)) {
                house.delete();
                houses.remove(house);
                return;
            }
        }
    }

}

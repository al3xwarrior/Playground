package com.al3x.housing2.Utils;

import com.al3x.housing2.Instances.HousingWorld;

public class HousingComparison implements java.util.Comparator<HousingWorld> {
    @Override
    public int compare(HousingWorld house1, HousingWorld house2) {
        return house1.getGuests() - house2.getGuests();
    }
}

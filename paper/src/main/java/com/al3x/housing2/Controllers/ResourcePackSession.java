package com.al3x.housing2.Controllers;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Utils.DurationString;

import java.time.Instant;

public class ResourcePackSession {
    public Instant expires;
    public HousingWorld house;

    public ResourcePackSession(HousingWorld house) {
        this.expires = DurationString.convertToExpiryTime("10m");
        this.house = house;
    }
}

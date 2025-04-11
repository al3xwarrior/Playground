package com.al3x.housing2.Enums;

public enum Locations {
    CUSTOM,
    HOUSE_SPAWN,
    PLAYER_LOCATION,
    INVOKERS_LOCATION,
    ;

    public static Locations fromString(String string) {
        for (Locations location : values()) {
            if (location.name().equalsIgnoreCase(string)) {
                return location;
            }
        }
        return null;
    }
}

package com.al3x.housing2.Enums;

public enum Locations {
    CUSTOM,
    HOUSE_SPAWN,
    PLAYER_LOCATION,
    INVOKERS_LOCATION,
    ;

    public static Locations fromString(String string) {
        return switch (string) {
            case "CUSTOM" -> CUSTOM;
            case "HOUSE_SPAWN" -> HOUSE_SPAWN;
            case "PLAYER_LOCATION" -> PLAYER_LOCATION;
            case "INVOKERS_LOCATION" -> INVOKERS_LOCATION;
            default -> null;
        };
    }
}

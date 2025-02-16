package com.al3x.housing2.Enums;

public enum Locations {
    CUSTOM,
    HOUSE_SPAWN,
    PLAYER_LOCATION,
    INVOKERS_LOCATION,
    SEND_TO_LOBBY;


    public static Locations fromString(String string) {
        return switch (string) {
            case "CUSTOM" -> CUSTOM;
            case "HOUSE_SPAWN" -> HOUSE_SPAWN;
            case "PLAYER_LOCATION" -> PLAYER_LOCATION;
            case "INVOKERS_LOCATION" -> INVOKERS_LOCATION;
            case "SEND_TO_LOBBY" -> SEND_TO_LOBBY;
            default -> null;
        };
    }
}

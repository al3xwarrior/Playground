package com.al3x.housing2.Enums;

public enum PushDirection {
    FORWARD,
    BACKWARD,
    UP,
    DOWN,
    RIGHT,
    LEFT,
    NORTH,
    SOUTH,
    EAST,
    WEST,
    ;

    public static PushDirection fromString(String string) {
        for (PushDirection direction : values()) {
            if (direction.name().equalsIgnoreCase(string)) {
                return direction;
            }
        }
        return null;
    }
}

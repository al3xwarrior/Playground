package com.al3x.housing2.Enums.permissions;

public class Gamemodes implements PermissionInterface {
    public static final Gamemodes ADVENTURE = new Gamemodes("ADVENTURE");
    public static final Gamemodes SURVIVAL = new Gamemodes("SURVIVAL");
    public static final Gamemodes CREATIVE = new Gamemodes("CREATIVE");

    private static final Gamemodes[] VALUES = {ADVENTURE, SURVIVAL, CREATIVE};

    private final String name;

    private Gamemodes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Object cycle(Object value, boolean forward) {
        int index = 0;
        for (int i = 0; i < VALUES.length; i++) {
            if (VALUES[i] == value) {
                index = i;
                break;
            }
        }

        if (forward) {
            index++;
            if (index >= VALUES.length) {
                index = 0;
            }
        } else {
            index--;
            if (index < 0) {
                index = VALUES.length - 1;
            }
        }

        return VALUES[index];
    }
}
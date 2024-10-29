package com.al3x.housing2.Enums;


import com.al3x.housing2.Instances.Stat;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Material.*;

public enum StatOperation implements EnumMaterial{
    INCREASE(GREEN_STAINED_GLASS),
    DECREASE(RED_STAINED_GLASS),
    SET(YELLOW_STAINED_GLASS),
    MULTIPLY(ORANGE_STAINED_GLASS),
    DIVIDE(BLUE_STAINED_GLASS),
    MOD(MAGENTA_STAINED_GLASS),
    FLOOR(WHITE_STAINED_GLASS),
    ROUND(BROWN_STAINED_GLASS),
    GET_STAT(FEATHER),
    CONCAT(BOOK),
    INDEX_OF(CHAIN),
    SET_STRING(PAPER),
    LENGTH_OF(BREEZE_ROD),
    CHAR_AT(SPYGLASS),
    ;

    Material material;

    StatOperation(Material material) {
        this.material = material;
    }

    public String asString() {
        return switch (this) {
            case INCREASE -> "+";
            case DECREASE -> "-";
            case SET -> "=";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
            case MOD -> "%";
            case FLOOR -> "floor";
            case ROUND -> "round";
            case GET_STAT -> "get";
            case CONCAT -> "concat";
            case INDEX_OF -> "indexOf";
            case SET_STRING -> "set";
            case LENGTH_OF -> "lengthOf";
            case CHAR_AT -> "charAt";
        };
    }

    public ArrayList<String> getArgs() {
        return switch (this) {
            case INCREASE, DECREASE, SET, MULTIPLY, DIVIDE, MOD -> new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"));
            case FLOOR, ROUND -> new ArrayList<>(Arrays.asList("MODE", "Number"));
            case GET_STAT -> new ArrayList<>(Arrays.asList("MODE", "Stat Name"));
            case CONCAT -> new ArrayList<>(Arrays.asList("String 1", "MODE", "String 2"));
            case INDEX_OF -> new ArrayList<>(Arrays.asList("String", "MODE", "Target"));
            case SET_STRING -> new ArrayList<>(Arrays.asList("MODE", "String"));
            case LENGTH_OF -> new ArrayList<>(Arrays.asList("MODE", "String"));
            case CHAR_AT -> new ArrayList<>(Arrays.asList("String", "MODE", "Index"));
        };
    }

    public boolean expressionOnly() {
        return this == GET_STAT || this == INDEX_OF || this == LENGTH_OF || this == CHAR_AT;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}

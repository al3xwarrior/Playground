package com.al3x.housing2.Enums;


import java.util.ArrayList;
import java.util.Arrays;

public enum StatOperation {
    INCREASE,
    DECREASE,
    SET,
    MULTIPLY,
    DIVIDE,
    MOD,
    FLOOR,
    ROUND,
    GET_STAT,
    CONCAT,
    INDEX_OF,
    SET_STRING,
    LENGTH_OF,
    CHAR_AT,
    ;

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
}

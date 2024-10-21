package com.al3x.housing2.Enums;


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
    SET_STRING
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
        };
    }
}

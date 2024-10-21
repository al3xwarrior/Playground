package com.al3x.housing2.Enums;

public enum ExpressionOperation {
    INCREASE,
    DECREASE,
    MULTIPLY,
    DIVIDE
    ;

    public String asString() {
        return switch (this) {
            case INCREASE -> "+";
            case DECREASE -> "-";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
        };
    }
}

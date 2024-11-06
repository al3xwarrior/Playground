package com.al3x.housing2.Enums;

import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Material.*;

public enum StatOperation implements EnumMaterial {
    INCREASE(GREEN_STAINED_GLASS, "+", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    DECREASE(RED_STAINED_GLASS, "-", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    SET(YELLOW_STAINED_GLASS, "=", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    MULTIPLY(ORANGE_STAINED_GLASS, "*", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    DIVIDE(BLUE_STAINED_GLASS, "/", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    MOD(MAGENTA_STAINED_GLASS, "%", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    FLOOR(WHITE_STAINED_GLASS, "floor", new ArrayList<>(Arrays.asList("MODE", "Number"))),
    ROUND(BROWN_STAINED_GLASS, "round", new ArrayList<>(Arrays.asList("MODE", "Number"))),
    PLAYER_STAT(FEATHER, "pStat", new ArrayList<>(Arrays.asList("MODE", "Stat Name"))),
    GLOBAL_STAT(PLAYER_HEAD, "gStat", new ArrayList<>(Arrays.asList("MODE", "Stat Name"))),
    CONCAT(BOOK, "concat", new ArrayList<>(Arrays.asList("String 1", "MODE", "String 2"))),
    INDEX_OF(CHAIN, "indexOf", new ArrayList<>(Arrays.asList("String", "MODE", "Target"))),
    SET_STRING(PAPER, "set", new ArrayList<>(Arrays.asList("MODE", "String"))),
    LENGTH_OF(BREEZE_ROD, "lengthOf", new ArrayList<>(Arrays.asList("MODE", "String"))),
    CHAR_AT(SPYGLASS, "charAt", new ArrayList<>(Arrays.asList("String", "MODE", "Index")));

    private final Material material;
    private final String asString;
    private final ArrayList<String> args;

    StatOperation(Material material, String asString, ArrayList<String> args) {
        this.material = material;
        this.asString = asString;
        this.args = args;
    }

    public String asString() {
        return asString;
    }

    public String toString() {
        return StringUtilsKt.formatCapitalize(name());
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public boolean expressionOnly() {
        return switch (this) {
            case PLAYER_STAT, GLOBAL_STAT, INDEX_OF, LENGTH_OF, CHAR_AT -> true;
            default -> false;
        };
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
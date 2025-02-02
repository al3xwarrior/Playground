package com.al3x.housing2.Enums;

import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Material.*;

public enum VelocityOperation implements EnumMaterial, EnumHTSLAlternative {
    SET(YELLOW_STAINED_GLASS, "=", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    ADD(GREEN_STAINED_GLASS, "+", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    SUBTRACT(RED_STAINED_GLASS, "-", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    MULTIPLY(ORANGE_STAINED_GLASS, "*", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    DIVIDE(BLUE_STAINED_GLASS, "/", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2"))),
    MOD(MAGENTA_STAINED_GLASS, "%", new ArrayList<>(Arrays.asList("Number 1", "MODE", "Number 2")));

    private final Material material;
    private final String asString;
    private final ArrayList<String> args;

    VelocityOperation(Material material, String asString, ArrayList<String> args) {
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

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String getAlternative() {
        return asString;
    }
}

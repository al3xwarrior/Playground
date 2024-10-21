package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.util.*;

public class Stat {

    private UUID uuid;
    private String stat;
    private String value;

    public Stat(UUID uuid, String stat, String value) {
        this.uuid = uuid;
        this.stat = stat;
        this.value = value;
    }

    public static String modifyStat(StatOperation operation, String value1, String value2) {
        //Check all possible combinations of value and this.value
        if (modifyDoubleIfInt(operation, value1, value2) != null) {
            return value1 = String.valueOf(modifyDoubleIfInt(operation, value1, value2));
        } else if (modifyIntIfDouble(operation, value1, value2) != null) {
            return value1 = String.valueOf(modifyIntIfDouble(operation, value1, value2));
        } else if (modifyIntIfInt(operation, value1, value2) != null) {
            return value1 = String.valueOf(modifyIntIfInt(operation, value1, value2));
        } else if (modifyDoubleIfDouble(operation, value1, value2) != null) {
            return value1 = String.valueOf(modifyDoubleIfDouble(operation, value1, value2));
        }

        //If all else fails, just return the value or append the value
        return modifyStringIfString(operation, value1, value2);
    }

    public String modifyStat(StatOperation operation, String value) {
        return this.value = modifyStat(operation, this.value, value);
    }

    public static Integer modifyDoubleIfInt(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isInt(numStr) && NumberUtilsKt.isDouble(valueStr)) {
            double value = Double.parseDouble(valueStr);
            int num = Integer.parseInt(numStr);
            return switch (operation) {
                case StatOperation.SET -> num = (int) value;
                case StatOperation.INCREASE -> num += value;
                case StatOperation.DECREASE -> num -= value;
                case StatOperation.MULTIPLY -> num *= value;
                case StatOperation.DIVIDE -> num /= value;
                case StatOperation.MOD -> num %= value;
                case StatOperation.FLOOR -> (int) Math.floor(num);
                case StatOperation.ROUND -> Math.round(num);
                default -> num;
            };
        }
        return null;
    }

    public static Double modifyDoubleIfDouble(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isDouble(numStr) && NumberUtilsKt.isDouble(valueStr)) {
            double value = Double.parseDouble(valueStr);
            double num = Double.parseDouble(numStr);
            return switch (operation) {
                case StatOperation.SET -> num = value;
                case StatOperation.INCREASE -> num += value;
                case StatOperation.DECREASE -> num -= value;
                case StatOperation.MULTIPLY -> num *= value;
                case StatOperation.DIVIDE -> num /= value;
                case StatOperation.MOD -> num %= value;
                case StatOperation.FLOOR -> Math.floor(num);
                case StatOperation.ROUND -> (double) Math.round(num);
                default -> num;
            };
        }
        return null;
    }

    public static Integer modifyIntIfInt(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isInt(numStr) && NumberUtilsKt.isInt(valueStr)) {
            int value = Integer.parseInt(valueStr);
            int num = Integer.parseInt(numStr);
            return switch (operation) {
                case StatOperation.SET -> num = value;
                case StatOperation.INCREASE -> num += value;
                case StatOperation.DECREASE -> num -= value;
                case StatOperation.MULTIPLY -> num *= value;
                case StatOperation.DIVIDE -> num /= value;
                case StatOperation.MOD -> num %= value;
                case StatOperation.FLOOR -> (int) Math.floor(num);
                case StatOperation.ROUND -> Math.round(num);
                default -> num;
            };
        }
        return null;
    }

    public static Double modifyIntIfDouble(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isDouble(numStr) && NumberUtilsKt.isInt(valueStr)) {
            int value = Integer.parseInt(valueStr);
            double num = Integer.parseInt(numStr);
            return switch (operation) {
                case StatOperation.SET -> num = value;
                case StatOperation.INCREASE -> num += value;
                case StatOperation.DECREASE -> num -= value;
                case StatOperation.MULTIPLY -> num *= value;
                case StatOperation.DIVIDE -> num /= value;
                case StatOperation.MOD -> num %= value;
                case StatOperation.FLOOR -> Math.floor(num); //These really don't make sense lol
                case StatOperation.ROUND -> (double) Math.round(num);
                default -> num;
            };
        }
        return null;
    }

    public static String modifyStringIfString(StatOperation operation, String numStr, String valueStr) {
        return switch (operation) {
            case StatOperation.SET -> valueStr;
            case StatOperation.INCREASE -> numStr + valueStr; //This is basically append (shrug)
            default -> valueStr;
        };
    }

    public String formatValue() {
        if (NumberUtilsKt.isInt(value)) {
            return String.valueOf(Integer.parseInt(value));
        } else if (NumberUtilsKt.isDouble(value)) {
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            return df.format(Double.parseDouble(value));
        }
        return value;
    }

    public String getValue() {
        return value;
    }

    public String getStatName() {
        return stat;
    }

    public UUID getUUID() {
        return uuid;
    }

}

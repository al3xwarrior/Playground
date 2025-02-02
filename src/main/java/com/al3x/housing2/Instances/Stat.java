package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.StatOperation;
import com.al3x.housing2.Utils.NumberUtilsKt;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.util.*;

import static com.al3x.housing2.Enums.StatOperation.*;

public class Stat {

    private String stat;
    private String value;

    public Stat(String stat, String value) {
        this.stat = stat;
        this.value = value;
    }

    public static String modifyStat(StatOperation operation, String value1, String value2) {
        //Check all possible combinations of value and this.value
        if (value1 == null) value1 = "null";
        if (value2 == null) value2 = "null";

        if (modifyDoubleIfInt(operation, value1, value2) != null) {
            return String.valueOf(modifyDoubleIfInt(operation, value1, value2));
        } else if (modifyIntIfDouble(operation, value1, value2) != null) {
            return String.valueOf(modifyIntIfDouble(operation, value1, value2));
        } else if (modifyIntIfInt(operation, value1, value2) != null) {
            return String.valueOf(modifyIntIfInt(operation, value1, value2));
        } else if (modifyDoubleIfDouble(operation, value1, value2) != null) {
            return String.valueOf(modifyDoubleIfDouble(operation, value1, value2));
        }

        //If all else fails, just return the value or append the value
        return modifyStringIfString(operation, value1, value2);
    }

    public String modifyStat(StatOperation operation, String value) {
        this.value = modifyStat(operation, this.value, value);

        return this.value;
    }

    public static Integer modifyDoubleIfInt(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isInt(numStr) && NumberUtilsKt.isDouble(valueStr)) {
            double value = Double.parseDouble(valueStr);
            int num = Integer.parseInt(numStr);
            return switch (operation) {
                case SET -> num = (int) value;
                case INCREASE -> num += value;
                case DECREASE -> num -= value;
                case MULTIPLY -> num *= value;
                case DIVIDE -> num /= value;
                case MOD -> num %= value;
                case FLOOR -> (int) Math.floor(num);
                case ROUND -> Math.round(num);
                default -> null;
            };
        }
        return null;
    }

    public static Double modifyDoubleIfDouble(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isDouble(numStr) && NumberUtilsKt.isDouble(valueStr)) {
            double value = Double.parseDouble(valueStr);
            double num = Double.parseDouble(numStr);
            return switch (operation) {
                case SET -> num = value;
                case INCREASE -> num += value;
                case DECREASE -> num -= value;
                case MULTIPLY -> num *= value;
                case DIVIDE -> num /= value;
                case MOD -> num %= value;
                case FLOOR -> Math.floor(num);
                case ROUND -> (double) Math.round(num);
                default -> null;
            };
        }
        return null;
    }

    public static Integer modifyIntIfInt(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isInt(numStr) && NumberUtilsKt.isInt(valueStr)) {
            int value = Integer.parseInt(valueStr);
            int num = Integer.parseInt(numStr);
            return switch (operation) {
                case SET -> num = value;
                case INCREASE -> num += value;
                case DECREASE -> num -= value;
                case MULTIPLY -> num *= value;
                case DIVIDE -> num /= value;
                case MOD -> num %= value;
                case FLOOR -> (int) Math.floor(num);
                case ROUND -> Math.round(num);
                default -> null;
            };
        }
        return null;
    }

    public static Double modifyIntIfDouble(StatOperation operation, String numStr, String valueStr) {
        if (NumberUtilsKt.isDouble(numStr) && NumberUtilsKt.isInt(valueStr)) {
            int value = Integer.parseInt(valueStr);
            double num = Double.parseDouble(numStr);
            return switch (operation) {
                case SET -> num = value;
                case INCREASE -> num += value;
                case DECREASE -> num -= value;
                case MULTIPLY -> num *= value;
                case DIVIDE -> num /= value;
                case MOD -> num %= value;
                case FLOOR -> Math.floor(num); //These really don't make sense lol
                case ROUND -> (double) Math.round(num);
                default -> null;
            };
        }
        return null;
    }

    public static String modifyStringIfString(StatOperation operation, String numStr, String valueStr) {
        return switch (operation) {
            case SET -> valueStr;
            case INCREASE -> numStr + valueStr; //This is basically append (shrug)
            case CONCAT -> numStr + valueStr;
            case INDEX_OF -> String.valueOf(numStr.indexOf(valueStr));
            case SET_STRING -> valueStr;
            case LENGTH_OF -> String.valueOf(valueStr.length());
            case CHAR_AT -> String.valueOf(numStr.charAt(Integer.parseInt(valueStr)));
            default -> null;
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

    public boolean isNotNumber() {
        return !NumberUtilsKt.isInt(value) && !NumberUtilsKt.isDouble(value);
    }

    public String getValue() {
        return value;
    }

    public String getStatName() {
        return stat;
    }

}
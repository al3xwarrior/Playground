package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.StatComparator;
import com.al3x.housing2.Utils.NumberUtilsKt;

public class Comparator {
    //Compare as int, double, or string
    public static boolean compare(StatComparator comparator, String value1, String value2) {
        //Check all possible combinations of value1 and value2
        if (NumberUtilsKt.isInt(value1) && NumberUtilsKt.isInt(value2)) {
            return compare(comparator, Integer.parseInt(value1), Integer.parseInt(value2));
        } else if (NumberUtilsKt.isInt(value1) && NumberUtilsKt.isDouble(value2)) {
            return compare(comparator, Integer.parseInt(value1), Double.parseDouble(value2));
        } else if (NumberUtilsKt.isDouble(value1) && NumberUtilsKt.isInt(value2)) {
            return compare(comparator, Double.parseDouble(value1), Integer.parseInt(value2));
        } else if (NumberUtilsKt.isDouble(value1) && NumberUtilsKt.isDouble(value2)) {
            return compare(comparator, Double.parseDouble(value1), Double.parseDouble(value2));
        } else {
            return switch (comparator) {
                case LESS_THAN -> value1.compareTo(value2) < 0;
                case LESS_THAN_OR_EQUAL -> value1.compareTo(value2) <= 0;
                case EQUALS -> value1.equals(value2);
                case GREATER_THAN_OR_EQUAL -> value1.compareTo(value2) >= 0;
                case GREATER_THAN -> value1.compareTo(value2) > 0;
            };
        }
    }

    public static boolean compare(StatComparator comparator, int value1, int value2) {
        return switch (comparator) {
            case LESS_THAN -> value1 < value2;
            case LESS_THAN_OR_EQUAL -> value1 <= value2;
            case EQUALS -> value1 == value2;
            case GREATER_THAN_OR_EQUAL -> value1 >= value2;
            case GREATER_THAN -> value1 > value2;
        };
    }

    public static boolean compare(StatComparator comparator, double value1, int value2) {
        return switch (comparator) {
            case LESS_THAN -> value1 < value2;
            case LESS_THAN_OR_EQUAL -> value1 <= value2;
            case EQUALS -> value1 == value2;
            case GREATER_THAN_OR_EQUAL -> value1 >= value2;
            case GREATER_THAN -> value1 > value2;
        };
    }

    public static boolean compare(StatComparator comparator, int value1, double value2) {
        return switch (comparator) {
            case LESS_THAN -> value1 < value2;
            case LESS_THAN_OR_EQUAL -> value1 <= value2;
            case EQUALS -> value1 == value2;
            case GREATER_THAN_OR_EQUAL -> value1 >= value2;
            case GREATER_THAN -> value1 > value2;
        };
    }

    public static boolean compare(StatComparator comparator, double value1, double value2) {
        return switch (comparator) {
            case LESS_THAN -> value1 < value2;
            case LESS_THAN_OR_EQUAL -> value1 <= value2;
            case EQUALS -> value1 == value2;
            case GREATER_THAN_OR_EQUAL -> value1 >= value2;
            case GREATER_THAN -> value1 > value2;
        };
    }
}

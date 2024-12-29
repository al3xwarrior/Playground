package com.al3x.housing2.Enums.permissions;

public class ChatSettings implements PermissionInterface {
    public static final ChatSettings ON = new ChatSettings("ON");
    public static final ChatSettings OFF = new ChatSettings("OFF");
    public static final ChatSettings ONE = new ChatSettings("ONE");
    public static final ChatSettings TWO = new ChatSettings("TWO");
    public static final ChatSettings THREE = new ChatSettings("THREE");
    public static final ChatSettings FIVE = new ChatSettings("FIVE");
    public static final ChatSettings TEN = new ChatSettings("TEN");
    public static final ChatSettings FIFTEEN = new ChatSettings("FIFTEEN");
    public static final ChatSettings THIRTY = new ChatSettings("THIRTY");
    public static final ChatSettings FOURTY_FIVE = new ChatSettings("FOURTY_FIVE");
    public static final ChatSettings SIXTY = new ChatSettings("SIXTY");

    private static final ChatSettings[] VALUES = {
            ON, OFF, ONE, TWO, THREE, FIVE, TEN, FIFTEEN, THIRTY, FOURTY_FIVE, SIXTY
    };

    private final String name;

    private ChatSettings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Object cycle(Object value) {
        int index = 0;
        for (int i = 0; i < VALUES.length; i++) {
            if (VALUES[i] == value) {
                index = i;
                break;
            }
        }
        index++;
        if (index >= VALUES.length) {
            index = 0;
        }
        return VALUES[index];
    }
}
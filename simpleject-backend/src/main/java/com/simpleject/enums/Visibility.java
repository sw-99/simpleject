package com.simpleject.enums;

import java.util.Arrays;

public enum Visibility {
    PRIVATE, PUBLIC, INTERNAL;

    public static boolean isValid(String value) {
        return Arrays.stream(Visibility.values())
                .map(Enum::name)
                .anyMatch(v -> v.equalsIgnoreCase(value));
    }
}

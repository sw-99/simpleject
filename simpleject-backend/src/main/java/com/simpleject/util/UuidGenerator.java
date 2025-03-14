package com.simpleject.util;

import java.util.UUID;

public class UuidGenerator {
    public static String generate() {
        return UUID.randomUUID().toString();
    }

}

package com.example.cardgame.domain.util;

public class Validations {
    private Validations() {}

    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}

package com.syncano.library.utils;

public class Validate {
    public static <T> void checkNotNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotNullAndZero(Integer object, String message) {
        checkNotNull(object, message);
        checkIsTrue(object != 0, message);
    }

    public static void checkIsTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotNullAndNotEmpty(String object, String message) {
        checkNotNull(object, message);
        checkIsTrue(object.length() > 0, message);
    }
}

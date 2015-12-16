package com.syncano.library.utils;

public class Log {
    public static void d(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    public static void w(String tag, String message) {
        android.util.Log.w(tag, message);
    }

    public static void e(String tag, String message) {
        android.util.Log.e(tag, message);
    }
}

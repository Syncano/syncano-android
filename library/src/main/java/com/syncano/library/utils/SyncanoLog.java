package com.syncano.library.utils;

public class SyncanoLog {
    private static SyncanoLogger syncanoLogger;

    public static void initLogger(SyncanoLogger logger) {
        syncanoLogger = logger;
    }

    public static void release() {
        syncanoLogger = null;
    }

    public static void d(String tag, String message) {
        if (logIsEnabled()) {
            syncanoLogger.d(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (logIsEnabled()) {
            syncanoLogger.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (logIsEnabled()) {
            syncanoLogger.e(tag, message);
        }
    }

    private static boolean logIsEnabled() {
        return syncanoLogger != null;
    }
}

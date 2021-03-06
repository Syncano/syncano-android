package com.syncano.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.syncano.library.PlatformType;
import com.syncano.library.Syncano;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.parser.GsonParser;

public class UserMemory {

    public static void saveUserToStorage(Syncano syncano, AbstractUser user) {
        if (syncano.getAndroidContext() == null || !PlatformType.isAndroid()) {
            return;
        }

        SharedPreferences prefs = syncano.getAndroidContext().getSharedPreferences(Syncano.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (user == null) {
            editor.remove(dataKey(syncano));
            editor.remove(typeKey(syncano));
        } else {
            GsonParser.GsonParseConfig config = new GsonParser.GsonParseConfig();
            config.serializeReadOnlyFields = true;
            Gson gson = GsonParser.createGson(user.getClass(), config);
            editor.putString(dataKey(syncano), gson.toJson(user));
            editor.putString(typeKey(syncano), user.getClass().getName());
        }
        editor.apply();
    }

    public static AbstractUser getUserFromStorage(Syncano syncano) {
        if (syncano.getAndroidContext() == null || !PlatformType.isAndroid()) {
            return null;
        }
        SharedPreferences prefs = syncano.getAndroidContext().getSharedPreferences(Syncano.class.getSimpleName(), Context.MODE_PRIVATE);
        String userJson = prefs.getString(dataKey(syncano), null);
        String className = prefs.getString(typeKey(syncano), null);
        if (userJson == null || className == null) {
            return null;
        }
        try {
            Class type = Class.forName(className);
            return (AbstractUser) GsonParser.createGson(type).fromJson(userJson, type);
        } catch (Exception e) {
            SyncanoLog.w(Syncano.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    private static String dataKey(Syncano s) {
        return s.getNotEmptyInstanceName() + "_" + s.getNotEmptyApiKey() + "_user";
    }

    private static String typeKey(Syncano s) {
        return s.getNotEmptyInstanceName() + "_" + s.getNotEmptyApiKey() + "_class";
    }
}

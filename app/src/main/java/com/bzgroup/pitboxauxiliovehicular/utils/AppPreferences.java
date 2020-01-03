package com.bzgroup.pitboxauxiliovehicular.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    public class Keys {
        public static final String UUID_GMAIL = "UUID_GMAIL";
        public static final String UUID_FACEBOOK = "UUID_FACEBOOK";
        public static final String NAME = "NAME";
        public static final String LAST_NAME = "LAST_NAME";
        public static final String EMAIL = "EMAIL";
        public static final String PASSWORD = "PASSWORD";
        public static final String URI_AVATAR = "URI_AVATAR";
        public static final String SIGNIN_FB = "SIGNIN_FB";
        public static final String SIGNIN_G = "SIGNIN_G";
        public static final String SIGNIN_TYPE = "SIGNIN_TYPE";

        public static final String KEYSTORE_FILENAME = "KEYSTORE_FILENAME";
        public static final String REGISTERED = "REGISTERED";
        public static final String USER_UUID = "USER_UUID";
        public static final String USER_ADDRESS = "USER_ADDRESS";

        public static final String USER_LAT_LOCATION = "USER_LAT_LOCATION";
        public static final String USER_LNG_LOCATION = "USER_LNG_LOCATION";
        public static final String USER_SCHEDULE_ORDER = "USER_SCHEDULE_ORDER";
    }

    private static final String SETTINGS_NAME = "default_settings_app";
    private static AppPreferences INSTANCE;
    private SharedPreferences sharedPreferences;

    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new AppPreferences(context.getApplicationContext());
        return INSTANCE;
    }

    public void writeString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void writeBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String readString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public boolean readBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}

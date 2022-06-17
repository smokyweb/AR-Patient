package com.ar.patient.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class Pref {
    private static SharedPreferences sharedPreferences = null;

    private static void openPref(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.PREF_FILE,
                Context.MODE_PRIVATE);
    }


    // String get Pref value
    public static String getValue(Context context, String key, String defaultValue) {
        Pref.openPref(context);
        String result = Pref.sharedPreferences.getString(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void clearData(Context context) {
        String pushId = getValue(context, Config.PREF_PUSH_ID, "");
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.clear();
        prefsPrivateEditor.apply();
        Pref.sharedPreferences = null;
        setValue(context, Config.PREF_TUTORAL, "1");
        setValue(context, Config.PREF_PUSH_ID, pushId);
    }

    public static void setValue(Context context, String key, String value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.apply();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static void setValue(Context context, String key, float value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putFloat(key, value);
        prefsPrivateEditor.apply();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static boolean getValue(Context context, String key,
                                   boolean defaultValue) {
        Pref.openPref(context);
        boolean result = Pref.sharedPreferences.getBoolean(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static float getValue(Context context, String key, float defaultValue) {
        Pref.openPref(context);
        float result = Pref.sharedPreferences.getFloat(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void setValue(Context context, String key, boolean value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putBoolean(key, value);
        prefsPrivateEditor.apply();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }


    public static void setValue(Context context, String key, int value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.apply();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

}
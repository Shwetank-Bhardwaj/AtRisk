package com.ibm.shwetank.atrisk;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref instance;
    private SharedPreferences preference;
    private SharedPref(Context context) {
        preference = context.getSharedPreferences("data", 0);
    }

    public static SharedPref getInstance(Context context){
        if(instance == null){
            instance = new SharedPref(context);
        }
        return instance;
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key){
        return preference.getString(key, null);
    }

    public void putInt(String key, int value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key){
        return preference.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key){
        return preference.getBoolean(key, false);
    }
}

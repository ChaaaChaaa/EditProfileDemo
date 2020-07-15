package com.practice.edituserprofiledemo;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceBase {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public Context context;
    private static SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase();

    public static SharedPreferenceBase getInstance() {
        return sharedPreferenceBase;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setString(String key, String value) {

        editor.putString(key, value);
        editor.commit();
    }

    public void setBoolean(String key, Boolean value) {

        editor.putBoolean(key, value);
        editor.commit();
    }
    public void setInt(String key, int value) {

        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key, String value) {
        pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, value);
    }
    public int getInt(String key, int value) {
        pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getInt(key, value);
    }
    public Boolean getBoolean(String key, boolean value) {
        pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean(key, value);
    }
}


package com.practice.edituserprofiledemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    public static UserPreference prf = new UserPreference();

    private static final String USER_PREFERENCES = "USER_PREFERENCES";
    private static final String LOGGED_IN_PREF = "logged_in_status";
    private static SharedPreferences userSharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;

    public static UserPreference getInstance() {
        return prf;
    }

    public void setContext(Context context) {
        userSharedPreferences = getPreference(context);
        sharedPreferencesEditor = getPreferenceEditor(context);
    }

    private SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(USER_PREFERENCES, Activity.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getPreferenceEditor(Context context) {
        return userSharedPreferences.edit();
    }

    public void setLoggedIn(Context context, boolean loggedIn) {
        sharedPreferencesEditor.putBoolean(LOGGED_IN_PREF, loggedIn);
        sharedPreferencesEditor.apply();
    }

    public boolean getLoggedStatus() {
        return userSharedPreferences.getBoolean(LOGGED_IN_PREF, false);
    }

    public void clearUserLogin() {
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.commit();
    }

    public void putString(String key, String value) {

        sharedPreferencesEditor.putString(key, value);
        sharedPreferencesEditor.commit();
    }

    public String getUserImage(){
        return userSharedPreferences.getString(Config.KEY_IMAGE,"");
    }

    public void setUserImage(String userImage){
        sharedPreferencesEditor.putString(Config.KEY_IMAGE,userImage).commit();
    }

    public String getString(String key) {
        return userSharedPreferences.getString(key, null);
    }
}
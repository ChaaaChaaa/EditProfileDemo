package com.practice.edituserprofiledemo;

import android.app.Application;
import android.content.SharedPreferences;


public class Myapplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceBase.getInstance().setContext(this);
    }
}

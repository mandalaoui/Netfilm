package com.example.androidapp;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;
    private String globalUserId;
    private boolean isAdmin;

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
    }

    // Static method to get the instance of this application class
    public static MyApplication getInstance() {
        return instance;
    }

    // Getter method for admin status
    public boolean isAdmin() {
        return isAdmin;
    }

    // Setter method for admin status
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // Getter method for global user ID
    public String getGlobalUserId() {
        return globalUserId;
    }

    // Setter method for global user ID
    public void setGlobalUserId(String value) {
        this.globalUserId = value;
    }

    // Static method to get the application context globally
    public static Context getAppContext() {
        return context;
    }
}

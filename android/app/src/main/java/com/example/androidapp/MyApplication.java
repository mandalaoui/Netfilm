package com.example.androidapp;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
    private String globalUserId;
    private boolean isAdmin;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getGlobalUserId() {
        return globalUserId;
    }

    public void setGlobalUserId(String value) {
        this.globalUserId = value;
    }
}
package com.example.androidapp;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
    private String globalVariable;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public String getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(String value) {
        this.globalVariable = value;
    }
}
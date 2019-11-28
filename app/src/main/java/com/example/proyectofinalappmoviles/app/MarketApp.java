package com.example.proyectofinalappmoviles.app;

import android.app.Application;
import android.content.Context;

public class MarketApp extends Application {

    private static Context context;

    public static Context getAppContext() {
        return MarketApp.context;
    }

    public void onCreate() {
        super.onCreate();
        MarketApp.context = getApplicationContext();
    }
}

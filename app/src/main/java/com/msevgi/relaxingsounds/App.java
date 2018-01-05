package com.msevgi.relaxingsounds;

import android.app.Application;

import com.msevgi.relaxingsounds.api.ApiModule;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiModule.init();
    }
}

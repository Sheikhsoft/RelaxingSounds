package com.msevgi.relaxingsounds;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.msevgi.relaxingsounds.api.ApiModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ApiModule.init();
        initRealm();
    }

    private void initRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}

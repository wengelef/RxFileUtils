package com.example.wengelef.rxfileutils;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Flo on 23.10.2016.
 */
public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}

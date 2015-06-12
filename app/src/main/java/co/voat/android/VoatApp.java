package co.voat.android;

import android.app.Application;

import timber.log.Timber;

/**
 * Aaaaaap
 * Created by John on 6/11/2015.
 */
public class VoatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}

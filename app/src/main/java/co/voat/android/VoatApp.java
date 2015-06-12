package co.voat.android;

import android.app.Application;

import com.google.gson.Gson;

import timber.log.Timber;

/**
 * Aaaaaap
 * Created by John on 6/11/2015.
 */
public class VoatApp extends Application {

    private static Gson gson;
    public static Gson gson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}

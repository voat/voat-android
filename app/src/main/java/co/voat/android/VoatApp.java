package co.voat.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import co.voat.android.data.User;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Aaaaaapp
 * Created by John on 6/11/2015.
 */
public class VoatApp extends Application {

    private static Gson gson;
    public static Gson gson() {
        if (gson == null) {
            gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        }
        return gson;
    }

    private static Bus bus;
    public static Bus bus() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }

    private static VoatApp instance;
    public static VoatApp instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        //If we have previously logged in, set our state
        if (User.getCurrentUser() == null) {
            User.setCurrentUser(VoatPrefs.getUser(this));
        }
    }
}

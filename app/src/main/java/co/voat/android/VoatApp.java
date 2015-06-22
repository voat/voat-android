package co.voat.android;

import android.app.Application;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import co.voat.android.data.User;
import timber.log.Timber;

/**
 * Aaaaaapp
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

    private static Bus bus;
    public static Bus bus() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        //If we have previously logged in, set our state
        if (User.getCurrentUser() == null) {
            User.setCurrentUser(VoatPrefs.getUser(this));
        }
    }
}

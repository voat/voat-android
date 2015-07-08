package co.voat.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import co.voat.android.data.User;

/**
 * Store things locally as prefs because easy
 * Created by Jawn on 6/12/2015.
 */
public class VoatPrefs {

    private static final String PREF_FILE_NAME = "voat_prefs";

    private static final String PREF_USER = "pref_user";
    private static final String PREF_CONFIRM_EXIT = "pref_confirm_exit";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static User getUser(Context context) {
        String userJson = getSharedPreferences(context).getString(PREF_USER, null);
        if (!TextUtils.isEmpty(userJson)) {
            return VoatApp.gson().fromJson(userJson, User.class);
        }
        return null;
    }

    public static void putUser(Context context, User user) {
        getSharedPreferences(context)
                .edit()
                .putString(PREF_USER, VoatApp.gson().toJson(user))
                .apply();
    }

    public static void clearUser(Context context) {
        getSharedPreferences(context)
                .edit()
                .remove(PREF_USER)
                .apply();
    }

    public static boolean isConfirmExit(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_CONFIRM_EXIT, false);
    }

    public static void setConfirmExit(Context context, boolean confirmation) {
        getSharedPreferences(context)
                .edit()
                .putBoolean(PREF_CONFIRM_EXIT, confirmation)
                .apply();
    }
}

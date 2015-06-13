package co.voat.android.util;

import android.content.Context;

import co.voat.android.R;

/**
 * Kind of a cache of all strings so that we don't load them 102183018 times from resources
 * Created by Jawn on 6/13/2015.
 */
public class CommonStrings {

    private static String in;
    public static String in(Context context) {
        if (in == null) {
            in = context.getString(R.string.in);
        }
        return in;
    }
}

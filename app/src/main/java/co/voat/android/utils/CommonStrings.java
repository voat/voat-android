package co.voat.android.utils;

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

    private static String self;
    public static String self(Context context) {
        if (self == null) {
            self = context.getString(R.string.self);
        }
        return self;
    }

    private static String dot;
    public static String dot(Context context) {
        if (dot == null) {
            dot = context.getString(R.string.dot);
        }
        return dot;
    }

    private static String points;
    public static String points(Context context) {
        if (points == null) {
            points = context.getString(R.string.points);
        }
        return points;
    }
}

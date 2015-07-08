package co.voat.android.utils;

import android.content.Context;
import android.util.TypedValue;

import co.voat.android.R;

/**
 * De colores
 * Created by Jawn on 6/13/2015.
 */
public class CommonColors {

    private static TypedValue typedValue = new TypedValue();

    private static int colorPrimary = -1;
    public static int colorPrimary(Context context) {
        if (colorPrimary == -1) {
            context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            colorPrimary = typedValue.data;
        }
        return colorPrimary;
    }

    private static int colorPrimaryDark = -1;
    public static int colorPrimaryDark(Context context) {
        if (colorPrimaryDark == -1) {
            context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            colorPrimaryDark = typedValue.data;
        }
        return colorPrimaryDark;
    }

    private static int colorAccent = -1;
    public static int colorAccent(Context context) {
        if (colorAccent == -1) {
            context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            colorAccent = typedValue.data;
        }
        return colorAccent;
    }

    private static int colorForeground = -1;
    public static int colorForground(Context context) {
        if (colorForeground == -1) {
            context.getTheme().resolveAttribute(android.R.attr.colorForeground, typedValue, true);
            colorForeground = typedValue.data;
        }
        return colorForeground;
    }

    private static int colorForegroundInverse = -1;
    public static int colorForegroundInverse(Context context) {
        if (colorForegroundInverse == -1) {
            context.getTheme().resolveAttribute(android.R.attr.colorForegroundInverse, typedValue, true);
            colorForegroundInverse = typedValue.data;
        }
        return colorForegroundInverse;
    }

    /**
     * Clear all the colors that have to do with the theme so that we do not
     * cache values that relate to the theme applied
     */
    public static void clear() {
        colorForeground = -1;
        colorForegroundInverse = -1;
    }
}

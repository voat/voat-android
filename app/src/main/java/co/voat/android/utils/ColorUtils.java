package co.voat.android.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

/**
 * Paint it black
 * Created by Jawn on 6/13/2015.
 */
public class ColorUtils {


    public static SpannableString colorWords(String str, int color) {
        return colorWords(str, str.length(), color);
    }

    public static SpannableString colorWords(String str, int endIndex, int color) {
        return colorWords(str, 0, endIndex, color);
    }

    public static SpannableString colorWords(String str, int startIndex, int endIndex, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, 0);
        return ss;
    }

    public static Drawable getColoredDrawable(Context context, int resId, int color) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = context.getResources().getDrawable(resId, context.getTheme());
        } else {
            drawable = context.getResources().getDrawable(resId);
            drawable = DrawableCompat.wrap(drawable);
        }
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }
}

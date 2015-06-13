package co.voat.android.util;

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
}

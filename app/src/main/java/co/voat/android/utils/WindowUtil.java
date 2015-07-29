package co.voat.android.utils;

import android.graphics.Point;
import android.view.Display;
import android.view.Window;

/**
 * Created by Jawn on 7/15/2015.
 */
public class WindowUtil {

    private static Point size;
    public static int getScreenWidth(Window window) {
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Window window) {
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size.y;
    }
}

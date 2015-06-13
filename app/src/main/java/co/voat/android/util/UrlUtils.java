package co.voat.android.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jawn on 6/13/2015.
 */
public class UrlUtils {

    private static URL url;
    public static String getBaseUrl(String fullUrl) {
        try {
            url = new URL(fullUrl);
            return url.getHost();
        } catch (MalformedURLException url) {
            //meh
        }
        return "";
    }
}

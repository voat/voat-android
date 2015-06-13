package co.voat.android.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import co.voat.android.R;

/**
 * Open stuff
 * Created by Jawn on 6/13/2015.
 */
public class IntentUtils {

    public static boolean openBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static boolean share(Context context, String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_url_via)));
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}

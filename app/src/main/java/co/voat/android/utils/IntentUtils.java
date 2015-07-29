package co.voat.android.utils;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

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

    public static void download(Context context, String fileName, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // This put the download in the same Download dir the browser uses
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // When downloading music and videos they will be listed in the player
        // (Seems to be available since Honeycomb only)
        request.allowScanningByMediaScanner();

        // Notify user when download is completed
        // (Seems to be available since Honeycomb only)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Start download
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }
}

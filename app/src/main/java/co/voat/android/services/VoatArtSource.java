package co.voat.android.services;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.Random;

import co.voat.android.api.SubmissionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Submission;
import co.voat.android.util.UrlUtils;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Plugin for Muzei
 * Created by John on 6/19/2015.
 */
public class VoatArtSource extends RemoteMuzeiArtSource{

    private static final String TAG = "VoatArtSource";
    private static final String SOURCE_NAME = "VoatArtSource";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours

    public VoatArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(VoatClient.FAKE_API_URL)
                .setRequestInterceptor(new VoatClient.VoatRequestInterceptor())
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        int statusCode = retrofitError.getResponse().getStatus();
                        if (retrofitError.getKind() == RetrofitError.Kind.NETWORK
                                || (500 <= statusCode && statusCode < 600)) {
                            return new RetryException();
                        }
                        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                        return retrofitError;
                    }
                })
                .build();

        VoatClient.Voat service = restAdapter.create(VoatClient.Voat.class);
        SubmissionsResponse response = service.getSubmissions("Playground");

        if (response == null || response.data == null) {
            throw new RetryException();
        }

        if (response.data.size() == 0) {
            scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
            return;
        }

        Random random = new Random();
        Submission submission;
        String token;
        while (true) {
            submission = response.data.get(random.nextInt(response.data.size()));
            token = Integer.toString(submission.getId());
            if (response.data.size() <= 1 || !TextUtils.equals(token, currentToken)
                    || UrlUtils.isImageLink(submission.getUrl())) {
                break;
            }
        }

        publishArtwork(new Artwork.Builder()
                .title(submission.getTitle())
                .byline(submission.getUserName())
                .imageUri(Uri.parse(submission.getUrl()))
                .token(token)
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(submission.getUrl())))
                .build());

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
    }
}

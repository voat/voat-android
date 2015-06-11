package co.voat.android.api;

import co.voat.android.BuildConfig;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;

public class VoatClient {
    //Replace with your API_KEY
    private static final String API_KEY_VALUE = "API_KEY_HERE";
    private static final String API_URL = "https://api.meh.com/1";
    private static final String PARAM_API_KEY = "apikey";

    private static Voat mVoat;

    public interface Voat {
        @GET("/current.json")
        void getVoat(
                Callback<VoatResponse> responseCallback
        );
    }

    public static Voat instance() {
        if (mVoat == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .setRequestInterceptor(new MehRequestInterceptor())
                    .build();
            mVoat = restAdapter.create(Voat.class);
        }
        return mVoat;
    }

    public static class MehRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam(PARAM_API_KEY, API_KEY_VALUE);
        }
    }
}
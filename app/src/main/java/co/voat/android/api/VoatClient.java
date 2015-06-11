package co.voat.android.api;

import co.voat.android.BuildConfig;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class VoatClient {
    //Replace with your API_KEY
    private static final String API_KEY_VALUE = "myUt2YVP/i8OKVovtTlOAQ==";
    private static final String API_URL = "http://vout.co/api/v1/";
    private static final String FAKE_API_URL = "http://fakevout.azurewebsites.net/api/v1/";
    private static final String PARAM_API_KEY = "Voat-ApiKey";

    private static Voat mVoat;

    public interface Voat {
        @GET("/v/{subverseName}")
        void getSubverse(
                @Path("subverseName") String user,
                Callback<SubverseResponse> responseCallback
        );
    }

    public static Voat instance() {
        if (mVoat == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(FAKE_API_URL)
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .setRequestInterceptor(new VoatRequestInterceptor())
                    .build();
            mVoat = restAdapter.create(Voat.class);
        }
        return mVoat;
    }

    public static class VoatRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader(PARAM_API_KEY, API_KEY_VALUE);
        }
    }
}
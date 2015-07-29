package co.voat.android.github;

import java.util.List;

import co.voat.android.BuildConfig;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Github is great!
 * Created by Jawn on 6/14/2015.
 */
public class GithubClient {

    public static final String API_URL = "https://api.github.com";

    public interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        void contributors(
                @Path("owner") String owner,
                @Path("repo") String repo,
                Callback<List<Contributor>> callback);
    }

    private static GitHub mGithub;
    public static GitHub instance() {
        if (mGithub == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .build();
            mGithub = restAdapter.create(GitHub.class);
        }
        return mGithub;
    }
}

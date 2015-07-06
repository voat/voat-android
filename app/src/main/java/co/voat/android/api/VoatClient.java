package co.voat.android.api;

import android.text.TextUtils;
import android.widget.Toast;

import co.voat.android.BuildConfig;
import co.voat.android.VoatApp;
import co.voat.android.data.Submission;
import co.voat.android.data.User;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.mime.TypedString;

/**
 * Client to get all the things from Voat
 * voat.co/api/help
 */
public class VoatClient {
    //Replace with your API_KEY
    public static final String API_KEY_VALUE = "myUt2YVP/i8OKVovtTlOAQ==";
    public static final String API_URL = "http://vout.co/api/";
    public static final String FAKE_API_URL = "http://fakevout.azurewebsites.net/api/";
    public static final String PARAM_API_KEY = "Voat-ApiKey";
    public static final String PARAM_AUTHORIZATION_KEY = "Authorization";

    private static Voat mVoat;

    public interface Voat {
        @GET("/v1/v/{subverse}")
        void getSubmissions(
                @Path("subverse") String subverse,
                Callback<SubmissionsResponse> responseCallback
        );

        @GET("/v1/v/{subverse}")
        SubmissionsResponse getSubmissions(
                @Path("subverse") String subverse
        );

        @POST("/v1/v/{subverse}")
        void postSubmission(
                @Path("subverse") String subverse,
                @Body SubmissionBody body,
                Callback<SubmissionResponse> responseCallback
        );

        @GET("/v1/v/{subverse}/{submissionID}")
        void getSubmission(
                @Path("subverse") String subverse,
                @Path("submissionID") String submissionID,
                Callback<Submission> responseCallback
        );

        @GET("/v1/v/{subverse}/info")
        void getSubverse(
                @Path("subverse") String subverse,
                Callback<SubverseResponse> responseCallback
        );

        @GET("/v1/v/{subverse}/{submissionID}/comments")
        void getComments(
                @Path("subverse") String subverse,
                @Path("submissionID") int submissionID,
                Callback<CommentsResponse> responseCallback
        );

        @GET("/v1/v/comments/{commentID}")
        void getComment(
                @Path("commentID") String commentID,
                Callback<CommentResponse> responseCallback
        );

        @POST("/v1/v/{subverse}/{submissionID}/comment")
        void postComment(
                @Path("subverse") String subverse,
                @Path("submissionID") int submissionID,
                @Body CommentBody body,
                Callback<CommentResponse> responseCallback
        );

        @GET("/v1/u/preferences")
        void getUserPreferences(
                Callback<UserPreferencesResponse> responseCallback
        );

        @GET("/v1/u/{user}/info")
        void getUserInfo(
                @Path("user") String user,
                Callback<UserResponse> responseCallback
        );

        @GET("/v1/u/{user}/comments")
        void getUserComments(
                @Path("user") String user,
                Callback<CommentsResponse> responseCallback
        );

        @GET("/v1/u/{user}/submissions")
        void getUserSubmissions(
                @Path("user") String user,
                Callback<SubmissionsResponse> responseCallback
        );

        @GET("/v1/u/{user}/subscriptions")
        void getUserSubscriptions(
                @Path("user") String user,
                Callback<SubscriptionsResponse> responseCallback
        );

        @GET("/v1/u/messages/{type}/{state}")
        void getUserMessages(
                @Path("type") String type,
                @Path("state") String state,
                Callback<UserMessagesResponse> responseCallback
        );

        @POST("/v1/u/messages")
        void postMessage(
                @Body MessageBody body,
                Callback<SimpleResponse> responseCallback
        );

        @POST("/v1/vote/{type}/{id}/{vote}")
        void postVote(
                @Path("type") String type,
                @Path("id") int id,
                @Path("vote") int vote,
                @Body String ignoreThisCauseRetrofitWontAcceptPostWithNoBody,
                Callback<VoteResponse> responseCallback
        );

        //Not yet
//        @GET("/u/saved")
//        void getUserSaved(
//                Callback<SubmissionsResponse> responseCallback
//        );

        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/token")
        void login(
                @Body TypedString auth,
                Callback<AuthResponse> authResponseCallback
        );
    }

    public static Voat instance() {
        if (mVoat == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(FAKE_API_URL)
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .setRequestInterceptor(new VoatRequestInterceptor())
                    .setErrorHandler(new VoatErrorHandler())
                    .build();
            mVoat = restAdapter.create(Voat.class);
        }
        return mVoat;
    }

    public static class VoatRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader(PARAM_API_KEY, API_KEY_VALUE);
            if (User.getCurrentUser() != null
                    && !TextUtils.isEmpty(User.getCurrentUser().getAuthToken().accessToken)) {
                request.addHeader(PARAM_AUTHORIZATION_KEY, "Bearer " + User.getCurrentUser().getAuthToken().accessToken);
            }
        }
    }

    public static class VoatErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(VoatApp.instance(), cause.toString(), Toast.LENGTH_LONG).show();
            }
            return cause;
        }
    }
}
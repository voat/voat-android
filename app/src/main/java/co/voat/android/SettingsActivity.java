package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.UserPreferencesResponse;
import co.voat.android.api.VoatClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jawn on 6/11/2015.
 */
public class SettingsActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        VoatClient.instance().getUserPreferences(new Callback<UserPreferencesResponse>() {
            @Override
            public void success(UserPreferencesResponse userPreferencesResponse, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}

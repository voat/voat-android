package co.voat.android;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.voat.android.api.AuthResponse;
import co.voat.android.api.VoatClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * What are you going to do without signing in... not much
 * Created by Jawn on 6/12/2015.
 */
public class LoginDialog extends AppCompatDialog {

    @InjectView(R.id.username)
    EditText usernameEditText;
    @InjectView(R.id.password)
    EditText passwordEditText;

    @OnClick(R.id.login)
    void onLoginClick(View v) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean hasError = false;
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("No!");
            hasError = true;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("No!");
            hasError = true;
        }
        if (!hasError) {
            final String auth = "grant_type=password&username=" + username + "&password" + password;
            VoatClient.instance().login(auth, new Callback<AuthResponse>() {
                @Override
                public void success(AuthResponse authResponse, Response response) {
                    Timber.d("Check it out: " + authResponse.accessToken);
                    dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Timber.e(error.toString());
                    dismiss();
                }
            });

        }
    }

    public LoginDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_login);
        ButterKnife.inject(this);
    }
}

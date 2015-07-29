package co.voat.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.VoatPrefs;
import co.voat.android.data.Submission;
import co.voat.android.data.User;
import co.voat.android.events.LoginEvent;
import co.voat.android.events.LogoffEvent;

/**
 * Drop da Base
 * Created by John on 6/11/2015.
 */
public class BaseActivity extends AppCompatActivity {

    private EventReceiver baseEventReceiver;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (VoatPrefs.isDarkTheme(this)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
        }
        super.onCreate(savedInstanceState);
        root = findViewById(R.id.root);
        baseEventReceiver = new EventReceiver();
        VoatApp.bus().register(baseEventReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoatApp.bus().unregister(baseEventReceiver);
    }

    public void gotoSettings() {
        startActivity(SettingsActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    public void gotoAbout() {
        startActivity(AboutActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    public void gotoSubmission(Submission submission) {
        gotoSubmission(submission, false);
    }

    public void gotoSubmission(Submission submission, boolean openToComments) {
        startActivity(SubmissionActivity.newInstance(this, submission, openToComments));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    public void gotoUser() {
        gotoUser(null);
    }

    public void gotoUser(User user) {
        startActivity(UserActivity.newInstance(this, user));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    public void gotoMySubscriptions() {
        startActivity(SubscriptionsActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    public void gotoMessages() {
        startActivity(MessagesActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.do_nothing, R.anim.fade_out);
    }

    private class EventReceiver {

        @Subscribe
        public void onLogoff(LogoffEvent event) {
            //TODO make this a snackbar once we figure out how to get the root
            Toast.makeText(BaseActivity.this, getString(R.string.successfully_logged_off), Toast.LENGTH_SHORT)
                    .show();
        }

        @Subscribe
        public void onLogin(LoginEvent event) {
            Toast.makeText(BaseActivity.this, getString(R.string.successfully_logged_in), Toast.LENGTH_SHORT)
                    .show();
        }

    }
}

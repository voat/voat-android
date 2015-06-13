package co.voat.android;

import android.support.v7.app.AppCompatActivity;

import co.voat.android.data.Submission;
import co.voat.android.data.User;

/**
 * Created by John on 6/11/2015.
 */
public class BaseActivity extends AppCompatActivity {

    protected void gotoSettings() {
        startActivity(SettingsActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    protected void gotoDetail(Submission submission) {
        startActivity(SubmissionActivity.newInstance(this, submission));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    protected void gotoImage(Submission submission) {
        startActivity(ImageActivity.newInstance(this, submission));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    protected void gotoUser() {
        gotoUser(null);
    }

    protected void gotoUser(User user) {
        startActivity(UserActivity.newInstance(this, user));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    protected void gotoMySubscriptions() {
        startActivity(SubscriptionsActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    protected void gotoMessages() {
        startActivity(MessagesActivity.newInstance(this));
        overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.do_nothing, R.anim.fade_out);
    }
}

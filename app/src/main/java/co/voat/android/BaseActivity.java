package co.voat.android;

import android.support.v7.app.AppCompatActivity;

import co.voat.android.data.Submission;

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.do_nothing, R.anim.fade_out);
    }
}

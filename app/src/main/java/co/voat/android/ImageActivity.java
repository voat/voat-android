package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.voat.android.data.Submission;
import co.voat.android.util.IntentUtils;
import uk.co.senab.photoview.PhotoView;

/**
 * Check out my selfie
 * Created by Jawn on 6/13/2015.
 */
public class ImageActivity extends BaseActivity {

    private static final String EXTRA_SUBMISSION = "extra_submission";

    public static Intent newInstance(Context context, Submission submission) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(EXTRA_SUBMISSION, submission);
        return intent;
    }

    @InjectView(R.id.root)
    View root;
    @InjectView(R.id.image)
    PhotoView image;

    @OnClick(R.id.upvote)
    void onUpVote(View v) {

    }
    @OnClick(R.id.downvote)
    void onDownVote(View v) {

    }
    @OnClick(R.id.share)
    void onClickShare(View v) {
        if (!IntentUtils.share(this, submission.getUrl())) {
            Snackbar.make(root, getString(R.string.no_share), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    @OnClick(R.id.download)
    void onClickDownload(View v) {

    }
    @OnClick(R.id.browser)
    void onClickBrowser(View v) {
        if (!IntentUtils.openBrowser(this, submission.getUrl())) {
            Snackbar.make(root, getString(R.string.no_browser), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    Submission submission;

    private final View.OnClickListener onImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);
        submission = (Submission) getIntent().getSerializableExtra(EXTRA_SUBMISSION);
        Glide.with(this)
                .load(submission.getUrl())
                .into(image);
        image.setOnClickListener(onImageClickListener);
    }
}

package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.data.Submission;

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

    @InjectView(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);
        Submission submission = (Submission) getIntent().getSerializableExtra(EXTRA_SUBMISSION);
        Glide.with(this)
                .load(submission.getUrl())
                .into(image);
    }
}

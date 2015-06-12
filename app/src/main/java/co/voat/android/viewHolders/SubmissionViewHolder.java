package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;
import co.voat.android.data.Submission;

/**
 * Submissions, yay!
 * Created by Jawn on 6/11/2015.
 */
public class SubmissionViewHolder extends RecyclerView.ViewHolder {

    public static SubmissionViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new SubmissionViewHolder(view);
    }

    @InjectView(R.id.post_score)
    TextView scoreText;
    @InjectView(R.id.post_title)
    TextView titleText;
    @InjectView(R.id.post_author)
    TextView authorText;
    @InjectView(R.id.post_image)
    ImageView image;

    public SubmissionViewHolder(View view) {
        super(view);
        ButterKnife.inject(this, view);
    }

    public void bind(Submission submission) {
        scoreText.setText(submission.getUpVotes() + "");
        titleText.setText(submission.getTitle());
        authorText.setText(submission.getUserName());
        Glide.with(image.getContext())
                .load(submission.getThumbnail())
                .into(image);
    }
}

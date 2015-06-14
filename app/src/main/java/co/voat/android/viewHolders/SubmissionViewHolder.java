package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import co.voat.android.util.ColorUtils;
import co.voat.android.util.CommonColors;
import co.voat.android.util.CommonStrings;
import co.voat.android.util.UrlUtils;

/**
 * Submissions, yay!
 * Created by Jawn on 6/11/2015.
 */
public class SubmissionViewHolder extends RecyclerView.ViewHolder {

    public static SubmissionViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_submission, parent, false);
        return new SubmissionViewHolder(view);
    }

    @InjectView(R.id.post_source)
    public TextView sourceText;
    @InjectView(R.id.post_title)
    public TextView titleText;
    @InjectView(R.id.post_origin)
    public TextView originText;
    @InjectView(R.id.post_image)
    public ImageView image;
    @InjectView(R.id.post_comments)
    public View comments;
    @InjectView(R.id.post_upvote)
    public View upVote;
    @InjectView(R.id.post_downvote)
    public View downVote;

    public SubmissionViewHolder(View view) {
        super(view);
        ButterKnife.inject(this, view);
    }

    public void bind(Submission submission) {
        sourceText.setText(ColorUtils.colorWords(submission.getUserName(), CommonColors.colorPrimary(itemView.getContext())));
        sourceText.append(" " + CommonStrings.in(itemView.getContext()) + " ");
        sourceText.append(ColorUtils.colorWords(submission.getSubverse(), CommonColors.colorPrimary(itemView.getContext())));
        titleText.setText(submission.getTitle());
        if (!TextUtils.isEmpty(submission.getUrl())) {
            originText.setText(UrlUtils.getBaseUrl(submission.getUrl()));
        } else {
            originText.setText(CommonStrings.self(itemView.getContext()) + "." + submission.getSubverse());
        }
        originText.append(" " + CommonStrings.dot(itemView.getContext()) + " " + submission.getDate());
        if (TextUtils.isEmpty(submission.getThumbnail())) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            Glide.with(image.getContext())
                    .load(submission.getThumbnail())
                    .into(image);
        }

    }
}

package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.data.Submission;
import co.voat.android.utils.ColorUtils;
import co.voat.android.utils.CommonColors;
import co.voat.android.utils.CommonStrings;
import co.voat.android.utils.UrlUtils;

/**
 * Submissions, yay!
 * Created by Jawn on 6/11/2015.
 */
public class SubmissionViewHolder extends RecyclerView.ViewHolder {

    public static SubmissionViewHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_submission, parent, false);
        return new SubmissionViewHolder(view);
    }

    @Bind(R.id.post_source)
    public TextView sourceText;
    @Bind(R.id.post_title)
    public TextView titleText;
    @Bind(R.id.post_origin)
    public TextView originText;
    @Bind(R.id.post_image)
    public ImageView image;
    @Bind(R.id.post_comments)
    public View comments;
    @Bind(R.id.comments_count)
    public TextView commentCount;
    @Bind(R.id.post_upvote)
    public View upVote;
    @Bind(R.id.upvote_count)
    public TextView upvoteCount;
    @Bind(R.id.post_downvote)
    public View downVote;
    @Bind(R.id.downvote_count)
    public TextView downvoteCount;

    public SubmissionViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
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
        commentCount.setText("" + submission.getCommentCount());
        upvoteCount.setText("" + submission.getUpVotes());
        downvoteCount.setText("" + submission.getDownVotes());
    }
}

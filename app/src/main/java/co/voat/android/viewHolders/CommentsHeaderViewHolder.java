package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
public class CommentsHeaderViewHolder extends RecyclerView.ViewHolder {

    public static CommentsHeaderViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comments_header, parent, false);
        return new CommentsHeaderViewHolder(view);
    }

    @Bind(R.id.post_source)
    public TextView sourceText;
    @Bind(R.id.post_title)
    public TextView titleText;
    @Bind(R.id.post_origin)
    public TextView originText;
    @Bind(R.id.post_image)
    public ImageView image;
    @Bind(R.id.post_content)
    public TextView contentText;

    public CommentsHeaderViewHolder(View view) {
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

        if (!TextUtils.isEmpty(submission.getFormattedContent())) {
            contentText.setText(Html.fromHtml(submission.getFormattedContent()));
            contentText.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            contentText.setVisibility(View.GONE);
        }

    }
}

package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.data.Comment;
import co.voat.android.utils.ColorUtils;
import co.voat.android.utils.CommonColors;
import co.voat.android.utils.CommonStrings;
import timber.log.Timber;

/**
 * Submissions, yay!
 * Created by Jawn on 6/11/2015.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    public static CommentViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Bind(R.id.comment_header)
    public TextView headerText;
    @Bind(R.id.comment_content)
    public TextView contentText;
    @BindDimen(R.dimen.padding_medium)
    int mediumPadding;

    public CommentViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(Comment comment) {
        headerText.setText(ColorUtils.colorWords(comment.getUserName(), CommonColors.colorPrimary(itemView.getContext())));
        headerText.append(" " + CommonStrings.dot(itemView.getContext()) + " ");
        headerText.append(comment.getUpVotes() - comment.getDownVotes() + " " +
                CommonStrings.points(itemView.getContext()));
        //TODO add timestamp
        contentText.setText(Html.fromHtml(comment.getFormattedContent()));
        contentText.setMovementMethod(LinkMovementMethod.getInstance());
        Timber.d("comment parent id " + comment.getParentId());

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (comment.getParentId() > 0) {
            lp.setMargins(mediumPadding * comment.getLevel(), 0, 0, 0);
        } else {
            lp.setMargins(0, 0, 0, 0);
        }
    }
}

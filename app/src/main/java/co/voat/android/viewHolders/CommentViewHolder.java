package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.data.Comment;

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

    @Bind(R.id.comment_score)
    public TextView scoreText;
    @Bind(R.id.comment_content)
    public TextView contentText;
    @Bind(R.id.comment_author)
    public TextView authorText;

    public CommentViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(Comment comment) {
        scoreText.setText(comment.getUpVotes() + "");
        contentText.setText(Html.fromHtml(comment.getFormattedContent()));
        contentText.setMovementMethod(LinkMovementMethod.getInstance());
        authorText.setText(comment.getUserName());
    }
}

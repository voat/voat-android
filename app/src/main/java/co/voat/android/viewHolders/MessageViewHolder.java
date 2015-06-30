package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.data.Message;

/**
 * Submissions, yay!
 * Created by Jawn on 6/11/2015.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    public static MessageViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Bind(R.id.comment_score)
    TextView scoreText;
    @Bind(R.id.comment_content)
    TextView contentText;
    @Bind(R.id.comment_author)
    TextView authorText;

    public MessageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(Message message) {
        scoreText.setText(message.getTypeName() + "");
        contentText.setText(message.getContent());
        authorText.setText(message.getSender());
    }
}

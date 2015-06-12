package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.UserMessagesResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Message;
import co.voat.android.viewHolders.MessageViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Check out that inbox!
 * Created by Jawn on 6/12/2015.
 */
public class MessagesActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MessagesActivity.class);
        return intent;
    }

    @InjectView(R.id.list)
    RecyclerView messagesList;
    @InjectView(R.id.empty_root)
    View emptyView;

    private final Callback<UserMessagesResponse> messagesResponseCallback = new Callback<UserMessagesResponse>() {
        @Override
        public void success(UserMessagesResponse messagesResponse, Response response) {
            if (messagesResponse.success
                    && messagesResponse.data != null
                    && !messagesResponse.data.isEmpty()) {
                emptyView.setVisibility(View.GONE);
                messagesList.setAdapter(new MessagesAdapter(messagesResponse.data));
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
            Snackbar.make(getWindow().getDecorView(), getString(R.string.error), Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        ButterKnife.inject(this);
        messagesList.setLayoutManager(new LinearLayoutManager(this));
        VoatClient.instance().getUserMessages(Message.MESSAGE_TYPE_ALL,
                Message.MESSAGE_STATE_ALL,
                messagesResponseCallback);
    }

    public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

        private List<Message> mValues;

        public Message getValueAt(int position) {
            return mValues.get(position);
        }

        public MessagesAdapter(List<Message> items) {
            mValues = items;
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MessageViewHolder holder = MessageViewHolder.create(parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MessageViewHolder holder, int position) {
            Message message = getValueAt(position);
            holder.bind(message);
            holder.itemView.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}

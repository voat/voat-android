package co.voat.android.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;
import co.voat.android.api.UserMessagesResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Message;
import co.voat.android.viewHolders.MessageViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Show dem messages
 * Created by Jawn on 6/12/2015.
 */
public class MessagesFragment extends BaseFragment {

    private static final String EXTRA_TYPE = "extra_type";

    public static MessagesFragment newInstance(String type) {
        MessagesFragment frag = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TYPE, type);
        frag.setArguments(args);
        return frag;
    }

    @InjectView(R.id.root)
    View root;
    @InjectView(R.id.empty_root)
    View emptyView;
    @InjectView(R.id.list)
    RecyclerView messagesList;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    String type;

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadMessages();
        }
    };

    private final Callback<UserMessagesResponse> messagesResponseCallback = new Callback<UserMessagesResponse>() {
        @Override
        public void success(UserMessagesResponse messagesResponse, Response response) {
            swipeRefreshLayout.setRefreshing(false);
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
            swipeRefreshLayout.setRefreshing(false);
            Timber.e(error.toString());
            Snackbar.make(root, getString(R.string.error), Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        type = getArguments().getString(EXTRA_TYPE);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        messagesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadMessages();
    }

    private void loadMessages() {
        swipeRefreshLayout.setRefreshing(true);
        VoatClient.instance().getUserMessages(type,
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

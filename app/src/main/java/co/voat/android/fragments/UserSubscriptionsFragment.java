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

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.api.SubscriptionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Subscription;
import co.voat.android.viewHolders.SubscriptionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Get the comments for a user
 * Created by Jawn on 6/20/2015.
 */
public class UserSubscriptionsFragment extends BaseFragment {

    private static final String EXTRA_USER = "extra_user";

    public static UserSubscriptionsFragment newInstance(String user) {
        UserSubscriptionsFragment fragment = new UserSubscriptionsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.root)
    View root;
    @Bind(R.id.empty_root)
    View emptyView;
    @Bind(R.id.list)
    RecyclerView messagesList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    String user;

    private final Callback<SubscriptionsResponse> subscriptionsResponseCallback = new Callback<SubscriptionsResponse>() {
        @Override
        public void success(SubscriptionsResponse subscriptionsResponse, Response response) {
            swipeRefreshLayout.setRefreshing(false);
            if (subscriptionsResponse.success
                    && subscriptionsResponse.data != null
                    && !subscriptionsResponse.data.isEmpty()) {
                emptyView.setVisibility(View.GONE);
                messagesList.setAdapter(new UserSubscriptionsAdapter(subscriptionsResponse.data));
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

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            load();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        user = getArguments().getString(EXTRA_USER);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        messagesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        load();
    }

    private void load() {
        swipeRefreshLayout.setRefreshing(true);
        VoatClient.instance().getUserSubscriptions(user, subscriptionsResponseCallback);
    }

    public class UserSubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

        private List<Subscription> mValues;

        public Subscription getValueAt(int position) {
            return mValues.get(position);
        }

        public UserSubscriptionsAdapter(List<Subscription> items) {
            mValues = items;
        }

        @Override
        public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SubscriptionViewHolder holder = SubscriptionViewHolder.newInstance(parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SubscriptionViewHolder holder, int position) {
            Subscription subscription = getValueAt(position);
            holder.bind(subscription);
            holder.itemView.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}

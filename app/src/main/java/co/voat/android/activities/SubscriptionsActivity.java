package co.voat.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.api.SubscriptionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Subscription;
import co.voat.android.data.User;
import co.voat.android.viewHolders.SubscriptionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Jawn on 6/12/2015.
 */
public class SubscriptionsActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SubscriptionsActivity.class);
        return intent;
    }

    @Bind(R.id.list)
    RecyclerView subversesList;
    @Bind(R.id.empty_root)
    View emptyView;

    private final Callback<SubscriptionsResponse> subscriptionsResponseCallback = new Callback<SubscriptionsResponse>() {
        @Override
        public void success(SubscriptionsResponse subscriptionsResponse, Response response) {
            if (subscriptionsResponse.success
                    && subscriptionsResponse.data != null
                    && !subscriptionsResponse.data.isEmpty()) {
                emptyView.setVisibility(View.GONE);
                subversesList.setAdapter(new SubscriptionAdapter(subscriptionsResponse.data));
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
        ButterKnife.bind(this);
        subversesList.setLayoutManager(new LinearLayoutManager(this));
        VoatClient.instance().getUserSubscriptions(User.getCurrentUser().getUserName(), subscriptionsResponseCallback);
    }

    public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

        private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                //gotoMain(mValues.get(position).getName());
                //TODO go to single Subverse screen
            }
        };

        private List<Subscription> mValues;

        public Subscription getValueAt(int position) {
            return mValues.get(position);
        }

        public SubscriptionAdapter(List<Subscription> items) {
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
            holder.itemView.setOnClickListener(onItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
